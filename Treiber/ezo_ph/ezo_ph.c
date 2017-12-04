/*
 * Copyright (C) 2017 HAW Hamburg
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 *
 */

/**
 * @ingroup     drivers_ezo_ph
 * @{
 *
 * @file
 * @brief       Driver for the AtlasScientific EZO^TM pH Circuit Sensor.
 *
 * @author      Katrin Moritz <katrin.moritz@haw-hamburg.de>
 *
 * @}
 */

#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>

#include "log.h"
#include "periph/i2c.h"
#include "ezo_ph.h"
#include "ezo_ph_regs.h"

#define ENABLE_DEBUG                (0)
#include "debug.h"

#define EZO_PH_CONFIG_RST           (1 << 15)

#define EZO_PH_CONFIG_MOD_SHIFT     (12U)
#define EZO_PH_CONFIG_MOD_MASK      (0x7000)
#define EZO_PH_CONFIG_MOD(x)        (((uint16_t)(((uint16_t)(x)) << EZO_PH_CONFIG_MOD_SHIFT))\
                                     & EZO_PH_CONFIG_MOD_MASK)
#define EZO_PH_CONFIG_MOD_CC        (0x07)
#define EZO_PH_CONFIG_MOD_OFF       (0x00)

#define EZO_PH_CONFIG_CR_SHIFT      (9U)
#define EZO_PH_CONFIG_CR_MASK       (0x0E00)
#define EZO_PH_CONFIG_CR(x)         (((uint16_t)(((uint16_t)(x)) << EZO_PH_CONFIG_CR_SHIFT))\
                                     & EZO_PH_CONFIG_CR_MASK)

#define EZO_PH_CONFIG_DRDY_PIN_EN   (1 << 8)
#define EZO_PH_CONFIG_DRDY          (1 << 7)

#define EZO_PH_MID_VALUE            (0x5449) /**< Manufacturer ID */
#define EZO_PH_DID_VALUE            (0x0067) /**< Device ID */

#define I2C_SPEED                   I2C_SPEED_FAST
#define BUS                         (dev->p.i2c)
#define ADDR                        (dev->p.addr)

int ezo_ph_init(ezo_ph_t *dev, const ezo_ph_params_t *params)
{
    /* check parameters */
    assert(dev && params);

    uint8_t reg[2];
    uint16_t ph;

    /* initialize the device descriptor */
    memcpy(&dev->p, params, sizeof(ezo_ph_params_t));

    if (dev->p.rate > EZO_PH_CONFIG_CR_AS16) {
        LOG_ERROR("ezo_ph_init: invalid conversion rate!\n");
        return -EZO_PH_ERROR_CONF;
    }

    /* setup the I2C bus */
    i2c_acquire(BUS);
    if (i2c_init_master(BUS, I2C_SPEED) < 0) {
        i2c_release(BUS);
        LOG_ERROR("ezo_ph_init: error initializing I2C bus\n");
        return -EZO_PH_ERROR_BUS;
    }
    /* test device id */
    if (i2c_read_regs(BUS, ADDR, EZO_PH_REGS_DEVICE_ID, reg, 2) != 2) {
        i2c_release(BUS);
        LOG_ERROR("ezo_ph_init: error reading device ID!\n");
        return -EZO_PH_ERROR_BUS;
    }
    ph = ((uint16_t)reg[0] << 8) | reg[1];
    if (ph != EZO_PH_DID_VALUE) {
        return -EZO_PH_ERROR_DEV;
    }
    /* set conversion rate */
    ph = EZO_PH_CONFIG_CR(dev->p.rate);
    reg[0] = (ph >> 8);
    reg[1] = ph;
    if (i2c_write_regs(BUS, ADDR, EZO_PH_REGS_CONFIG, reg, 2) != 2) {
        i2c_release(BUS);
        LOG_ERROR("ezo_ph_init: error setting conversion rate!\n");
        return -EZO_PH_ERROR_BUS;
    }
    i2c_release(BUS);

    return EZO_PH_OK;
}

int ezo_ph_reset(const ezo_ph_t *dev)
{
    uint8_t reg[2];
    uint16_t ph = EZO_PH_CONFIG_RST;
    reg[0] = (ph >> 8);
    reg[1] = ph;

    /* Acquire exclusive access to the bus. */
    i2c_acquire(BUS);
    if (i2c_write_regs(BUS, ADDR, EZO_PH_REGS_CONFIG, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }
    i2c_release(BUS);
    return EZO_PH_OK;
}

int ezo_ph_set_active(const ezo_ph_t *dev)
{
    uint8_t reg[2];

    i2c_acquire(BUS);
    if (i2c_read_regs(BUS, ADDR, EZO_PH_REGS_CONFIG, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }

    reg[0] |= (EZO_PH_CONFIG_MOD(EZO_PH_CONFIG_MOD_CC) >> 8);
    if (i2c_write_regs(BUS, ADDR, EZO_PH_REGS_CONFIG, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }
    i2c_release(BUS);
    return EZO_PH_OK;
}

int ezo_ph_set_standby(const ezo_ph_t *dev)
{
    uint8_t reg[2];

    i2c_acquire(BUS);
    if (i2c_read_regs(BUS, ADDR, EZO_PH_REGS_CONFIG, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }

    reg[0] &= ~(EZO_PH_CONFIG_MOD(EZO_PH_CONFIG_MOD_CC) >> 8);
    if (i2c_write_regs(BUS, ADDR, EZO_PH_REGS_CONFIG, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }
    i2c_release(BUS);
    return EZO_PH_OK;
}

int ezo_ph_read(const ezo_ph_t *dev, int16_t *rawv, int16_t *rawt, uint8_t *drdy)
{
    uint8_t reg[2];

    i2c_acquire(BUS);
    /* Register bytes are sent MSB first. */
    if (i2c_read_regs(BUS, ADDR, EZO_PH_REGS_CONFIG, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }
    i2c_release(BUS);

    *drdy = reg[1] & (EZO_PH_CONFIG_DRDY);
    if (!(*drdy)) {
        LOG_DEBUG("ezo_ph_read: conversion in progress!\n");
        return -EZO_PH_ERROR;
    }

    i2c_acquire(BUS);
    if (i2c_read_regs(BUS, ADDR, EZO_PH_REGS_V_OBJECT, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }
    i2c_release(BUS);

    *rawv = ((uint16_t)reg[0] << 8) | reg[1];

    i2c_acquire(BUS);
    if (i2c_read_regs(BUS, ADDR, EZO_PH_REGS_T_AMBIENT, reg, 2) != 2) {
        i2c_release(BUS);
        return -EZO_PH_ERROR_BUS;
    }
    i2c_release(BUS);
    *rawt = ((uint16_t)reg[0] << 8) | reg[1];
    return EZO_PH_OK;
}

void ezo_ph_convert(int16_t rawv, int16_t rawt,  float *ph)
{
    /* calculate die temperature */
    *tamb = (double)rawt / 128.0;
    /* die temperature in Kelvin */
    double tdie_k = *tamb + 273.15;

    /* calculate sensor voltage */
    double sens_v = (double)rawv * EZO_PH_CCONST_LSB_SIZE;

    double tdiff = tdie_k - EZO_PH_CCONST_TREF;
    double tdiff_pow2 = pow(tdiff, 2);

    double s = EZO_PH_CCONST_S0 * (1 + EZO_PH_CCONST_A1 * tdiff
                                   + EZO_PH_CCONST_A2 * tdiff_pow2);

    double v_os = EZO_PH_CCONST_B0 + EZO_PH_CCONST_B1 * tdiff
                  + EZO_PH_CCONST_B2 * tdiff_pow2;

    double f_obj = (sens_v - v_os) + EZO_PH_CCONST_C2 * pow((sens_v - v_os), 2);

    double t = pow(pow(tdie_k, 4) + (f_obj / s), 0.25);
    /* calculate object temperature in Celsius */
    *tobj = (t - 273.15);
}

int ezo_ph_read_ph(const ezo_ph_t *dev, int16_t *ph)
{
    int16_t rawtemp, rawvolt;
    float tamb, tobj;
    uint8_t drdy;
    ezo_ph_read(dev, &rawvolt, &rawtemp, &drdy);

    if (!drdy) {
        return EZO_PH_ERROR;
    }
    ezo_ph_convert(rawvolt, rawtemp,  &tamb, &tobj);
    *ta = (int16_t)(tamb*100);
    *to = (int16_t)(tobj*100);

    return EZO_PH_OK;
}

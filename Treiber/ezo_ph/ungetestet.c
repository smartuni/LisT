
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

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
 * @brief       Driver for the Atlas Scientific EZO^TM pH Circuit Sensor.
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
// Timer
#include "xtimer.h"
#include "timex.h"

#define ENABLE_DEBUG                (0)
#include "debug.h"


#define I2C_SPEED                   I2C_SPEED_FAST
#define BUS                         (dev->p.i2c)
#define ADDR                        (dev->p.addr)

int ezo_ph_init(ezo_ph_t *dev, const ezo_ph_params_t *params)
{
    /* check parameters */
    assert(dev && params);

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
}

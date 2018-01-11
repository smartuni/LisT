/*
 * Copyright (C) 2017 HAW Hamburg
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @defgroup    drivers_ezo_ph AtlasScientific EZO^TM pH Circuit Sensor
 * @ingroup     drivers_sensors
 * @brief       Driver for the AtlasScientific EZO^TM pH Circuit Sensor.
 *
 * @{
 *
 * @file
 * @brief       Interface definition for the ezo_ph sensor driver.
 *
 * @author      Katrin Moritz <katrin.moritz@haw-hambirg.de>
 */

#ifndef EZO_PH_H
#define EZO_PH_H

#include <stdint.h>
#include <stdbool.h>
#include "periph/i2c.h"

#ifdef __cplusplus
extern "C"
{
#endif

/**
 * @brief   EZO_PH Default Address
 */
#ifndef EZO_PH_I2C_ADDRESS
#define EZO_PH_I2C_ADDRESS         (0x63)
#endif

/**
 * @brief   Default Conversion Time in us
 */
#define EZO_PH_CONVERSION_TIME     (1E6)

/**
 * @name    Constants for EZO_PH calibration
 * @{
 */

/** @} */

/**
 * @brief   Parameters needed for device initialization
 */
typedef struct {
    i2c_t   i2c;        /**< I2C device, the sensor is connected to */
    uint8_t addr;       /**< the sensor's slave address on the I2C bus */
    uint8_t rate;       /**< number of averaged samples */
} ezo_ph_params_t;

/**
 * @brief   Device descriptor for EZO_PH sensors.
 */
typedef struct {
    ezo_ph_params_t p;  /**< Configuration parameters */
} ezo_ph_t;

/**
 * @brief   EZO_PH specific return values
 */
enum {
    EZO_PH_OK,          /**< Success, no error */
    EZO_PH_ERROR_BUS,   /**< I2C bus error */
    EZO_PH_ERROR_DEV,   /**< internal device error */
    EZO_PH_ERROR_CONF,  /**< invalid device configuration */
    EZO_PH_ERROR,       /**< general error */
};

/**
 * @brief   Initialize the EZO_PH sensor driver.
 *
 * @param[out] dev          device descriptor of sensor to initialize
 * @param[in]  params       configuration parameters
 *
 * @return                  0 on success
 * @return                  -EZO_PH_ERROR_BUS on I2C bus error
 * @return                  -EZO_PH_ERROR_DEV if sensor test failed
 * @return                  -EZO_PH_ERROR_CONF if sensor configuration failed
 */
int ezo_ph_init(ezo_ph_t *dev, const ezo_ph_params_t *params);

/**
 * @brief   Reset the EZO_PH sensor, afterwards it should be reinitialized.
 *
 * @param[out] dev          device descriptor of sensor
 *
 * @return                  0 on success
 * @return                  -1 on error
 */
int ezo_ph_reset(const ezo_ph_t *dev);

/**
 * @brief   Set active mode, this enables periodic measurements.
 *
 * @param[in]  dev          device descriptor of sensor
 *
 * @return                  0 on success
 * @return                  -1 on error
 */
int ezo_ph_set_active(const ezo_ph_t *dev);

/**
 * @brief   Set standby mode.
 *
 * @param[in]  dev          device descriptor of sensor
 *
 * @return                  0 on success
 * @return                  -1 on error
 */
int ezo_ph_set_standby(const ezo_ph_t *dev);

/**
 * @brief   Read sensor's data.
 *
 * @param[in]  dev          device descriptor of sensor
 * @param[out] rawv         object voltage value
 * @param[out] rawt         raw die pH
 * @param[out] drdy         data ready, 0 if a conversion is in progress
 *
 * @return                  0 on success
 * @return                  -1 on error
 */
int ezo_ph_read(const ezo_ph_t *dev, int16_t *rawv, int16_t *rawt, uint8_t *drdy);

/**
 * @brief   Convert raw sensor values to pH value.
 *
 * @param[in]  rawv         object voltage value
 * @param[in]  rawph        raw die pH value
 * @param[out] ph           converted pH value
 */
void ezo_ph_convert(int16_t rawv, int16_t rawt,  float *ph);

/**
 * @brief   Convenience function to get pH values 
 *
 * @note    
 *
 * @param[in]  dev          device descriptor of sensor
 * @param[out] ph           converted pH value
 */
int ezo_ph_read_ph(const ezo_ph_t *dev, int16_t *ph);

#ifdef __cplusplus
}
#endif

#endif /* EZO_PH_H */
/** @} */

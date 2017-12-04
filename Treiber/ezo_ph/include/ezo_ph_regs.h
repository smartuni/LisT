/*
 * Copyright (C) 2017 HAW Hamburg
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @ingroup     drivers_ezo_ph
 *
 * @{
 * @file
 * @brief       Register definitions for EZO pH devices
 *
 * @author      Katrin Moritz <katrin.moritz@haw-hamburg.de>
 */

#ifndef EZO_PH_REGS_H
#define EZO_PH_REGS_H

#include "board.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * @name    Register Map
 * @{
 */
 #define EZO_PH_REGS_V_OBJECT           0x00 /**< Sensor Voltage Register */
 #define EZO_PH_REGS_T_AMBIENT          0x01 /**< Ambient Temperature Register */
 #define EZO_PH_REGS_CONFIG             0x02 /**< Configuration Register */
 #define EZO_PH_REGS_MANUFACTURER_ID    0xFE /**< Manufacturer ID Register */
 #define EZO_PH_REGS_DEVICE_ID          0xFF /**< Device ID Register */
/** @} */

#ifdef __cplusplus
}
#endif

#endif /* EZO_PH_REGS_H */
/** @} */

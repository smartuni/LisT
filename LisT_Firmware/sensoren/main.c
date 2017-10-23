/*
 * Copyright (C) 2017 Freie Universität Berlin
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @{
 *
 * @file
 * @brief       Example for demonstrating SAUL and the SAUL registry
 *
 * @author      Hauke Petersen <hauke.petersen@fu-berlin.de>;
 *				Katrin Moritz <katrin.moritz@haw-hamburg.de>
 *
 * @}
 */

#include <stdio.h>
#include <stdlib.h>

#include "shell.h"
#include "xtimer.h"
#include "tmp006.h"
#include "tmp006_params.h"
#include "saul.h"
#include "saul_reg.h"
#include "debug.h"

/* set interval to 60 seconds */
#define INTERVAL (10U * US_PER_SEC)


int main(void)
{
	phydat_t temp_read, light_read;
    int dim_temp, dim_light;
	
	puts("Welcome to the RIOT-PO!\n");
	
	saul_reg_t *temp = saul_reg_find_name("tmp006");
    saul_reg_t *light = saul_reg_find_type(SAUL_SENSE_COLOR);

	if (temp == NULL || light == NULL) {
        DEBUG("[ERROR] Unable to find sensors\n");
        return 0;
	}

    xtimer_ticks32_t last_wakeup = xtimer_now();
    xtimer_sleep(1);
    while(1){
        
        xtimer_periodic_wakeup(&last_wakeup, INTERVAL);
        puts("Test periodic wakeup");
        dim_temp = saul_reg_read(temp, &temp_read);
        dim_light = saul_reg_read(light, &light_read);
        if(dim_temp < 0){
            puts("temp read error");
            continue;
        }
        if(dim_light < 0){
            puts("light read error");
            continue;
        }
        puts("Temperatur:");
        phydat_dump(&temp_read, dim_temp);  
        puts("RGB-Licht:");
        phydat_dump(&light_read, dim_light); 
                 
    }
        	
    return 0;
}

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
 *              Sebastian Frisch <JohannesSebastian.Frisch@haw-hambrug.de>
 *
 * @}
 */

#include <stdio.h>
#include <stdlib.h>

#include "msg.h"
#include "net/gcoap.h"
#include "kernel_types.h"

#include "shell.h"
#include "xtimer.h"
#include "tmp006.h"
#include "tmp006_params.h"
#include "saul.h"
#include "saul_reg.h"
#include "debug.h"

#include "net/gnrc/sixlowpan.h"

#include "net/ipv6/addr.h"
#include "net/gnrc/ipv6/netif.h"
#include "net/gnrc/netif.h"
#include "net/gnrc/netapi.h"
#include "net/netopt.h"
#include "net/gnrc/pkt.h"
#include "net/gnrc/pktbuf.h"
#include "net/gnrc/netif/hdr.h"
#include "net/gnrc/sixlowpan/netif.h"


/* set interval to 60 seconds */
#define INTERVAL (10U * US_PER_SEC)
#define MAIN_QUEUE_SIZE (4)
static msg_t _main_msg_queue[MAIN_QUEUE_SIZE];

extern int gcoap_cli_cmd(int argc, char **argv);
extern void gcoap_cli_init(void);

static const shell_command_t shell_commands[] = {
    { "coap", "CoAP example", gcoap_cli_cmd },
    { NULL, NULL, NULL }
};


int main(void)
{
	phydat_t temp_read, light_read;
    int dim_temp, dim_light;
    
    //change lowpan channel to 20
    uint16_t channel=20;
    kernel_pid_t ifs[GNRC_NETIF_NUMOF];
    gnrc_netif_get(ifs);
    gnrc_netapi_set(ifs[0], NETOPT_CHANNEL, 0, &channel, sizeof(channel));
    
    /* for the thread running the shell */
    msg_init_queue(_main_msg_queue, MAIN_QUEUE_SIZE);
    gcoap_cli_init();
    // 6LoWpan init
    gnrc_sixlowpan_init();
	
	puts("Welcome to the RIOT-PO!\n");
	
	saul_reg_t *temp = saul_reg_find_name("tmp006");
    saul_reg_t *light = saul_reg_find_type(SAUL_SENSE_COLOR);

	if (temp == NULL || light == NULL) {
        DEBUG("[ERROR] Unable to find sensors\n");
        return 0;
	}

    /* start shell */
    puts("All up, running the shell now");
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(shell_commands, line_buf, SHELL_DEFAULT_BUFSIZE);

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

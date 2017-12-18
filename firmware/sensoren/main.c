/*
 * Copyright (C) 2017 HAW Hamburg
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @{
 *
 * @file
 * @brief       reading the sensor values
 *
 * @author      Katrin Moritz <katrin.moritz@haw-hamburg.de>
 * @author      Sebastian Frisch <JohannesSebastian.Frisch@haw-hambrug.de>
 *
 * @}
 */

#include <stdio.h>
#include <stdlib.h>

//  CoAP
#include "msg.h"
#include "net/gcoap.h"
#include "kernel_types.h"

// SAUL, drivers
//#include "shell.h"
#include "saul.h"
#include "saul_reg.h"
#include "debug.h"

// Timer
#include "xtimer.h"
#include "timex.h"


// Network
#include "net/ipv6/addr.h"
#include "net/gnrc/ipv6/netif.h"
#include "net/gnrc/netif.h"
#include "net/gnrc/netapi.h"
#include "net/netopt.h"
#include "net/gnrc/pkt.h"
#include "net/gnrc/pktbuf.h"
#include "net/gnrc/netif/hdr.h"
#include "net/gnrc/sixlowpan.h"
#include "net/gnrc/sixlowpan/netif.h"

#include "gcoap_cli.h"


#define MAIN_QUEUE_SIZE (4)

static msg_t _main_msg_queue[MAIN_QUEUE_SIZE];

extern int gcoap_cli_cmd(int argc, char **argv);
extern void gcoap_cli_init(void);

/*
static const shell_command_t shell_commands[] = {
    { "coap", "CoAP example", gcoap_cli_cmd },
    { NULL, NULL, NULL }
};*/



int main(void)
{
    // sensors
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
	
	saul_reg_t *temp_saul = saul_reg_find_name("tmp006");
    saul_reg_t *light_saul = saul_reg_find_type(SAUL_SENSE_COLOR);

	if (temp_saul == NULL || light_saul == NULL) {
        DEBUG("[ERROR] Unable to find sensors\n");
        return 0;
	}

    /* start shell */
    /*
    puts("All up, running the shell now");
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(shell_commands, line_buf, SHELL_DEFAULT_BUFSIZE);
    */

    xtimer_sleep(1);
    while(1){
        
        //_send((uint8_t *)"TESTING", sizeof("TESTING"), "ff02::1", "5683");
        
        //xtimer_periodic_wakeup(&last_wakeup, INTERVAL);
        //puts("Test periodic wakeup");
        
        dim_temp = saul_reg_read(temp_saul, &temp_read);
        dim_light = saul_reg_read(light_saul, &light_read);
        if(dim_temp < 0){
            puts("temp read error");
            strcpy(msg_temp, "error");
            continue;
        }
        else{
            strcpy(msg_temp, "ok");
        }
        if(dim_light < 0) {
            puts("light read error");
            strcpy(msg_light, "error");
            continue;
        }
        else{
            strcpy(msg_light, "ok");
        }
        
        //to be requested by coap-client
        temp = temp_read;
        light = light_read;
        
        phydat_dump(&temp_read, dim_temp);
        phydat_dump(&light_read, dim_light);
        
        puts("Temperatur:");
        printf("temp.val[0] = %d\n", temp.val[0]);
        printf("temp.val[1] = %d\n", temp.val[1]);
        puts("RGB-Licht:");
        printf("light.val[0] = %d\n", light.val[0]);
        printf("light.val[1] = %d\n", light.val[1]);
        printf("light.val[2] = %d\n", light.val[2]);
        printf("/cli/stats: %d\n", req_count);
        printf("-------------------------------------------\n");
        
        xtimer_sleep(1);
    }
        	
    return 0;
}

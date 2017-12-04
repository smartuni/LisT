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
 * @brief       controlling 
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
/*#include "saul.h"
#include "saul_reg.h"
#include "debug.h"
*/

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

// LED bar
#include "log.h"
#include "xtimer.h"
#include "grove_ledbar.h"
#include "grove_ledbar_params.h"

#include "gcoap_cli.h"

/*'
#include "shell.h"
#include "periph/gpio.h"
*/
// RGB-LED
#include "rgbled.h"

#define MAIN_QUEUE_SIZE (4)

/*
static msg_t _main_msg_queue[MAIN_QUEUE_SIZE];

extern int gcoap_cli_cmd(int argc, char **argv);
extern void gcoap_cli_init(void);

static const shell_command_t shell_commands[] = {
    { "coap", "CoAP example", gcoap_cli_cmd },
    { NULL, NULL, NULL }
};*/


int main(void)
{
    
    //change lowpan channel to 20
    uint16_t channel=20;
    kernel_pid_t ifs[GNRC_NETIF_NUMOF];
    gnrc_netif_get(ifs);
    gnrc_netapi_set(ifs[0], NETOPT_CHANNEL, 0, &channel, sizeof(channel));
    
    /* for the thread running the shell */
    /*msg_init_queue(_main_msg_queue, MAIN_QUEUE_SIZE);
    gcoap_cli_init();
    // 6LoWpan init
    gnrc_sixlowpan_init();
    */

    // RGB values
    uint8_t red = 0, green = 0, blue = 0;
    rgbled_t led;
    color_rgb_t color = {red, green, blue};
    rgbled_init(&led, PWM_DEV(1), 0, 1, 2);

    // heating values/ temp values
    grove_ledbar_t dev;
    uint16_t temp_set = 0;
    /* init display */
    if (grove_ledbar_init(&dev, &grove_ledbar_params[0]) != 0) {
        puts("[FAILED]");
        return 1;
    }
    
    // begin
    puts("Welcome to the RIOT-PO!\n");
    
    /*
    puts("All up, running the shell now");
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(shell_commands, line_buf, SHELL_DEFAULT_BUFSIZE);
*/    

    while(1){

        // set RGB values
        color.r = red;
        color.g = green;
        color.b = blue;
        rgbled_set(&led, &color);

        // set heting values
        temp_set = temp;
        grove_ledbar_set(&dev, temp_set);
    }
            
    return 0;

} 
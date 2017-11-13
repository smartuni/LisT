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
#include "saul.h"
#include "saul_reg.h"
#include "debug.h"


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

// RGB-LED
#include "rgbled.h"

#define MAIN_QUEUE_SIZE (4)


static msg_t _main_msg_queue[MAIN_QUEUE_SIZE];

extern int gcoap_cli_cmd(int argc, char **argv);
extern void gcoap_cli_init(void);

int main(void)
{

    // RGB values
    rgbled_t led; 
    uint8_t red = 0;
    uint8_t green = 0;
    uint8_t blue = 0;
    color_rgb_t color = {red, green, blue};
    rgbled_init(&led, PWM_DEV(1), 0, 1, 2);
    
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
    
    while(1){

        rgbled_set(&led, &color);
    }
            
    return 0;
}

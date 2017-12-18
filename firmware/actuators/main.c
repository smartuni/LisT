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


// RGB-LED
#include "rgbled.h"

// LED bar
#include "log.h"
#include "xtimer.h"
#include "grove_ledbar.h"
#include "grove_ledbar_params.h"

// heating
#include "periph/gpio.h"


// CoAP
#include "gcoap_cli.h" // must be last include

/*
#include "shell.h"
*/

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

    // temp values
    grove_ledbar_t dev;
    phydat_t temp_set = { .val = {0}, .unit = 0, .scale = 0};
    uint8_t led_set = 0;
    /* init display */
    if (grove_ledbar_init(&dev, &grove_ledbar_params[0]) != 0) {
        puts("[FAILED]");
        return 1;
    }

    // heating
    gpio_init(GPIO_PIN(0,23), GPIO_OUT);
    
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

        // set temp values
        temp_set.val[0] = temp.val[0];
        
        if(temp_set.val[0]<18) led_set = 0;                           // no bar
        if(temp_set.val[0]>=18 && temp_set.val[0]<=19) led_set = 25;  // 1 bar
        if(temp_set.val[0]>19 && temp_set.val[0]<=20) led_set = 50;   // 2 bar
        if(temp_set.val[0]>20 && temp_set.val[0]<=21) led_set = 75;   // 3 bar
        if(temp_set.val[0]>21 && temp_set.val[0]<=22) led_set = 100;  // 4 bar
        if(temp_set.val[0]>22 && temp_set.val[0]<=23) led_set = 125;  // 5 bar
        if(temp_set.val[0]>23 && temp_set.val[0]<=24) led_set = 150;  // 6 bar
        if(temp_set.val[0]>24 && temp_set.val[0]<=25) led_set = 175;  // 7 bar
        if(temp_set.val[0]>25 && temp_set.val[0]<=26) led_set = 200;  // 8 bar
        if(temp_set.val[0]>26 && temp_set.val[0]<=28) led_set = 225;  // 9 bar
        if(temp_set.val[0]>28) led_set = 255;                         // 10 bar
        grove_ledbar_set(&dev, led_set);

        //heating
        if(!heat){
            gpio_clear(GPIO_PIN(0,23));
        }else{
            gpio_set(GPIO_PIN(0,23));
        }
    }
            
    return 0;

} 

#include <stdio.h>

// RGB-LED
#include "rgbled.h"


int main(void)
{
    // RGB values
    rgbled_t led; 
    uint8_t red = 200;
    uint8_t green = 50;
    uint8_t blue = 50;
    color_rgb_t color = {red, green, blue};
    rgbled_init(&led, PWM_DEV(1), 0, 1, 2);
    
    puts("Welcome to the RIOT-PO!\n");
    printf("Available PWM devices: %i\n", PWM_NUMOF);
    

    while (1) {
        rgbled_set(&led, &color);
    }
    
    
    /*
    printf("device: %i\n", led.device);
    printf("channel_r: %i\n", led.channel_r);
    printf("channel_g: %i\n", led.channel_g);
    printf("channel_b: %i\n", led.channel_b);
    printf("red: %i\n", color.r);
    printf("green: %i\n", color.g);
    printf("blue: %i\n", color.b);
        */	
    return 0;
}

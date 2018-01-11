#include <stdint.h>

#include <stdio.h>
#include <stdlib.h>

#include "cpu.h"
#include "irq.h"
#include "mutex.h"
#include "periph_conf.h"
#include "periph/i2c.h"
#include "debug.h"

int main(void)
{
	i2c_init_master(0, I2C_SPEED_NORMAL);



    return 0;
}
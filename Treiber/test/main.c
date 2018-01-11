/*
 * Copyright (C) 2008, 2009, 2010 Kaspar Schleiser <kaspar@schleiser.de>
 * Copyright (C) 2013 INRIA
 * Copyright (C) 2013 Ludwig Knüpfer <ludwig.knuepfer@fu-berlin.de>
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @ingroup     examples
 * @{
 *
 * @file
 * @brief       Default application that shows a lot of functionality of RIOT
 *
 * @author      Kaspar Schleiser <kaspar@schleiser.de>
 * @author      Oliver Hahm <oliver.hahm@inria.fr>
 * @author      Ludwig Knüpfer <ludwig.knuepfer@fu-berlin.de>
 *
 * @}
 */

#include <stdio.h>
#include <string.h>

#include "thread.h"
#include "shell.h"
#include "shell_commands.h"

#include <stdint.h>

#include "cpu.h"
#include "irq.h"
#include "mutex.h"
#include "periph_conf.h"
#include "periph/i2c.h"
// Timer
#include "xtimer.h"
#include "timex.h"

int main(void)
{

	i2c_t dev = 0;
	uint8_t address = 0x63;
	uint8_t bytes = 15;
	char data[bytes];
	
	char write[] = "Status";
	

    (void) puts("Welcome to RIOT!");

/*
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(NULL, line_buf, SHELL_DEFAULT_BUFSIZE);
*/
    // init master
    i2c_init_master(dev, I2C_SPEED_NORMAL);
    xtimer_usleep(300*1000);

    // write command
  	i2c_write_bytes(dev, address, write, sizeof(write)-1);
  	xtimer_usleep(900*1000);

  	//read
  	/*
	i2c_read_regs(dev, address, *write, data, bytes);
	xtimer_usleep(300*1000);
	*/
	i2c_read_bytes(dev, address, data, bytes);
	xtimer_usleep(300*1000);

	printf("Command: 0x%x, %s\n", *write, write);

	if (data[0] == 254)
	{
		puts("still processing, not ready");
	}else if (data[0]==255)
	{
		puts("no data to send");
	}else if (data[0] == 2)
	{
		puts("ERROR!");
	}else if (data[0] == 1)
	{
		puts("SUCCESS");
	}

	printf("Datenbyte: %s\n",  data);
	
	for (int i = 0; i < bytes; ++i)
	{
		printf("Datenbyte %i: 0x%x, %c\n", i, data[i], data[i]);
	}
	

    puts("---ENDE---");
    return 0;
}

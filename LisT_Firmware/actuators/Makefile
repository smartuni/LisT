# name of your application
APPLICATION = actuators

# If no BOARD is found in the environment, use this default:
BOARD ?= samr21-xpro

# This has to be the absolute path to the RIOT base directory:
RIOTBASE ?= $(CURDIR)/../../../RIOT

#####################################

BOARD_INSUFFICIENT_MEMORY := chronos msb-430 msb-430h nucleo32-f031 nucleo32-f042 \
                             nucleo32-l031 nucleo-f030 nucleo-f334 nucleo-l053 \
                             stm32f0discovery telosb wsn430-v1_3b wsn430-v1_4 z1
# rgb led
USEMODULE += rgbled

# Include packages that pull up and auto-init the link layer.
# NOTE: 6LoWPAN will be included if IEEE802.15.4 devices are present
USEMODULE += gnrc_netdev_default
USEMODULE += auto_init_gnrc_netif
# Specify the mandatory networking modules
USEMODULE += gnrc_ipv6_router_default
USEMODULE += gcoap
# Additional networking modules that can be dropped if not needed
USEMODULE += gnrc_icmpv6_echo

# Required by gcoap example
USEMODULE += od
USEMODULE += fmt

#######################################
# grove led bar
USEMODULE += grove_ledbar
# set default device parameters in case they are undefined
# the following params are for board pba-d-01-kw2x and pins PA01 and PA02
TEST_GROVE_LEDBAR_CLK ?= 	GPIO_PIN\(0,13\)
TEST_GROVE_LEDBAR_DAT ?= 	GPIO_PIN\(0,28\)
TEST_GROVE_LEDBAR_DIR ?=	GROVE_LEDBAR_G2R

# export parameters
CFLAGS += -DGROVE_LEDBAR_CLK=$(TEST_GROVE_LEDBAR_CLK)
CFLAGS += -DGROVE_LEDBAR_DAT=$(TEST_GROVE_LEDBAR_DAT)
CFLAGS += -DGROVE_LEDBAR_DIR=$(TEST_GROVE_LEDBAR_DIR)
#####################################
# heating
FEATURES_REQUIRED = periph_gpio
#########################################

# Add also the shell, some shell commands
USEMODULE += shell
USEMODULE += shell_commands
USEMODULE += ps
#######################################


# include and auto-initialize all available sensors
#USEMODULE += saul_default
# we want to use xtimer:
USEMODULE += xtimer

# Comment this out to disable code in RIOT that does safety checking
# which is not needed in a production environment but helps in the
# development process:
CFLAGS += -DDEVELHELP

# Change this to 0 show compiler invocation lines by default:
QUIET ?= 1

include $(RIOTBASE)/Makefile.include

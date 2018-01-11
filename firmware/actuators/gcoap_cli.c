/*
 * Copyright (c) 2015-2016 Ken Bannister. All rights reserved.
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @{
 *
 * @file
 * @brief       gcoap CLI support
 *
 * @author      Ken Bannister <kb2ma@runbox.com>
 *
 * @user        Sebastian Frisch <JohannesSebastian.Frisch@haw-hambrug.de>
 * 
 * @}
 */

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "net/gcoap.h"
#include "od.h"
#include "fmt.h"
#include "phydat.h"

#include "random.h"

#include "gcoap_cli.h"

#define ENABLE_DEBUG (0)
#include "debug.h"

static void _resp_handler(unsigned req_state, coap_pkt_t* pdu,
                          sock_udp_ep_t *remote);
static ssize_t _stats_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);

static ssize_t _ledbar_status_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);
static ssize_t _ledbar_temp_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);

static ssize_t _ledstatus_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);
static ssize_t _led_red_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);
static ssize_t _led_green_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);
static ssize_t _led_blue_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);

static ssize_t _heat_status_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);
static ssize_t _heatswitch_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);

static ssize_t _riot_board_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);
static ssize_t _status_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len);

/* CoAP resources */
/* must be in alphabetical order */
static const coap_resource_t _resources[] = {
    {"/cli/stats", COAP_GET | COAP_PUT, _stats_handler},
    {"/heat", COAP_GET , _heat_status_handler},
    {"/heat/switch", COAP_GET | COAP_PUT, _heatswitch_handler},
    {"/led", COAP_GET, _ledstatus_handler},
    {"/led/blue", COAP_GET | COAP_PUT, _led_blue_handler},
    {"/led/green", COAP_GET | COAP_PUT, _led_green_handler},
    {"/led/red", COAP_GET | COAP_PUT, _led_red_handler},
    {"/riot/board", COAP_GET, _riot_board_handler},
    {"/status", COAP_GET, _status_handler},
    {"/ledbar", COAP_GET, _ledbar_status_handler},
    {"/ledbar/temp", COAP_GET | COAP_PUT, _ledbar_temp_handler},
};

static gcoap_listener_t _listener = {
    (coap_resource_t *)&_resources[0],
    sizeof(_resources) / sizeof(_resources[0]),
    NULL
};

/* Counts requests sent by CLI. */
static uint16_t req_count = 0;

//data variables
uint8_t red = 0;
uint8_t green = 0;
uint8_t blue = 0;
uint8_t heat = 0;
phydat_t temp = { .val = {0}, .unit = 0, .scale = 0};

/*
 * Response callback.
 */
static void _resp_handler(unsigned req_state, coap_pkt_t* pdu,
                          sock_udp_ep_t *remote)
{
    (void)remote;       /* not interested in the source currently */

    if (req_state == GCOAP_MEMO_TIMEOUT) {
        printf("gcoap: timeout for msg ID %02u\n", coap_get_id(pdu));
        return;
    }
    else if (req_state == GCOAP_MEMO_ERR) {
        printf("gcoap: error in response\n");
        return;
    }

    char *class_str = (coap_get_code_class(pdu) == COAP_CLASS_SUCCESS)
                            ? "Success" : "Error";
    printf("gcoap: response %s, code %1u.%02u", class_str,
                                                coap_get_code_class(pdu),
                                                coap_get_code_detail(pdu));
    if (pdu->payload_len) {
        if (pdu->content_type == COAP_FORMAT_TEXT
                || pdu->content_type == COAP_FORMAT_LINK
                || coap_get_code_class(pdu) == COAP_CLASS_CLIENT_FAILURE
                || coap_get_code_class(pdu) == COAP_CLASS_SERVER_FAILURE) {
            /* Expecting diagnostic payload in failure cases */
            printf(", %u bytes\n%.*s\n", pdu->payload_len, pdu->payload_len,
                                                          (char *)pdu->payload);
        }
        else {
            printf(", %u bytes\n", pdu->payload_len);
            od_hex_dump(pdu->payload, pdu->payload_len, OD_WIDTH_DEFAULT);
        }
    }
    else {
        printf(", empty payload\n");
    }
}

/*
 * Server callback for /cli/stats. Accepts either a GET or a PUT.
 *
 * GET: Returns the count of packets sent by the CLI.
 * PUT: Updates the count of packets. Rejects an obviously bad request, but
 *      allows any two byte value for example purposes. Semantically, the only
 *      valid action is to set the value to 0.
 */
static ssize_t _stats_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len)
{
    /* read coap method type in packet */
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));

    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            /* write the response buffer with the request count value */
            size_t payload_len = fmt_u16_dec((char *)pdu->payload, req_count);

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_TEXT);

        case COAP_PUT:
            /* convert the payload to an integer and update the internal
               value */
            if (pdu->payload_len <= 5) {
                char payload[6] = { 0 };
                memcpy(payload, (char *)pdu->payload, pdu->payload_len);
                req_count = (uint16_t)strtoul(payload, NULL, 10);
                return gcoap_response(pdu, buf, len, COAP_CODE_CHANGED);
            }
            else {
                return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
            }
    }

    return 0;
}

static ssize_t _status_handler(coap_pkt_t* pdu, uint8_t* buf, size_t len)
{
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));
    uint8_t i = 0;
    uint8_t j = 0;
    uint8_t count = 0;
    uint8_t max = (uint8_t)_listener.resources_len;
    char output[200] = "";
    char ch[1] = "/";
    signed int check = 0;
    
    uint32_t rand_number = random_uint32_range(500000, 5000000); //get random number between 0.5s and 5s
    xtimer_usleep(rand_number);
    
    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            // write the response buffer with the requested data (temp)
            //NOTE: signed value for data
            //size_t payload_len = fmt_s16_dec((char *)pdu->payload, temp.val[0]);
            
            printf("max = %d\n", max);
            
            sprintf(output+strlen(output), "{\"Res\": [");
            for(i=0; i<max; i++){
                for (j = 0; _resources[i].path[j] != '\0'; j++){
                    if (_resources[i].path[j] == ch[0]){
                        count++;
                    }
                }
                if(count < 2){      //if less then 2 "/" in string
                    sprintf(output+strlen(output) ,"\"%s\"", _resources[i].path);
                
                    if(i<(max-1)){
                    sprintf(output+strlen(output), ", ");
                    }
                }
                count = 0;

                
            }
            sprintf(output+strlen(output), "]}");
            
            size_t payload_len = sprintf((char *)pdu->payload, output);

            check = gcoap_finish(pdu, payload_len, COAP_FORMAT_JSON);
            printf("RETURN = %d\n", check);
            return check;
    }
    
    return 0;
}


static ssize_t _ledbar_status_handler(coap_pkt_t* pdu, uint8_t* buf, size_t len)
{
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));
    
    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            // write the response buffer with the requested data (temp) 
            //NOTE: signed value for data
            //size_t payload_len = fmt_s16_dec((char *)pdu->payload, temp.val[0]);
            
            size_t payload_len = sprintf((char *)pdu->payload, "{\"/led/red\": [\"GET, PUT\"], \"/led/green\": [\"GET, PUT\"], \"/led/blue\": [\"GET, PUT\"], \"message\": \"ok\"}");

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_JSON);

    }
    
    return 0;
}

static ssize_t _ledbar_temp_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len)
{
    /* read coap method type in packet */
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));

    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            /* write the response buffer with the request count value */
            size_t payload_len = fmt_u16_dec((char *)pdu->payload, temp.val[0]);

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_TEXT);

        case COAP_PUT:
            /* convert the payload to an integer and update the internal
               value */
            if (pdu->payload_len <= 5) {
                char payload[6] = { 0 };
                memcpy(payload, (char *)pdu->payload, pdu->payload_len);
                temp.val[0] = (uint16_t)strtoul(payload, NULL, 10);
                return gcoap_response(pdu, buf, len, COAP_CODE_CHANGED);
            }
            else {
                return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
            }
    }

    return 0;
}

static ssize_t _heat_status_handler(coap_pkt_t* pdu, uint8_t* buf, size_t len)
{
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));
    
    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            // write the response buffer with the requested data (temp) 
            //NOTE: signed value for data
            //size_t payload_len = fmt_s16_dec((char *)pdu->payload, temp.val[0]);
            
            size_t payload_len = sprintf((char *)pdu->payload, "{\"/heat/switch\": [\"GET, PUT\"], \"message\": \"ok\"}");

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_JSON);

    }
    
    return 0;
}

static ssize_t _heatswitch_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len)
{
    /* read coap method type in packet */
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));

    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            /* write the response buffer with the request count value */
            size_t payload_len = fmt_u16_dec((char *)pdu->payload, heat);

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_TEXT);

        case COAP_PUT:
            /* convert the payload to an integer and update the internal
               value */
            if (pdu->payload_len <= 5) {
                char payload[6] = { 0 };
                memcpy(payload, (char *)pdu->payload, pdu->payload_len);
                heat = (uint16_t)strtoul(payload, NULL, 10);
                return gcoap_response(pdu, buf, len, COAP_CODE_CHANGED);
            }
            else {
                return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
            }
    }

    return 0;
}

static ssize_t _ledstatus_handler(coap_pkt_t* pdu, uint8_t* buf, size_t len)
{
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));
    
    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            // write the response buffer with the requested data (temp) 
            //NOTE: signed value for data
            //size_t payload_len = fmt_s16_dec((char *)pdu->payload, temp.val[0]);
            
            size_t payload_len = sprintf((char *)pdu->payload, "{\"/ledbar/temp\": [\"GET, PUT\"], \"message\": \"ok\"}");

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_JSON);

    }
    
    return 0;
}

static ssize_t _led_red_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len)
{
    /* read coap method type in packet */
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));

    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            /* write the response buffer with the request count value */
            size_t payload_len = fmt_u16_dec((char *)pdu->payload, red);

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_TEXT);

        case COAP_PUT:
            /* convert the payload to an integer and update the internal
               value */
            if (pdu->payload_len <= 5) {
                char payload[6] = { 0 };
                memcpy(payload, (char *)pdu->payload, pdu->payload_len);
                red = (uint16_t)strtoul(payload, NULL, 10);
                return gcoap_response(pdu, buf, len, COAP_CODE_CHANGED);
            }
            else {
                return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
            }
    }

    return 0;
}

static ssize_t _led_green_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len)
{
    /* read coap method type in packet */
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));

    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            /* write the response buffer with the request count value */
            size_t payload_len = fmt_u16_dec((char *)pdu->payload, green);

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_TEXT);

        case COAP_PUT:
            /* convert the payload to an integer and update the internal
               value */
            if (pdu->payload_len <= 5) {
                char payload[6] = { 0 };
                memcpy(payload, (char *)pdu->payload, pdu->payload_len);
                green = (uint16_t)strtoul(payload, NULL, 10);
                return gcoap_response(pdu, buf, len, COAP_CODE_CHANGED);
            }
            else {
                return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
            }
    }

    return 0;
}

static ssize_t _led_blue_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len)
{
    /* read coap method type in packet */
    unsigned method_flag = coap_method2flag(coap_get_code_detail(pdu));

    switch(method_flag) {
        case COAP_GET:
            gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

            /* write the response buffer with the request count value */
            size_t payload_len = fmt_u16_dec((char *)pdu->payload, blue);

            return gcoap_finish(pdu, payload_len, COAP_FORMAT_TEXT);

        case COAP_PUT:
            /* convert the payload to an integer and update the internal
               value */
            if (pdu->payload_len <= 5) {
                char payload[6] = { 0 };
                memcpy(payload, (char *)pdu->payload, pdu->payload_len);
                blue = (uint16_t)strtoul(payload, NULL, 10);
                return gcoap_response(pdu, buf, len, COAP_CODE_CHANGED);
            }
            else {
                return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
            }
    }

    return 0;
}


static ssize_t _riot_board_handler(coap_pkt_t *pdu, uint8_t *buf, size_t len)
{
    gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);
    /* write the RIOT board name in the response buffer */
    memcpy(pdu->payload, RIOT_BOARD, strlen(RIOT_BOARD));
    return gcoap_finish(pdu, strlen(RIOT_BOARD), COAP_FORMAT_TEXT);
}

static size_t _send(uint8_t *buf, size_t len, char *addr_str, char *port_str)
{
    ipv6_addr_t addr;
    size_t bytes_sent;
    sock_udp_ep_t remote;

    remote.family = AF_INET6;
    remote.netif  = SOCK_ADDR_ANY_NETIF;

    /* parse destination address */
    if (ipv6_addr_from_str(&addr, addr_str) == NULL) {
        puts("gcoap_cli: unable to parse destination address");
        return 0;
    }
    memcpy(&remote.addr.ipv6[0], &addr.u8[0], sizeof(addr.u8));

    /* parse port */
    remote.port = atoi(port_str);
    if (remote.port == 0) {
        puts("gcoap_cli: unable to parse destination port");
        return 0;
    }

    bytes_sent = gcoap_req_send2(buf, len, &remote, _resp_handler);
    if (bytes_sent > 0) {
        req_count++;
    }
    return bytes_sent;
}

int gcoap_cli_cmd(int argc, char **argv)
{
    /* Ordered like the RFC method code numbers, but off by 1. GET is code 0. */
    char *method_codes[] = {"get", "post", "put"};
    uint8_t buf[GCOAP_PDU_BUF_SIZE];
    coap_pkt_t pdu;
    size_t len;

    if (argc == 1) {
        /* show help for main commands */
        goto end;
    }

    for (size_t i = 0; i < sizeof(method_codes) / sizeof(char*); i++) {
        if (strcmp(argv[1], method_codes[i]) == 0) {
            if (argc == 5 || argc == 6) {
                if (argc == 6) {
                    gcoap_req_init(&pdu, &buf[0], GCOAP_PDU_BUF_SIZE, i+1, argv[4]);
                    memcpy(pdu.payload, argv[5], strlen(argv[5]));
                    len = gcoap_finish(&pdu, strlen(argv[5]), COAP_FORMAT_TEXT);
                }
                else {
                    len = gcoap_request(&pdu, &buf[0], GCOAP_PDU_BUF_SIZE, i+1,
                                                                           argv[4]);
                }
                printf("gcoap_cli: sending msg ID %u, %u bytes\n", coap_get_id(&pdu),
                       (unsigned) len);
                if (!_send(&buf[0], len, argv[2], argv[3])) {
                    puts("gcoap_cli: msg send failed");
                }
                else {
                    /* send Observe notification for /cli/stats */
                    switch (gcoap_obs_init(&pdu, &buf[0], GCOAP_PDU_BUF_SIZE,
                            &_resources[1])) {
                    case GCOAP_OBS_INIT_OK:
                        DEBUG("gcoap_cli: creating /cli/stats notification\n");
                        size_t payload_len = fmt_u16_dec((char *)pdu.payload, req_count);
                        len = gcoap_finish(&pdu, payload_len, COAP_FORMAT_TEXT);
                        gcoap_obs_send(&buf[0], len, &_resources[1]);
                        break;
                    case GCOAP_OBS_INIT_UNUSED:
                        DEBUG("gcoap_cli: no observer for /cli/stats\n");
                        break;
                    case GCOAP_OBS_INIT_ERR:
                        DEBUG("gcoap_cli: error initializing /cli/stats notification\n");
                        break;
                    }
                }
                return 0;
            }
            else {
                printf("usage: %s <get|post|put> <addr> <port> <path> [data]\n",
                       argv[0]);
                return 1;
            }
        }
    }

    if (strcmp(argv[1], "info") == 0) {
        if (argc == 2) {
            uint8_t open_reqs = gcoap_op_state();

            printf("CoAP server is listening on port %u\n", GCOAP_PORT);
            printf(" CLI requests sent: %u\n", req_count);
            printf("CoAP open requests: %u\n", open_reqs);
            return 0;
        }
    }

    end:
    printf("usage: %s <get|post|put|info>\n", argv[0]);
    return 1;
}

void gcoap_cli_init(void)
{
    gcoap_register_listener(&_listener);
}

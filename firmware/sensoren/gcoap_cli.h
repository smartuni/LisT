
#ifndef GCOAP_CLI
#define GCOAP_CLI

extern uint16_t req_count;
extern phydat_t temp;
extern phydat_t light;

extern char msg_temp[10];
extern char msg_light[10];

#define PRINT_ON_CONSOLE 1

//static 
//size_t _send(uint8_t *buf, size_t len, char *addr_str, char *port_str);

#endif

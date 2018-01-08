/*
*
* ############ Run Parameters ##########################################################################################
*
 */

var argv = require('minimist')(process.argv.slice(2));

// if (argv.help) {
//     console.log("Parameter description:");
//     console.log("Option                         Description");
//     console.log("--mqttAddress                  Something like mqtt://141.22.28.86 ");
//     // console.log("");
//     console.log("--logLevel                     DEFAULT:    debug ");
//     console.log("                               OPTIONS:    debug, info ");
//     // console.log("");
//     console.log("--coapInterface                DEFAULT:    lowpan0");
//     // console.log("");
//     console.log("--coapMulticastAddress         DEFAULT:    ff02::1");
//     // console.log("");
//     console.log("--coapPort                     DEFAULT:    5873");
//     process.exit(0);
// }

//# Parameter ##### exists? ######### copy #### default ##
var logLevel = (argv.logLevel ? argv.logLevel : 'debug');

//######################################################################################################################

//var colors = require('colors');
var q = require('q');
var mqtt = require('mqtt');
var log = require('console-log-level')({level: logLevel});
var Jetty = require("jetty");


// colors.setTheme({
//     silly: 'rainbow',
//     input: 'grey',
//     verbose: 'blue',
//     prompt: 'grey',
//     info: 'green',
//     data: 'cyan',
//     help: 'cyan',
//     warn: 'yellow',
//     debug: 'grey',
//     error: 'red',
//     serverColor: 'green'
// });


/*
*
* #### Connection Config ###############################################################################################
*
*/
var coapConfig = {
    multicastAddress: "ff02::1",
    port: 5873,
    interface: "lowpan0",
    discoveryPfad: "/status",
    discoveryTimeout: 2000,
};


var Frontend = {
    url: 'mqtt://141.22.28.86',
    port: '1883',
    subscribeTopic: 'actor',
    publishTopic: 'sensor',
    status: 'disconnected',
    connect: function () {
        Frontend.client = mqtt.connect(Frontend.url, {
            port: Frontend.port
        });
    }
};


var coapNodes = [];
//
// var coapNodes = [{
//     hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
//     statusResponse: "Something like ",
//     supportedPaths: [
//         {
//             path: "/temp",
//             typ: "GET",
//
//         }, {
//             path: "/light/rgb",
//             typ: "GET",
//
//         }],
// }, {
//     hostname: "fe80::7b62:1b6d:89cc:89ca%lowpan0",
//     supportedPaths: [
//         {
//             path: "/temp",
//             typ: "GET",
//
//         }, "/light"],
// }];


var sensorNodeOne = {
    hostname: "ff02::1%lowpan0",
    //hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
    temperaturePath: "/temp",
    surfaceTemp: "0",
    ambientTemp: "0",
    message: "",
    lightPath: "/light",
    lightValue: {
        red: "0",
        green: "0",
        blue: "0"

    },
    writePath: "/cli/stats"
};

var sensorNodeTwo = {
    hostname: "fe80::7b79:4946:539e:75e%lowpan0",
    lampPaths: {
        red: "/red",
        green: "/green",
        blue: "/blue"
    },
    lampValue: {
        red: "0",
        green: "0",
        blue: "0"

    }
};

var data = {
    sensor: 's1',
    typ: 'temp',
    value: [],
    timestamp: '',

};

var communicationStatus = {
    sumPackages: 0,
    faildPackages: 0,
    transmittedPackages: 0,
    numberNodes: 0,
    numberRessources: 0
};


/*
*
*
*
*/
var jetty = new Jetty(process.stdout);

// jetty.clear();

Frontend.connect();

Frontend.client.on('connect', function () {
    Frontend.status = 'connected';
    Frontend.client.subscribe(Frontend.subscribeTopic);
    log.debug("Frontend connected and subscribed to: " + Frontend.subscribeTopic);
});

Frontend.client.on('reconnect', function () {
    Frontend.status = 'reconnect';
});

Frontend.client.on('close', function () {
    Frontend.status = 'disconnected';
});

Frontend.client.on('error', function (error) {
    log.error('MQTT Client Errored' + error);
});

Frontend.client.on('message', function (topic, message) {
    //TODO Handle incomming messages

    log.debug("MQTT Message recieved: " + message.toString());

});


//console.log(data);
console.log(communicationStatus);


/*
*
*########### COAP SERVER ###########################################################################################
*
*/
var coap = require('coap');


/*
*
*########### START INTERVAL ###########################################################################################
*
*/


var productionInterval = setInterval(function () {

    updateFromSensors()

    //coapDiscovery(true)

        .then(function () {
            log.debug("Sensor Update successful");
            return calculate();
        })

        .then(function () {
            log.debug("Calculation successful");
            return forwardToFrontend();
        })

        .then(function () {
            log.debug("Calculation successful");
            return updateToActors();
        })

        .catch(function (error) {
            log.debug("Production interval faild with error: " + error);
        });

}.bind(this), 5000);


if (logLevel === 'info') {

    var displayInterval = setInterval(function () {
        jetty.clear();
        displayUpdate(false, false);
    }, 1000);

}


/*
*
*
*
*/
function forwardToFrontend() {

    return new Promise(function (resolve, reject) {
        if (Frontend.status === "connected") {

            let d = new Date();

            data.timestamp = d.getFullYear() + "-" + ("0" + (d.getMonth() + 1)).slice(-2) + "-" + ("0" + d.getDate()).slice(-2) + " " + ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2);
            // YYYY-MM-DD HH:MM


            data.value.length = 0;
            data.value.push(Math.floor(Math.random() * 30) + 10);

            Frontend.client.publish(Frontend.publishTopic, JSON.stringify(data), function (error) {

                if (error) {
                    reject(error)

                } else {
                    log.debug("Data: " + JSON.stringify(data), " send to " + Frontend.url + ":" + Frontend.port + " via: " + Frontend.publishTopic);
                    resolve();
                }

            });

        } else {

            reject(Frontend.status);

        }
    });
}

/*
*
*
*
*/
function coapDiscovery(update) {

    return new Promise(function (resolve, reject) {
            //TODO Add Latest update based cleanup here
            if (!update) {
                coapNodes.length = 0;
            }

            let servicesRequest = coap.request({
                hostname: coapConfig.multicastAddress + "%" + coapConfig.interface,
                pathname: coapConfig.discoveryPfad,
                method: 'GET',
                multicast: true,
                multicastTimeout: coapConfig.discoveryTimeout
            });


            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    log.error(error);
                    reject(error);
                });

                var addNode = true;
                coapNodes.forEach(function (node) {
                    if (node.hostname === servicesResponse.rsinfo.address) {
                        log.debug("Node (" + servicesResponse.rsinfo.address + ") already in Node Array");
                        addNode = false;
                    }
                });

                if (addNode) {
                    coapNodes.push({
                        hostname: servicesResponse.rsinfo.address,
                        statusResponse: JSON.parse(servicesResponse.payload)
                    })
                }


                console.log("########################################");
                console.log("########################################");
                console.log(coapNodes);
                console.log("########################################");
                console.log("########################################");

                // resolve("ok");

            });


            setTimeout(function () {
                resolve();

            }, coapConfig.discoveryTimeout);

            servicesRequest.end();
        }
    );
}


/*
*
*
*
*/
function calculate() {

    return new Promise(function (resolve, reject) {

        //data.temperature = sensorNodeOne.surfaceTemp;
        //data.light = sensorNodeOne.lightValue;

        //sensorNodeTwo.lampValue = sensorNodeOne.lightValue;
        data.value.length = 0;
        data.value.push(sensorNodeOne.surfaceTemp);

        if (sensorNodeOne.lightValue.red.toString() < 600) {
            sensorNodeTwo.lampValue.red = 255;
            sensorNodeTwo.lampValue.green = 255;
            sensorNodeTwo.lampValue.blue = 255;
        } else {
            sensorNodeTwo.lampValue.red = 0;
            sensorNodeTwo.lampValue.green = 0;
            sensorNodeTwo.lampValue.blue = 0;
        }

        resolve();
    });
}

/*
*{"surfaceTemp": 2575, "ambientTemp": 2469, "message": ok}
*
*
*/
function updateFromSensors() {

    log.debug('Initialize COAP Request');

    var promise = coapRequest(sensorNodeOne.hostname, "/status", 'GET')
    // var promise = coapRequest(sensorNodeOne.hostname, "/temp", 'GET')
    //     var promise = coapRequest(sensorNodeTwo.hostname, "/.well-known/core", 'GET')

        .then(function (data) {
            console.log(data);
            //             // let jsonData = JSON.parse(data);
            //             // sensorNodeOne.ambientTemp = JSON.stringify(jsonData.ambientTemp).slice(0, 2) + "." + JSON.stringify(jsonData.ambientTemp).slice(2, 4);
            //             // sensorNodeOne.surfaceTemp = JSON.stringify(jsonData.surfaceTemp).slice(0, 2) + "." + JSON.stringify(jsonData.surfaceTemp).slice(2, 4);
            //             // sensorNodeOne.message = jsonData.message;
            //             //
            //             // log.debug('GET RESULT: ' + JSON.stringify(sensorNodeOne));
            //
            //             return coapRequest(sensorNodeOne.hostname, "/light/rgb", 'GET')
        })
        //         //
        //         // // .then(function (data) {
        //         // //     console.log(data);
        //         // //
        //         // //     console.log("#######-> " + data.blue);
        //         // //     console.log(JSON.stringify(data));
        //         // //
        //         // //
        //         // // })
        //         //
        //         // .then(function (data) {
        //         //
        //         //     //sensorNodeOne.lightValue = data;
        //         //
        //         //     log.debug(data);
        //         //
        //         //
        //         //     return coapRequest(sensorNodeOne.hostname, "/status", 'GET');
        //         //
        //         //
        //         // })
        //
        //         .then(function (data) {
        //             let tmp = JSON.parse(data);
        //
        //             if (tmp.message === "ok") {
        //                 sensorNodeOne.lightValue.red = tmp.red;
        //                 sensorNodeOne.lightValue.green = tmp.green;
        //                 sensorNodeOne.lightValue.blue = tmp.blue;
        //             }
        //
        //             //log.debug("GET AUF STATUS " + JSON.stringify(JSON.parse(data)));
        //             log.debug("GET auf " + sensorNodeOne.hostname + " Result: " + JSON.stringify(tmp));
        //
        //         })


        .catch(function (error) {
            log.debug("SENSOR READ failed with error: " + error);
        });

    return promise;
}

/*
*
*
*
*/

function updateToActors() {
    //
    // var promise = coapRequest(sensorNodeTwo.hostname, sensorNodeTwo.lampPaths.red, 'PUT', sensorNodeTwo.lampValue.red.toString())
    //
    //     .then(function () {
    //         return coapRequest(sensorNodeTwo.hostname, sensorNodeTwo.lampPaths.green, 'PUT', sensorNodeTwo.lampValue.green.toString())
    //     })
    //
    //     .then(function () {
    //         return coapRequest(sensorNodeTwo.hostname, sensorNodeTwo.lampPaths.blue, 'PUT', sensorNodeTwo.lampValue.blue.toString())
    //     })
    //
    //     .then(function () {
    //         return coapRequest(sensorNodeTwo.hostname, '/temp', 'PUT', sensorNodeOne.surfaceTemp)
    //     })
    //
    //     .catch(function (error) {
    //         log.debug("ACTOR PUT failed with error: " + error);
    //     });
    //
    log.debug("Actor Put succsessfull");

    return promise;
}

/*
*
*
*
*
 */


function coapRequest(hostname, path, method, payload) {

    return new Promise(function (resolve, reject) {

        var servicesRequest = coap.request({
            hostname: hostname,
            pathname: path,
            method: method,
            multicast: true,
            multicastTimeout: 2000
        });


        if (method === "PUT") {
            servicesRequest.write(payload);
            log.debug("Sensor " + method + " on: " + hostname, path + " ----> " + payload);

            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    log.error(error);
                    reject(error);
                });
                resolve();
            });

        } else if (method === "GET") {
            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    log.error(error);
                    reject(error);
                });


                //let tmp = servicesResponse.payload.toString();
                console.log("########################################");
                console.log("########################################");
                console.log(servicesResponse.rsinfo);
                console.log(servicesResponse.payload.toString());
                console.log("########################################");
                console.log("########################################");
                //log.debug("Sensor " + method + " on: " + hostname, path + " ----> " + tmp);
                resolve("ok");
                // resolve(tmp)
            });
        } else {
            reject(Error("Invalid Method"));
        }

        servicesRequest.end();
    });
}

/*
*
*
*
*/
function displayUpdate() {

    return new Promise(function (resolve, reject) {

        /*
                var date = new Date();

                log.info("######################################## AQUARIUM GATEWAY ########################################");
                log.info("###   Node Side   ########   " + colors.warn(date) + "   #######   Server Side   ###");
                log.info("###########################################            ###########################################");
                log.info("### Number Nodes      Number Ressources ###            ###########################################");
                log.info("###      " + colors.data(communicationStatus.numberNodes) + "        #         " + colors.data(communicationStatus.numberRessources) + "         #####            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###             Requests                ###            ###########################################");
                log.info("### Succsessful               Failed    ###            ###########################################");
                log.info("###                                     ###            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("###########################################            ###########################################");
                log.info("##################################################################################################");
        */

        resolve();

    });
}


function CoapSensor() {

}



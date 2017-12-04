
//############ Run Parameters ##########################################################################################

var argv = require('minimist')(process.argv.slice(2));

if (argv.help) {

    console.log("Parameter description:");
    console.log("");
    console.log("--mqtt                 Something like mqtt://141.22.28.86 ");
    console.log("");
    console.log("--logLevel             debug , info ");
    console.log("");
    console.log("--mqtt                 Something like mqtt://141.22.28.86 ");

    process.exit(0);
}


//var logLevel = 'debug';

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
var Frontend = {
    url: 'mqtt://141.22.28.86',
    port: '1883',
    subscribeTopic: 'actor',
    publishTopic: 'test',
    status: 'disconnected',
    connect: function () {
        Frontend.client = mqtt.connect(Frontend.url, {
            port: Frontend.port
        });
    }
};

var sensorNodes = [{
    hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
    supportedPaths: [
        {
            path: "/temp",
            typ: "GET",

        }, {
            path: "/light",
            typ: "GET",

        }],
}, {
    hostname: "fe80::7b62:1b6d:89cc:89ca%lowpan0",
    supportedPaths: [
        {
            path: "/temp",
            typ: "GET",

        }, "/light"],
}];


var sensorNodeOne = {
    //hostname: "fe80::68c0:6d50:52ae:432a%lowpan0",
    hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
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
    hostname: "fe80::7b62:1b6d:89cc:89ca%lowpan0",
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
    node_1: {
        temperature: "0",
        light: {
            red: "0",
            green: "0",
            blue: "0"
        },
        ph: "0"
    },
    node_2: {
        lampStatus: "0"
    }
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

jetty.clear();

Frontend.connect();

Frontend.client.on('connect', function () {
    Frontend.status = 'connected';
    Frontend.client.subscribe(Frontend.subscribeTopic);
    //log.debug(colors.info("Frontend connected and subscribed to: " + Frontend.subscribeTopic));
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


console.log(data);
console.log(communicationStatus);


/*
*
*########### START INTERVAL ###########################################################################################
*
*/

var productionInterval = setInterval(function () {

    updateFromSensors()

        .then(function () {
            log.debug(colors.info("Sensor Update successful"));

            return calculate();
        })

        .then(function () {
            log.debug(colors.info("Calculation successful"));

            return forwardToFrontend();
        })

        .catch(function (error) {
            log.debug(colors.error("Production interval faild with error: " + error));
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
function coapDiscovery() {

    return new Promise(function (resolve, reject) {


        reject();
        resolve();
    });
}


/*
*
*
*
*/
function calculate() {

    return new Promise(function (resolve, reject) {

        data.temperature = sensorNodeOne.surfaceTemp;
        data.light = sensorNodeOne.lightValue;


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

    // .then(function (data) {
    //     let jsonData = JSON.parse(data);
    //     sensorNodeOne.ambientTemp = JSON.stringify(jsonData.ambientTemp).slice(0, 2) + "." + JSON.stringify(jsonData.ambientTemp).slice(2, 4);
    //     sensorNodeOne.surfaceTemp = JSON.stringify(jsonData.surfaceTemp).slice(0, 2) + "." + JSON.stringify(jsonData.surfaceTemp).slice(2, 4);
    //     sensorNodeOne.message = jsonData.message;
    //
    //     log.debug('GET RESULT: ' + JSON.stringify(sensorNodeOne));
    //
    //     return coapRequest(sensorNodeOne.hostname, sensorNodeOne.lightPath, 'GET');
    // })
    //
    // // .then(function (data) {
    // //     console.log(data);
    // //
    // //     console.log("#######-> " + data.blue);
    // //     console.log(JSON.stringify(data));
    // //
    // //
    // // })
    //
    // .then(function (data) {
    //
    //     //sensorNodeOne.lightValue = data;
    //
    //     log.debug(data);
    //
    //
    //     return coapRequest(sensorNodeOne.hostname, "/status", 'GET');
    //
    //
    // })

        .then(function (data) {


            console.log("test");

            log.debug("GET AUF STATUS " + JSON.stringify(JSON.parse(data)));


        })


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
function coapRequest(hostname, path, method, payload) {

    return new Promise(function (resolve, reject) {

        var coap = require('coap');

        var servicesRequest = coap.request({
            hostname: hostname,
            pathname: path,
            method: method,
            multicast: true,
            multicastTimeout: 1000
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

                let tmp = servicesResponse.payload.toString();
                //log.debug("Sensor " + method + " on: " + hostname, path + " ----> " + tmp);
                resolve(tmp);
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

        resolve();

    });
}


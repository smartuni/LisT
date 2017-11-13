//############ LOG LEVEL ##########
var logLevel = 'debug';
//############ LOG LEVEL ##########

var colors = require('colors');
var q = require('q');
var mqtt = require('mqtt');
var log = require('console-log-level')({level: logLevel});
var Jetty = require("jetty");

colors.setTheme({
    silly: 'rainbow',
    input: 'grey',
    verbose: 'blue',
    prompt: 'grey',
    info: 'green',
    data: 'cyan',
    help: 'cyan',
    warn: 'yellow',
    debug: 'grey',
    error: 'red'
});


/*
*
* #### Connection Config ####
*
*/
var Frontend = {
    url: 'mqtt://141.22.28.86',
    port: '1883',
    subscribeTopic: 'set_target_values',
    publishTopic: 'test',
    status: 'disconnected',
    connect: function () {
        Frontend.client = mqtt.connect(Frontend.url, {
            port: Frontend.port
        });
    }
};

var sensorNodeOne = {
    //hostname: "fe80::68c0:6d50:52ae:432a%lowpan0",
    hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
    temperaturePath: "/temp",
    temperatureValue: "0",
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

/*
*
*
*
*/
var jetty = new Jetty(process.stdout);


Frontend.connect();


Frontend.client.on('connect', function () {
    Frontend.status = 'connected';
    Frontend.client.subscribe(Frontend.subscribeTopic);
    log.info(colors.info("Frontend connected and subscribed to: " + Frontend.subscribeTopic));
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
    // Do some sort of thing here.

    console.log(message.toString());


});


var productionInterval = setInterval(function () {
    //jetty.clear();

    updateFromSensors()
        .then(function () {
            log.info(colors.info("Sensor Update successful"));
            return calculate();
        })
        .then(function () {
            log.info(colors.info("calculate successful"));
            return forwardToFrontend();
        })
        .catch(function (error) {
            log.info(colors.error("INTERVAL faild with error: " + error));
        });


}.bind(this), 2000);

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
                    resolve()
                }
            });
        } else {
            reject(Frontend.status)
        }

    });
}

/*
*
*
*
*/
function calculate() {

    return new Promise(function (resolve, reject) {
        data.temperature = sensorNodeOne.temperatureValue;
        data.light = sensorNodeOne.lightValue;


            if (sensorNodeOne.lightValue.red.toString()< 600) {
                sensorNodeTwo.lampValue.red = 255;
                sensorNodeTwo.lampValue.green = 255;
                sensorNodeTwo.lampValue.blue = 255;
            } else {
                sensorNodeTwo.lampValue.red = 0;
                sensorNodeTwo.lampValue.green = 0;
                sensorNodeTwo.lampValue.blue = 0;
            }



        log.info("calculate");
        resolve();

        //reject();

    });
}

/*
*
*
*
*/
function updateFromSensors() {

    log.info('Initialize COAP Request');

    var promise = coapRequest(sensorNodeOne.hostname, sensorNodeOne.temperaturePath, 'GET')

        .then(function (value) {
            sensorNodeOne.temperatureValue = value.slice(0, 2) + "." + value.slice(2, 4);
            log.debug('GET on ' + sensorNodeOne.hostname + ' ' + sensorNodeOne.temperaturePath + ' ---> ' + colors.data(sensorNodeOne.temperatureValue));

            return coapRequest(sensorNodeOne.hostname, sensorNodeOne.lightPath, 'GET');
        })

        .then(function (value) {
            sensorNodeOne.lightValue = value;
            log.debug('GET on ' + sensorNodeOne.hostname + ' ' + sensorNodeOne.lightPath + ' ---> ' + colors.data(sensorNodeOne.lightValue));

            return coapRequest(sensorNodeTwo.hostname, sensorNodeTwo.lampPaths.red, 'PUT', sensorNodeTwo.lampValue.red);
        })
        .then(function (value) {
            sensorNodeOne.lightValue = value;
            log.debug('GET on ' + sensorNodeOne.hostname + ' ' + sensorNodeOne.lightPath + ' ---> ' + colors.data(sensorNodeOne.lightValue));

            return coapRequest(sensorNodeTwo.hostname, sensorNodeTwo.lampPaths.green, 'PUT', sensorNodeTwo.lampValue.green);
        })
        .then(function (value) {
            sensorNodeOne.lightValue = value;
            log.debug('GET on ' + sensorNodeOne.hostname + ' ' + sensorNodeOne.lightPath + ' ---> ' + colors.data(sensorNodeOne.lightValue));

            return coapRequest(sensorNodeTwo.hostname, sensorNodeTwo.lampPaths.blue, 'PUT', sensorNodeTwo.lampValue.blue);
        })


        .then(function (value) {
            log.debug('Request 4 complete with return: ' + colors.data(value));

            return coapRequest(sensorNodeOne.hostname, sensorNodeOne.writePath, 'GET');
        })

        .then(function (value) {
            log.debug('Request 5 complete with return: ' + colors.data(value));

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


        if (method == "PUT") {
            servicesRequest.write(payload);
            log.debug("Sensor " + method + " on: " + hostname, path + " ----> " + payload);


            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    log.error(error);
                    reject(error);
                });

                resolve();

            });

            //resolve();

        } else if (method == "GET") {

            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    log.error(error);
                    reject(error);
                });

                let tmp = servicesResponse.payload.toString();
                log.debug("Sensor " + method + " on: " + hostname, path + " ----> " + tmp);

                resolve(tmp);
            });

        } else {
            reject(Error("Invalid Method"));
        }

        servicesRequest.end();
    });
}


//MQTT CLIENT ############################################################
/*
Frontend.client.publish('iot_data', JSON.stringify(yourobject));
Frontend.client.publish('iot_data', 'Dies das jenes');

Frontend.connect();

Frontend.client.on('connect', function () {
    Frontend.status = 'connected';
    Frontend.client.subscribe(Frontend.subscribeTopic);
    console.log("Frontend connected and subscribed to: " + Frontend.subscribeTopic);
});

Frontend.client.on('error', function (error) {
    console.log('MQTT Client Errored');
    console.log(error);
});

Frontend.client.on('message', function (topic, message) {
    // Do some sort of thing here.

    console.log(message.toString());


});


//SERVER ############################################################
/*var mosca = require('mosca');

var settings = {
    port: 1883
    //backend: ascoltatore
};


var message = {
    topic: 'iot_data',
    payload: 'Dies das jenes', // or a Buffer
    qos: 0, // 0, 1, or 2
    retain: false // or true
};


var mqttServer = new mosca.Server(settings);


mqttServer.on('clientConnected', function (client) {
    console.log('client connected', client.id);
});


// fired when a message is received
mqttServer.on('published', function (packet, client) {
    console.log('Published', packet.payload);
});

mqttServer.on('ready', setup);

// fired when the mqtt server is ready
function setup() {
    console.log('Mosca server is up and running');

    setInterval(function () {
        mqttServer.publish(message, function () {
            console.log('message send!');
        });
    },3000);


}

*/
var q = require('q');
var mqtt = require('mqtt');

var Frontend = {
    url: 'mqtt://141.22.28.86',
    port: '1883',
    subscribeTopic: 'set_target_values',
    publishTopic: 'iot_data',
    status: 'disconnected',
    connect: function () {
        Frontend.client = mqtt.connect(Frontend.url, {
            port: Frontend.port
        });
    }
};



var sensorNodeOne = {
    hostname: "fe80::68c0:6d50:52ae:432a%lowpan0",
    temperaturePath: "/temp",
    temperatureValue: "nothing",
    lightPath: "/light",
    writePath: "/cli/stats"
};

/*
*
*
*
*/
var productionInterval = setInterval(function () {

    updateFromSensors()
        .then(function () {
            console.log("Sensor Update successful");
            return calculate();
        })
        .then(function () {
            console.log("calculate successful");
            return forwardToFrontend();
        })
        .catch(function (error) {
            console.log("INTERVAL faild with error: " + error);
        });


}.bind(this), 2000);

/*
*
*
*
*/
function forwardToFrontend() {

    return new Promise(function (resolve, reject) {

        console.log("frontend");
        resolve();

        //reject();

    });
}

/*
*
*
*
*/
function calculate() {

    return new Promise(function (resolve, reject) {

        console.log("calculate");
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

    console.log('Reading from Sensors');

    var promise = coapRequest(sensorNodeOne.hostname, sensorNodeOne.temperaturePath, 'GET')
        .then(function (value) {
            console.log('Request 1 complete with return: ' + value);
            return coapRequest(sensorNodeOne.hostname, sensorNodeOne.lightPath, 'GET');
        })
        .then(function (value) {
            console.log('Request 2 complete with return: ' + value);
            return coapRequest(sensorNodeOne.hostname, sensorNodeOne.writePath, 'PUT', '2');
        })
        .then(function (value) {
            console.log('Request 3 complete with return: ' + value);
            return coapRequest(sensorNodeOne.hostname, sensorNodeOne.writePath, 'GET');
        })
        .catch(function (error) {
            console.log("SENSOR READ failed with error: " + error);
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
            servicesRequest.write(JSON.stringify(payload));
            console.log("Sensor " + method + " on: " + hostname, path + " ----> " + payload);

            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    console.log(error);
                    reject(error);
                });

                let tmp = servicesResponse.payload.toString();
                console.log("Sensor " + method + " Response on: " + hostname, path + " ----> " + tmp);
                resolve();

            });

        } else if (method == "GET") {

            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    console.log(error);
                    reject(error);
                });

                let tmp = servicesResponse.payload.toString();
                console.log("Sensor " + method + " on: " + hostname, path + " ----> " + tmp);

                resolve();
            });

        } else {
            reject(Error("Invalid Method"));
        }

        servicesRequest.end();
    });
}


//CLIENT ############################################################

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
Frontend.client.publish('iot_data', 'Dies das jenes')

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
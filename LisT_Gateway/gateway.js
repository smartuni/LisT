/*
var SmartObject = require('smartobject');

// initialize Resources that follow IPSO definition
var so = new SmartObject();

// initialize your Resources
// oid = 'temperature', iid = 0
so.init('temperature', 0, {
    sensorValue: 21,
    units: 'C'
});

// oid = 'lightCtrl', iid = 0
so.init('lightCtrl', 0, {
    onOff: false
});


var CoapNode = require('coap-node');

// Instantiate a machine node with a client name and your smart object
var cnode = new CoapNode('my_first_node', so);

cnode.on('registered', function () {

    // If the registration procedure completes successfully, 'registered' will be fired
    console.log("registered!!!")
});

// register to a Server with its ip and port
cnode.register('fe80::68c0:6d50:52ae:432a', 5683, function (err, rsp) {
    console.log("register log: " + rsp);      // { status: '2.05' }
});

*/

//var coap = require('coap')
//    , server = coap.createServer({type: 'udp6'});


//server.on('request', function (req, res) {
//    res.end('Hello ' + req.url.split('/')[1] + '\n')
//});

var q = require('q');

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

        resolve();

        //reject();

        console.log("frontend")
    });
}

/*
*
*
*
*/
function calculate() {

    return new Promise(function (resolve, reject) {


        resolve();


        //reject();

        console.log("calculate")
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
function coapRequest(hostname, path, method, clientToServerData) {

    return new Promise(function (resolve, reject) {

        let coap = require('coap');

        let servicesRequest = coap.request({
            hostname: hostname,
            pathname: path,
            method: method,
            multicast: true,
            multicastTimeout: 1000
        });


        if (method == "PUT") {
            servicesRequest.write(JSON.stringify(clientToServerData));
            console.log("Sensor " + method + " on: " + hostname, path + " ----> " + clientToServerData);

            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    console.log(error);
                    reject();
                });

                let tmp = servicesResponse.payload.toString();
                console.log("Sensor " + method + " Response on: " + hostname, path + " ----> " + tmp);
                resolve();

            });

        } else if (method == "GET") {

            servicesRequest.on('response', function (servicesResponse) {
                servicesResponse.on('error', function (error) {
                    console.log(error);
                    reject();
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
/*
var mqtt = require('mqtt'),
/*
    my_topic_name = 'iot_data';

var client = mqtt.connect('mqtt://141.22.28.86', {
    port: 1883
    //username: 'riotadmin',
    //password: '2whiteRUSSIAN4me'
});

client.on('connect', function () {
    client.subscribe(my_topic_name);
    console.log("i am connected");
});

client.on('error', function (error) {
    console.log('MQTT Client Errored');
    console.log(error);
});

client.on('message', function (topic, message) {
    // Do some sort of thing here.

    console.log(message.toString());

    client.publish('iot_data', 'Dies das jenes')
});
*/


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
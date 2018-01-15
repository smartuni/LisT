//############ Run Parameters ##########################################################################################
let argv = require('minimist')(process.argv.slice(2));

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


/*
*
* #### Connection Config ###############################################################################################
*
*/
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

//     hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
//     hostname: "fe80::7b62:1b6d:89cc:89ca%lowpan0",


var sensorNodeNew = [{
    hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
    paths: [{
        name: "/temp",
        typ: "GET",
        paths: [{
            name: "/ambient",
            typ: "GET",
            value: "",
            message: ""
        }, {
            name: "/surface",
            typ: "GET",
            value: "",
            message: ""
        }]

    }, {
        name: "/light",
        typ: "GET",
        paths: [{
            name: "/red",
            typ: "GET",
            value: "",
            message: ""
        }, {
            name: "/green",
            typ: "GET",
            value: "",
            message: ""
        }, {
            name: "/blue",
            typ: "GET",
            value: "",
            message: ""
        }]

    }, {
        name: "/ph",
        typ: "GET",
        paths: [{
            name: "/value",
            typ: "GET",
            value: "",
            message: ""
        }]

    }, {

        //TODO create this dynamically


    }],
    //hostname: "ff02::1",
}];


var sensorNode = [{
    hostname: "fe80::abc0:6e7a:7427:322%lowpan0",
    paths: [{
        path: "/temp/ambient",
        typ: "GET",
        value: "",
        message: ""
    }, {
        path: "/temp/surface",
        typ: "GET",
        value: "",
        message: ""
    }, {
        path: "/light/red",
        typ: "GET",
        value: "",
        message: ""
    }, {
        path: "/light/green",
        typ: "GET",
        value: "",
        message: ""
    }, {
        path: "/light/blue",
        typ: "GET",
        value: "",
        message: ""
    }, {
        path: "/ph/value",
        typ: "GET",
        value: "",
        message: ""
    }],
    //hostname: "ff02::1",
}];


var actorNode = [{
    hostname: "fe80::7b79:4946:539e:75e%lowpan0",
    paths: [{
        path: "/led/red",
        typ: "PUT",
        value: "200",
        message: ""
    }, {
        path: "/led/green",
        typ: "PUT",
        value: "",
        message: ""
    }, {
        path: "/led/blue",
        typ: "PUT",
        value: "",
        message: ""
    }, {
        path: "/ledbar/temp",
        typ: "PUT",
        value: "",
        message: ""
    }, {
        path: "/heat/switch",
        typ: "PUT",
        value: "",
        message: ""
    }],
    //hostname: "ff02::1%lowpan0",
}];


var frontendData = [{
    sensor: 's1',
    typ: 'temp',
    value: [],
    timestamp: ''
}, {
    sensor: 's2',
    typ: 'temp',
    value: [],
    timestamp: '',

}, {
    sensor: 's3',
    typ: 'light',
    value: [],
    timestamp: '',
}, {
    sensor: 's4',
    typ: 'ph',
    value: [],
    timestamp: '',
}];

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

console.log(communicationStatus);


/*
*
*########### COAP SERVER ###########################################################################################
*
*/
var coap = require('coap');


// var server = coap.createServer({
//     type: 'udp6',
//     //multicast: true,
//     multicastAddress: 'ff02::1'
// });
//

// // Create servers
// server.listen(5683, function () {
//     console.log('Server 1 is listening')
// });
//

// server.on('request', function (msg, res) {
//     console.log('Server 1 has received message' + msg);
//     res.end('Ok');
//
//     //server.close();
// });


/*
*
*########### START INTERVAL ###########################################################################################
*
*/


var productionInterval = setInterval(function () {

    // coapRequest("ff02::1%lowpan0", "/heat", 'GET', 0, function (result) {
    //
    //     console.log(result);
    // })


    updateFromSensors()

        .then(function () {
            log.debug("Sensor update successful");
            return calculate();
        })

        .then(function () {
            log.debug("Calculation successful");
            return updateToActors();
        })

        .then(function () {
            log.debug("Actor update successful");
            return updateFrontend();
        })

        .catch(function (error) {
            log.debug("Production interval faild with error: " + error);
        });

}.bind(this), 10000);


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

//TODO ADJUST TO LATEST BRANCH
function updateFrontend() {

    return new Promise(function (resolve, reject) {
        if (Frontend.status === "connected") {

            let d = new Date();
            frontendData.timestamp = d.getFullYear() + "-" + ("0" + (d.getMonth() + 1)).slice(-2) + "-" + ("0" + d.getDate()).slice(-2) + " " + ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2);

            var interval = 2000; // how much time should the delay between two iterations be (in milliseconds)?
            var innerPromise = Promise.resolve();

            frontendData.forEach(function (data,) {
                innerPromise = innerPromise.then(function () {
                    Frontend.client.publish(Frontend.publishTopic, JSON.stringify(data), function (error) {
                        if (error) {
                            reject(error)
                        } else {
                            log.debug("Data: " + JSON.stringify(data), " send to " + Frontend.url + ":" + Frontend.port + " via: " + Frontend.publishTopic);
                            resolve();
                        }
                    });

                    return new Promise(function (resolve) {
                        setTimeout(resolve, interval);
                    });
                });
            });


            // innerPromise.then(function () {
            //     resolve();
            // });


            // frontendData.forEach(function (frontendData,index) {
            //


            // Frontend.client.publish(Frontend.publishTopic, JSON.stringify(frontendData[0]), function (error) {
            //
            //     if (error) {
            //         reject(error)
            //
            //     } else {
            //         log.debug("Data: " + JSON.stringify(frontendData[0]), " send to " + Frontend.url + ":" + Frontend.port + " via: " + Frontend.publishTopic);
            //         resolve();
            //     }
            //
            // });
            // });


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

        //Scan for Nodes on Broadcast
        //scan recursiv for Paths and create node objects


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

        actorNode[0].paths[0].value = sensorNode[0].paths[2].value / 2;
        actorNode[0].paths[1].value = sensorNode[0].paths[3].value / 2;
        actorNode[0].paths[2].value = sensorNode[0].paths[4].value / 2;
        actorNode[0].paths[3].value = sensorNode[0].paths[0].value;

        if (sensorNode[0].paths[0].value < 26){
            actorNode[0].paths[4].value = 1;
        }else{
            actorNode[0].paths[4].value = 0;
        }

        //PREPARE FOR FRONTEND
        let d = new Date();
        let timestamp = d.getFullYear() + "-" + ("0" + (d.getMonth() + 1)).slice(-2) + "-" + ("0" + d.getDate()).slice(-2) + " " + ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2);

        //s1
        frontendData[0].value.length = 0;
        frontendData[0].value.push(sensorNode[0].paths[0].value);
        frontendData[0].timestamp = timestamp;

        //s2
        frontendData[1].value.length = 0;
        frontendData[1].value.push(sensorNode[0].paths[1].value);
        frontendData[1].timestamp = timestamp;

        //s3
        frontendData[2].value.length = 0;
        frontendData[2].value.push(sensorNode[0].paths[2].value);
        frontendData[2].value.push(sensorNode[0].paths[3].value);
        frontendData[2].value.push(sensorNode[0].paths[4].value);
        frontendData[2].timestamp = timestamp;

        //s3
        frontendData[3].value.length = 0;
        frontendData[3].value.push(sensorNode[0].paths[5].value);
        frontendData[3].timestamp = timestamp;

        resolve();
    });
}

/*
*{"surfaceTemp": 2575, "ambientTemp": 2469, "message": ok}
*
*
*/
function updateFromSensors() {


    log.debug('Update from sensors...');

    // //TODO Discovery
    // var promise = sensor.Node.forEach()coapRequest(sensor.hostname, sensor.path, sensor.typ).then(function(){})
    //
    //
    //  sensorNode.forEach(function (sensor) {
    //      console.log(sensor);
    //      var promise = coapRequest(sensor.hostname, sensor.path, sensor.typ).then(function(){})
    //  });

    //var promise = coapRequest(actorNode.hostname, "/.well-known/core", 'GET')

    var promise = coapRequest(sensorNode[0].hostname, sensorNode[0].paths[0].path, sensorNode[0].paths[0].typ)

        .then(function (data) {
            sensorNode[0].paths[0].value = JSON.stringify(data.ambientTemp).slice(0, 2) + "." + JSON.stringify(data.ambientTemp).slice(2, 4);
            sensorNode[0].paths[0].message = data.message;

            log.debug('COAP Response from ' + sensorNode[0].hostname + ' on path ' + sensorNode[0].paths[0].path + ' value: ' + JSON.stringify(sensorNode[0].paths[0].value) + ' message: ' + sensorNode[0].paths[0].message);

            return coapRequest(sensorNode[0].hostname, sensorNode[0].paths[1].path, sensorNode[0].paths[1].typ);
        })

        .then(function (data) {
            sensorNode[0].paths[1].value = JSON.stringify(data.surfaceTemp).slice(0, 2) + "." + JSON.stringify(data.surfaceTemp).slice(2, 4);
            sensorNode[0].paths[1].message = data.message;

            log.debug('COAP Response from ' + sensorNode[0].hostname + ' on path ' + sensorNode[0].paths[1].path + ' value: ' + JSON.stringify(sensorNode[0].paths[1].value) + ' message: ' + sensorNode[0].paths[1].message);

            return coapRequest(sensorNode[0].hostname, sensorNode[0].paths[2].path, sensorNode[0].paths[2].typ);
        })

        .then(function (data) {
            sensorNode[0].paths[2].value = data.red;
            sensorNode[0].paths[2].message = data.message;

            log.debug('COAP Response from ' + sensorNode[0].hostname + ' on path ' + sensorNode[0].paths[2].path + ' value: ' + JSON.stringify(sensorNode[0].paths[2].value) + ' message: ' + sensorNode[0].paths[2].message);

            return coapRequest(sensorNode[0].hostname, sensorNode[0].paths[3].path, sensorNode[0].paths[3].typ);
        })

        .then(function (data) {
            sensorNode[0].paths[3].value = data.green;
            sensorNode[0].paths[3].message = data.message;

            log.debug('COAP Response from ' + sensorNode[0].hostname + ' on path ' + sensorNode[0].paths[3].path + ' value: ' + JSON.stringify(sensorNode[0].paths[3].value) + ' message: ' + sensorNode[0].paths[3].message);

            return coapRequest(sensorNode[0].hostname, sensorNode[0].paths[4].path, sensorNode[0].paths[4].typ);
        })

        .then(function (data) {
            sensorNode[0].paths[4].value = data.blue;
            sensorNode[0].paths[4].message = data.message;

            log.debug('COAP Response from ' + sensorNode[0].hostname + ' on path ' + sensorNode[0].paths[4].path + ' value: ' + JSON.stringify(sensorNode[0].paths[4].value) + ' message: ' + sensorNode[0].paths[4].message);

             // return (1);
             return coapRequest(sensorNode[0].hostname, sensorNode[0].paths[5].path, sensorNode[0].paths[5].typ);
        })

        .then(function (data) {
            sensorNode[0].paths[5].value = data.value;
            sensorNode[0].paths[5].message = data.message;

            log.debug('COAP Response from ' + sensorNode[0].hostname + ' on path ' + sensorNode[0].paths[5].path + ' value: ' + JSON.stringify(sensorNode[0].paths[5].value) + ' message: ' + sensorNode[0].paths[5].message);

            return (1);
        })


        // .then(function (frontendData) {
        //     let tmp = JSON.parse(frontendData);
        //
        //     if (tmp.message === "ok") {
        //         sensorNode.lightValue.red = tmp.red;
        //         sensorNode.lightValue.green = tmp.green;
        //         sensorNode.lightValue.blue = tmp.blue;
        //     }
        //
        //     //log.debug("GET AUF STATUS " + JSON.stringify(JSON.parse(frontendData)));
        //     log.debug("GET auf " + sensorNode.hostname + " Result: " + JSON.stringify(tmp));
        //
        // })


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


    var promise = coapRequest(actorNode[0].hostname, actorNode[0].paths[0].path, actorNode[0].paths[0].typ, actorNode[0].paths[0].value.toString())

        .then(function () {
            return coapRequest(actorNode[0].hostname, actorNode[0].paths[1].path, actorNode[0].paths[1].typ, actorNode[0].paths[1].value.toString())
        })

        .then(function () {
            return coapRequest(actorNode[0].hostname, actorNode[0].paths[2].path, actorNode[0].paths[2].typ, actorNode[0].paths[2].value.toString())
        })

        .then(function () {
            return coapRequest(actorNode[0].hostname, actorNode[0].paths[3].path, actorNode[0].paths[3].typ, actorNode[0].paths[3].value.toString())
        })

        .then(function () {
            return coapRequest(actorNode[0].hostname, actorNode[0].paths[4].path, actorNode[0].paths[4].typ, actorNode[0].paths[4].value.toString())
        })

        .then(function () {
            return promise;
        })

        .catch(function (error) {
            log.debug("ACTOR PUT failed with error: " + error);
        });

    log.debug("Actor Put succsessfull");


}

/*
*
*
*
*
 */
function coapRequest(hostname, path, method, payload) {

    return new Promise(function (resolve, reject) {

        //log.debug('Initialize COAP Request');

        var servicesRequest = coap.request({
            hostname: hostname,
            pathname: path,
            method: method,
            multicast: true,
            multicastTimeout: 1000
        });


        if (method === "PUT") {
            servicesRequest.write(payload);
            log.debug(method + " on: " + hostname, path + " ----> " + payload);

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
                //console.log(servicesResponse.payload.toString());
                resolve(JSON.parse(tmp));
                //console.log(tmp);

                //console.log(servicesResponse.rsinfo);
                resolve(tmp);
            });

        } else {
            reject(Error("Invalid coapRequest Method"));
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
        log.info("Ambient Temperature: " + sensorNode[0].paths[0].value);
        log.info("Surface Temperature: " + sensorNode[0].paths[1].value);
        log.info("Light Red:           " + sensorNode[0].paths[2].value + " Green: " + sensorNode[0].paths[3].value + " Blue: " + sensorNode[0].paths[4].value);
        log.info("PH Sensor:           " + sensorNode[0].paths[5].value);

        resolve();

    });
}


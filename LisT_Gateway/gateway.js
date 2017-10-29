var coap = require('coap'), server = coap.createServer();

var mqtt = require('mqtt'),
    my_topic_name = 'your-adafruit-username/f/feed-name';


server.on('request', function (req, res) {
    res.end('Hello ' + req.url.split('/')[1] + '\n')
});

// the default CoAP port is 5683
server.listen(function () {
    var req = coap.request('coap://localhost/Matteo');

    req.on('response', function (res) {
        res.pipe(process.stdout);
        res.on('end', function () {
            process.exit(0)
        })
    });

    req.end()
});


var client = mqtt.connect('mqtts://test.mosquitto.org', {
    port: 8883,
    //username: 'username',
    //password: 'apikey'
});

client.on('connect', function () {
    client.subscribe(my_topic_name)
});

client.on('error', function (error) {
    console.log('MQTT Client Errored');
    console.log(error);
});

client.on('message', function (topic, message) {
    // Do some sort of thing here.

    console.log(message.toString());
});

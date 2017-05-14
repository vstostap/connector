var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var kafka = require('kafka-node');
var HighLevelConsumer = kafka.HighLevelConsumer;
var Offset = kafka.Offset;
var Client = kafka.Client;
var topic = 'order-one-min-data';
var config = './config';
var client = new Client(config.kafka.client, config.kafka.worker );
var payloads = [{ topic: topic }];
var consumer = new HighLevelConsumer(client, payloads);
var offset = new Offset(client);

app.get('/', function(req, res){
    res.sendfile('index.html');
});

io = io.on('connection', function(socket){
    console.log('a user connected');
    socket.on('disconnect', function(){
        console.log('user disconnected');
    });
});

consumer = consumer.on('message', function(message) {
    console.log(message.value);
    io.emit("message", message.value);
});

http.listen(config.http.port, function(){
    console.log("Node app running on port " + config.http.port)
});
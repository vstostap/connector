var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var kafka = require('kafka-node');
var config = require('./config');
var HighLevelConsumer = kafka.HighLevelConsumer;
var Offset = kafka.Offset;
var Client = kafka.Client;
var client = new Client(config.kafka.client, config.kafka.worker );
var payloads = [{ topic: config.kafka.topic }];
var consumer = new HighLevelConsumer(client, payloads);
var offset = new Offset(client);
var path = require('path');

app.use('/dist', express.static(__dirname + '/../'));

app.get('/', function(req, res){
    res.sendFile(path.resolve(__dirname + '/../index.html'));
});
/*
io = io.on('connection', function(socket){
    console.log('a socket connected');
    socket.on('disconnect', function(){
        console.log('a socket disconnected');
    });
});

consumer = consumer.on('message', function(message) {
    console.log((message.key).toString('utf8'));
    console.log(message.value);
    //io.emit("message", message);
});

process.on('SIGINT', function() {
    consumer.close(true, function(){
        process.exit();
    })
}); */

http.listen(config.http.port, function(){
    console.log("Node app running on port " + config.http.port)
});
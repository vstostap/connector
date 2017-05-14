'use strict';

var path = require('path'),
    rootPath = path.normalize(__dirname + '/../..'),
    _ = require('lodash');


// Set default node environment to development
process.env.NODE_ENV = process.env.NODE_ENV || 'development';

var index = {
    env: process.env.NODE_ENV,
    root: rootPath,
    bodyParser: {
        json: {limit: '100kb'},
        urlencoded: {limit: '100kb', extended: true}
    },
    app: {
        name: 'connector'
    },
    http: {
        // Server port
        port: process.env.PORT || 3000
    },
    kafka: {
        client: 'localhost:'+2181,
        worker: "worker-" + Math.floor(Math.random() * 10000)
    }
};

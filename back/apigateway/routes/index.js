const express = require("express")
const axios = require('axios');
const { services } = require('./registry.json');
const router = express.Router()
const config = require("../config")

/**
 * Filter and redirect the routes to the microservices
 */
router.all('*', (req, res) => {
    const [ , , , service, ...pathParts] = req.originalUrl.split('/');
    const path = pathParts.join('/');
    const serviceConfig = services[service];

    if (!serviceConfig) {
        return res.status(404).send({ message: 'Service not found' });
    } 

    axios({
        baseUrl: config.host,
        method: req.method,
        url: `${serviceConfig.url_local}:${serviceConfig.port}/${path}`,
        //Validate how to pass headers
        data: req.body
    })
    .then(response => {
        res.send(response.data);
    })
    .catch(error => {
        console.log("Error: " + error);
        if (error.response) {
        res.status(error.response.status).send(error.response.data);
        } else if (error.request) {
        res.status(500).send({ message: 'No response received from the service' });
        } else {
        res.status(500).send({ message: 'Internal Server Error' });
        }
    });
});

module.exports = router

const express = require("express")
const axios = require('axios');
const { services } = require('./registry.json');
const router = express.Router()
const config = require("../config")
const path = require("path")
const fs = require("fs")
const FormData = require('form-data');

const multer = require("multer");

const uploadDir = path.join('uploads');
if (!fs.existsSync(uploadDir)) {
    fs.mkdirSync(uploadDir);
}
// Configuración de multer
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, uploadDir);
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    },
});

const upload = multer({storage}); // Multer para manejar los archivos en la solicitud de entrada
/**
 * Filter and redirect the routes to the microservices
 */
router.all('*', upload.single("image"), (req, res) => {
    const [ , , , service, ...pathParts] = req.originalUrl.split('/');
    const path = pathParts.join('/');
    const serviceConfig = services[service];

    if (!serviceConfig) {
        return res.status(404).send({ message: 'Service not found' });
    } 

    // Crear un nuevo FormData para reenviar el archivo al microservicio
    const form = new FormData();

    // Si hay un archivo en la solicitud, agregarlo al FormData
    if (req.file) {
        form.append("image", fs.createReadStream(req.file.path), req.file.originalname); // Usando fs.createReadStream para enviar el archivo
    }


    // Agregar otros datos del body, si existen
    if (req.body) {
        console.log("Body correcto");
        Object.keys(req.body).forEach((key) => {
            console.log("Key: " + key, "input: " + req.body[key]);
            form.append(key, req.body[key]);
            console.log("Body on ag: ", req.body);
        });
    }

    axios({
        baseUrl: config.host,
        method: req.method,
        url: `${serviceConfig.host}:${serviceConfig.port}/${path}`,
        //Validate how to pass headers
        data: form, // Pasamos el FormData con los archivos y otros datos
    })
    .then(response => {
        console.log("Response: "+ response.data);
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

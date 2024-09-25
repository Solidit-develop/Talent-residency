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
        console.log("Body correcto on AG");
        console.log(req.body);
        Object.keys(req.body).forEach((key) => {
            const value = req.body[key];
            // Verificar si el valor es un objeto o un array
            // Si el valor es un objeto, lo convertimos a JSON string
            if (typeof value === 'object' && value !== null) {
                valueToAdd = JSON.stringify(value)
                console.log("En if con key: " + key + " y value: " + valueToAdd);
                form.append(key, valueToAdd);
            } else {
                console.log("En else con key: " + key + " y value: " + value);
                form.append(key, value);
            }
        });
    }

    console.log("Form data values: " + JSON.stringify(form));

    // Asignación del host según el ambiente a desplegar
    var ambiente = config.url_host;
    var host_to_deploy = "";
    if(ambiente=="DEV"){
        host_to_deploy = serviceConfig.host
    }
    if(ambiente=="LOCAL"){
        host_to_deploy = serviceConfig.url_local
    }

    console.log("Ambiente: " + ambiente + " HOST: " + host_to_deploy);

    axios({
        baseUrl: config.host,
        method: req.method,
        url: `${host_to_deploy}:${serviceConfig.port}/${path}`,
        //Validate how to pass headers
        data: form, // Pasamos el FormData con los archivos y otros datos
        responseType: 'arraybuffer',  // Cambiar a arraybuffer para manejar datos binarios

    })
    .then(response => {
       // Configurar el tipo de contenido según la respuesta del microservicio
        res.setHeader('Content-Type', response.headers['content-type'] || 'application/octet-stream');

        // Si el encabezado 'Content-Disposition' está presente, configurarlo
        if (response.headers['content-disposition']) {
            res.setHeader('Content-Disposition', response.headers['content-disposition']);
        }

        // Enviar el archivo binario
        res.send(response.data);  // Enviar directamente el contenido binario
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

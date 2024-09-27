import { Router } from "express";
import serviceImages from '../service/images-service';
import multer from "multer";
import path from "path";
import config from "../config";
import fs from "fs";

// si no existe el directorio, lo crea
var onLocalSave = config.pathToSave;
var pathToSave = "";

if (onLocalSave == "DEV") {
    pathToSave = "/var/lib/images/data";
} else {
    pathToSave = __dirname;
}

const uploadDir = path.join(pathToSave, 'uploads');


if (!fs.existsSync(uploadDir)) {
    fs.mkdirSync(uploadDir, { recursive: true }); // Asegúrate de que el directorio se cree si no existe
}

// Configuramos multer para almacenar las imágenes en el volumen
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, path.join(pathToSave, 'uploads'));  // Usamos __dirname para una ruta absoluta
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    },
});

const upload = multer({ storage });

const router = Router();

router.get('/ping', serviceImages.ping);
router.post('/upload', upload.single('image'), serviceImages.uploadImage);
router.get('/print/:id', serviceImages.print);
router.post('/upload/:tableToUpload', serviceImages.uploadInformation);
router.get('/print/:table/:idUsedOn/:funcionality', serviceImages.obtainInformation);

module.exports = router;
import { Router } from "express";
import serviceImages from '../service/images-service';
import multer from "multer";
import path from "path";
import config from "../config";
import fs from "fs";

const router = Router();

router.get('/ping', serviceImages.ping);
router.post('/upload/:tableToUpload', serviceImages.uploadInformation);
router.get('/print/:table/:idUsedOn/:funcionality', serviceImages.obtainInformation);

module.exports = router;
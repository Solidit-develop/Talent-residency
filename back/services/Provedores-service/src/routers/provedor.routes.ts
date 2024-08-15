import { Router } from "express";
import controllerProvider from "../controllers/provedor.controllers";

const router = Router();

router.get('/correo',controllerProvider.infocomplete);

module.exports= router

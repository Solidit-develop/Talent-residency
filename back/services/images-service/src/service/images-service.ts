import { Request, Response } from "express";
import config from "../config";
import path from "path";
import { AppDataSource } from "../database";
import { images } from "../entitis/images";
import { imagesRelation } from "../entitis/imagesRelation";

const repoImages = AppDataSource.getRepository(images);
const repoImagesRelation = AppDataSource.getRepository(imagesRelation);

const serviceImages = {
    ping: async (req: Request, res: Response): Promise<void> => {
        res.send("pong")
    },

    uploadImage: async (req: Request, res: Response): Promise<void> => {
        const baseAddress = config.baseAddress
        const fileName = req.file?.filename;
        console.log("Ruta: ", req.url);
        var message = "";

        console.log("BaseAddress: ", baseAddress);
        if (!req.file) {
            console.log("Should ends here");
            message = "No file uploaded.";
        }
        const imageUrl = `${fileName}`;
        console.log("Should ends");
        message = imageUrl;
        res.send({ "response": message });
    },

    print: async (req: Request, res: Response): Promise<void> => {
        const id = req.params.id;
        console.log("Find the url:" + id);
        var onLocalSave = config.pathToSave;
        var pathToSave = "";
        var pathToFind = "";
        /*** Change line to test on local */
        if (onLocalSave == "DEV") {
            pathToSave = "/var/lib/images/data";
            pathToFind = path.join(pathToSave, '/uploads/');
        } else {
            pathToSave = __dirname;
            pathToFind = path.join(pathToSave, '../routes/uploads/');
        }

        const result = pathToFind + id;
        console.log("PTF: " + result);
        res.sendFile(result);
    }
}
export default serviceImages;

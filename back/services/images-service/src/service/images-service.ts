import { Request, Response } from "express";
import config from "../config";
import path from "path";
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
        const imageUrl = `${baseAddress}/uploads/${fileName}`;
        console.log("Should ends");
        message = imageUrl;
        res.send({ "response": message });
    },

    print: async (req: Request, res: Response): Promise<void> => {
        const id = req.params.id;
        console.log("Find the url:" + id);
        var onLocalSave = config.pathToSave;
        var pathToSave = "";

        if (onLocalSave == "DEV") {
            pathToSave = "/var/lib/images/data";
        } else {
            pathToSave = __dirname;
        }

        // const pathToFind = path.join(pathToSave, '../routes/uploads/');
        const pathToFind = path.join(pathToSave, '/uploads/');
        console.log("PTF: " + pathToFind);
        res.sendFile(pathToFind + id);

    }
}
export default serviceImages;

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
        const imageUrl = `${baseAddress}/uploads/${fileName}`;
        console.log("Should ends");
        message = imageUrl;
        res.send({ "response": message });
    },

    uploadInformation: async (req: Request, res: Response): Promise<void> => {
        var table = req.params.tableToUpload;
        console.log("Table: " + table);
        const { funcionality, urlLocation, idUsedOn } = req.body;

        const row = new images();
        row.funcionality = funcionality;
        row.urlLocation = urlLocation;

        const result = await repoImages.save(row);
        console.log("Result saved: " + result.id_images);

        const relationRow = new imagesRelation();
        relationRow.idUsedOn = idUsedOn;
        relationRow.tableToRelation = table;
        relationRow.images = result

        const restOnRelation = await repoImagesRelation.save(relationRow);

        console.log("Result on relation: " + restOnRelation.id_imagesRelation);;
        res.json({ "imageSaved": result.id_images, "entityOrigen": restOnRelation.tableToRelation, "nameToFind": result.urlLocation });
    },

    obtainInformation: async (req: Request, res: Response): Promise<void> => {
        const { table, idUsedOn, funcionality } = req.params

        const resultImageRelation = await repoImagesRelation.findOne({
            where:
            {
                idUsedOn: parseInt(idUsedOn),
                tableToRelation: table
            }
        });

        const idImageToFind = resultImageRelation?.images.id_images;

        const resultImage = await repoImages.findOne({
            where: {
                id_images: idImageToFind,
                funcionality: funcionality
            }
        })


        res.json({ table, idUsedOn, funcionality, "imageName": resultImage?.urlLocation });
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

        //const pathToFind = path.join(pathToSave, '../routes/uploads/');
        const pathToFind = path.join(pathToSave, '/uploads/');
        const result = pathToFind + id;
        console.log("PTF: " + result);
        res.sendFile(result);
    }
}
export default serviceImages;

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
        const { table, idUsedOn, funcionality } = req.params;
        const id = parseInt(idUsedOn);
        let errorMessage: string | null = null; // Mensaje de error inicial
        var resultImage: images | null = new images();

        // Buscar la relación de imagen
        const resultImageRelation = await repoImagesRelation.findOne({
            where: {
                idUsedOn: id,
                tableToRelation: table // Agregar validación para la tabla
            },
            relations: ['images'] // Carga la relación `images`
        });

        // Validación de resultImageRelation
        if (!resultImageRelation) {
            errorMessage = "No se encontró la relación de imagen para el id proporcionado.";
        } else {
            console.log("Id to find: " + id);
            console.log("Table result: " + resultImageRelation?.tableToRelation);

            const idImageToFind = resultImageRelation.images?.id_images; // Usar el encadenamiento opcional

            // Validación de idImageToFind
            if (!idImageToFind) {
                errorMessage = "No se encontró el ID de la imagen en la relación.";
            } else {
                resultImage = await repoImages.findOne({
                    where: {
                        id_images: idImageToFind,
                        funcionality: funcionality
                    }
                });

                // Validación de resultImage
                if (!resultImage) {
                    errorMessage = "No se encontró la imagen correspondiente a la funcionalidad proporcionada.";
                }
                // Construir la respuesta final


            }

        }
        const response = {
            table,
            idUsedOn,
            funcionality,
            message: errorMessage || null, // Agregar el mensaje de error si existe
            imageName: resultImage?.urlLocation || null // Usar el encadenamiento opcional
        };

        // Devolver la respuesta
        res.json(response);
    },
}
export default serviceImages;

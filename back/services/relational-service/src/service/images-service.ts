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
        try{
            const { table, idUsedOn, funcionality } = req.params;
            const id = parseInt(idUsedOn);
            let errorMessage: string | null = null; // Mensaje de error inicial
            var funcion = String(funcionality)
            // Buscar la relación de imagen

            
            // const resultImageRelation = await repoImagesRelation.find({
            //     where: {
            //         idUsedOn: id,
            //         tableToRelation: table, // Agregar validación para la tabla
            //     },
            //     relations: ['images'] // Carga la relación `images`
            // });

            const relacionImagen = await repoImages.createQueryBuilder("images")
            .leftJoinAndSelect("images.imagesRelation","imagesRelation")
            .where("imagesRelation.idUsedOn = :idUsedOn and imagesRelation.tableToRelation = :tableToRelation and images.funcionality = :funcionality  ",{idUsedOn:id,tableToRelation:table,funcionality:funcion})
            .getMany();


            console.log("Imagenes................")
            console.log(relacionImagen);

            // Validación de resultImageRelation
            if (relacionImagen.length<=0) {
                errorMessage = "No se encontró la relación de imagen para el id proporcionado.";
            } 
                const response = {
                    table,
                    idUsedOn,
                    funcionality,
                    message: errorMessage || null, // Agregar el mensaje de error si existe,
                    relacionImagen
                };
        
                // Devolver la respuesta
                res.json(response);
        
        }catch(error){
            console.log(error)
            res.status(500).json("Error interno")
        }

    },

}
export default serviceImages;

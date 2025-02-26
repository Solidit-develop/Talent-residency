import { Request, Response } from "express";
import { AppDataSource } from "../database";
import { Estado } from "../entitis/estados";
import { Municipio } from "../entitis/municipios";
import { Localidad } from "../entitis/localidades";

// Obtener los repositorios de las entidades
const repositorioEstado = AppDataSource.getRepository(Estado);
const repositorioMunicipio = AppDataSource.getRepository(Municipio);
const repositorioLocalidad = AppDataSource.getRepository(Localidad);

const controlleraddresses = {

  // Controlador de ping
  ping: async (req: Request, res: Response): Promise<void> => {
    console.log("pong");
    res.json("pong");
  },

  // Controlador para obtener todos los estados
  estados: async (req: Request, res: Response): Promise<void> => {
    try {
      const estados = await repositorioEstado.find();
      const resultado = estados.map((estado) => ({
        state_name: estado.nombre,
      }));
      res.status(200).json(resultado);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Error interno" });
    }
  },

  // Controlador para obtener municipios de un estado
  municipio: async (req: Request, res: Response): Promise<void> => {
    try {
      const state = req.params.state;
  
      // Buscar el estado por nombre
      const estado = await repositorioEstado.findOne({ where: { nombre: state } });
  
      if (!estado) {
        // No se encontró el estado
         res.status(404).json({ message: "Estado no encontrado" });
         return;
      }
  
      // Buscar municipios del estado encontrado
      const municipios = await repositorioMunicipio.find({ where: { estado_id: estado.id } });
  
      // Mapeamos los resultados de los municipios
      const result = municipios.map((municipio) => ({
        city_name: municipio.nombre,
      }));
  
      // Responder con los municipios encontrados
      res.status(200).json(result); // No es necesario un 'return' aquí
    } catch (error) {
      console.error(error);
      // Si hay un error, responder con un error interno
      res.status(500).json({ message: "Error interno" });
    }
  },
  
  
};

export default controlleraddresses;

import dotenv from 'dotenv';

// Cargar las variables de entorno desde el archivo .env
dotenv.config();

console.log(" ");

interface Config {
    db_type?: string;
    host?: string;
    db_port?: string;
    user?: string;
    password?: string;
    database?: string;
    baseAddress?: string;
    pathToSave?: string;
}
const config: Config = {
    db_type: process.env.DB_TYPE,
    host: process.env.DB_HOST,
    db_port: process.env.DB_PORT,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB,
    baseAddress: process.env.BaseAddress,
    pathToSave: process.env.PathToSave
};

export default config;
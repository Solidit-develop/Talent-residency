import dotenv from 'dotenv';

// Cargar las variables de entorno desde el archivo .env
dotenv.config();

console.log(" VARIABLES DE ENTORNO CARGADAS");

interface Config {
    token_url_locations:string;
    username_token_location?: string;
    password_token_location?: string;
}
const config: Config = {
    token_url_locations: process.env.TOKEN_URL_LOCATIONS || "https://www.universal-tutorial.com/api/getaccesstoken",
    username_token_location: process.env.TOKEN_LOCATIONS_USER,
    password_token_location: process.env.TOKEN_LOCATIONS_PASSWORD,
};

export default config;
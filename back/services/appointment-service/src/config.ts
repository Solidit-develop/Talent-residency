import dotenv from 'dotenv';

dotenv.config();

// console.log(process.env)
interface Config{
    db_type?:string;
    host?:string;
    port?:string;
    user?:string;
    password?:string;
    database?:string;
}

const config:Config={
    db_type: process.env.DB_TYPE,
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB
}

export default config
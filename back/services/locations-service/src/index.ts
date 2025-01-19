import "reflect-metadata"
import app from "./app"
// require ("dotenv").config({path:"./src/.env"})
import 'dotenv/config'

async function main() {
    try {
        const portserver = process.env.PORT || 4012
        app.listen(portserver)
        console.log("Servidor escuchando en el purto ", portserver)

    }
    catch (error) {
        console.log(error)
    }

}

main()

const pool = require('../database');

const controlleraddresses = {}

controlleraddresses.ping = async (req,res)=>{
    console.log("pong")
    res.json("pong")
}

controlleraddresses.estados = async (req, res) => {
    try {
        const [estados] = await pool.query("SELECT * FROM estados");

        // Transformamos la respuesta con map()
        const resultado = estados.map(estado => ({
            state_name: estado.nombre
        }));

        res.status(200).json(resultado);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: "Error interno" });
    }
};

controlleraddresses.municipio=async(req,res)=>{
    try{
        const state = req.params.state;
        const [states]= await pool.query("SELECT  * FROM estados WHERE nombre = ?", [state])

        const id = String(states[0].id);
        const [city] = await pool.query("SELECT nombre FROM municipios WHERE estado_id = ?",[id])
        console.log(id);


        const result = city.map(city => ({
            city_name: city.nombre
        }));

        console.log(result)
        res.status(200).json(result)
        
    }catch(error){
        console.error(error);
        res.status(500).json({ message: "Error interno" });
    }
}

module.exports = controlleraddresses;
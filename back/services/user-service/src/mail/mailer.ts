import nodemailer,{Transporter} from 'nodemailer'
// require ("dotenv").config({path:"./src/.env"})


const transporter: Transporter = nodemailer.createTransport({
    host:'smtp.gmail.com',
    port:587,
    secure:true,
    tls:{
        rejectUnauthorized:true,
        minVersion: "TLSv1.2"
    },
    auth:{
    user: 'devsolidit@gmail.com',
    pass: 'qqdc mnwm blph lwwf'
    }

});

transporter.verify()

.then(()=>{
    console.log("Listo emails")
})
.catch((err)=>{
    console.log("Error",err)
})

export default transporter;

//comentario1
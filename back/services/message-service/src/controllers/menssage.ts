import { Request, Response } from "express";


const controllermessages={

    ping: async(req:Request, res:Response): Promise<void> => {
        res.send("pong");
    },
}

export default controllermessages;
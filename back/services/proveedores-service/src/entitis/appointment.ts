import { Entity, PrimaryGeneratedColumn } from "typeorm";

@Entity()

export class appointment{
    @PrimaryGeneratedColumn()
    id_appointment:number

    

}
import { Entity, PrimaryGeneratedColumn,Column, OneToOne, JoinColumn } from "typeorm";
import { appointment } from "./appointment";
import { review } from "./review";

@Entity()

export class interaccion{

    @PrimaryGeneratedColumn()
    id_interaccion:number

    @Column({type:"boolean"})
    origenEmitidoComoUser:boolean

    @OneToOne(()=>appointment, appointment=> appointment.interaccion)
    @JoinColumn()
    appointment:appointment
    
    @OneToOne(()=>review, review=>review.interaccion)
    @JoinColumn()
    review:review
}
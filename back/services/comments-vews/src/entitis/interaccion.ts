import { Entity, PrimaryGeneratedColumn,Column, OneToOne, JoinColumn, JoinTable, ManyToMany } from "typeorm";
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
    
    @ManyToMany(() => review, (review) => review.interacciones)
    @JoinTable({
        name: "review_interacciones_interaccion", // Nombre de la tabla intermedia
        joinColumn: {
            name: "interaccionIdInteraccion", // Columna de esta entidad
            referencedColumnName: "id_interaccion",
        },
        inverseJoinColumn: {
            name: "reviewIdReview", // Columna de la otra entidad
            referencedColumnName: "id_review",
        },
    })
    reviews: review[];
}
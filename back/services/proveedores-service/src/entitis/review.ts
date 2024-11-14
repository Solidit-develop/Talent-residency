import { Column, Entity, ManyToMany, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { interaccion } from "./interaccion";

@Entity()

export class review {
    @PrimaryGeneratedColumn()
    id_review: number;

    @Column({ type: 'varchar' })
    calificacion: string

    @Column({ type: 'varchar' })
    comment: string

    @Column({ type: 'varchar' })
    image: string

    @OneToMany(()=>interaccion,interaccion=>interaccion.review)
    interaccion:interaccion[]
}

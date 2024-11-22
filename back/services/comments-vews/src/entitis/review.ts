import { Column, Entity, ManyToMany, ManyToOne, OneToMany, JoinTable, PrimaryGeneratedColumn } from "typeorm";
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

    @ManyToMany(() => interaccion, (interaccion) => interaccion.reviews)
    interacciones: interaccion[];

}

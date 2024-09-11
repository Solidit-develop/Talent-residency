import { Column, Entity, ManyToMany, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { appointment } from "./appointment";

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

    @ManyToOne(() => appointment, appointmen => appointmen.revie)
    appointment: review;

}

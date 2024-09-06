import { Column, Entity, OneToMany, PrimaryColumn, PrimaryGeneratedColumn } from "typeorm";
import { appointment } from "./appointment";

@Entity()
export class statusAppointment {

    @PrimaryGeneratedColumn()
    id_statusAppointment: number

    @Column({ type: 'varchar', length: 500 })
    descripcion: string

    @Column({ type: 'boolean' })
    value: boolean

    @OneToMany(() => appointment, estatus => estatus.estatus)
    statusAppointment: appointment[];
}



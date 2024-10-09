import { Column, Entity, ManyToMany, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from "typeorm";
// import { statusAppointment } from "./statusAppointment";
import { agrements } from "./agrements";

@Entity()

export class appointment {

    @PrimaryGeneratedColumn()
    id_appointment: number

    @Column({ type: 'time',nullable: true })
    creationDate: Date | null;

    @Column({ type: 'time',nullable: true  })
    apointmentDate: Date | null;

    @Column({ type: 'varchar' })
    AppointmentLocation: string

    @Column({type:'varchar'})
    statusAppointment:string;

    @OneToMany(()=>agrements,agrements=>agrements.appointment)
    agrements:agrements;
    
} 
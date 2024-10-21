import { Column, Entity, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Providers } from "./provedores";
import { users } from "./users";

@Entity()
export class appointment {

    @PrimaryGeneratedColumn()
    id_appointment: number

    @Column({ type: 'date' })
    creationDate: Date;

    @Column({ type: 'date' })
    apointmentDate: Date;

    @Column({ type: 'varchar' })
    AppointmentLocation: string

    @Column({type:"varchar"})
    statusAppointment:string

    // Provedor
    @ManyToOne(() => Providers, providers => providers.appointment)
    providers: Providers;

    // Customer
    @ManyToOne(()=> users,users=>users.appointment )
    users:users
} 
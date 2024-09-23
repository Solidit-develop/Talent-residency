import { Column, Entity, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Providers } from "./provedores";
import { users } from "./users";

@Entity()

export class appointment {

    @PrimaryGeneratedColumn()
    id_appointment: number

    @Column({ type: 'time' })
    creationDate: Date;

    @Column({ type: 'time' })
    apointmentDate: Date;

    @Column({ type: 'varchar' })
    AppointmentLocation: string

    @ManyToOne(() => Providers, providers => providers.appointment)
    providers: Providers;

    @ManyToOne(()=> users,users=>users.appointment )
    users:users
} 
import { Column, Entity, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { statusAppointment } from "./statusAppointment";
import { review } from "./review";
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

    @ManyToOne(() => statusAppointment, statusAppointment => statusAppointment.statusAppointment)
    estatus: statusAppointment;

    @OneToMany(() => review, revie => revie.appointment)
    revie: review[];

    @ManyToOne(() => Providers, providers => providers.appointment)
    providers: Providers;

    @ManyToOne(()=> users,users=>users.appointment )
    users:users
} 
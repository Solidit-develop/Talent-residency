import { Column, Entity, ManyToMany, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from "typeorm";
// import { statusAppointment } from "./statusAppointment";

import { Providers } from "./provedores";
import { users } from "./users";
import { agrements } from "./agrements";
import { interaccion } from "./interaccion";

@Entity()

export class appointment {

    @PrimaryGeneratedColumn()
    id_appointment: number

    @Column({ type: 'date',nullable: true })
    creationDate: Date | null;

    @Column({ type: 'date',nullable: true  })
    apointmentDate: Date | null;

    @Column({ type: 'varchar' })
    AppointmentLocation: string

    @Column({type:'varchar'})
    statusAppointment:string;

    @ManyToOne(() => Providers, providers => providers.appointment)
    providers: Providers;

    @ManyToOne(()=> users,users=>users.appointment )
    users:users;

    @OneToMany(()=>agrements,agrements=>agrements.appointment)
    agrements:agrements;

    @OneToOne(()=>interaccion,interaccion=>interaccion.appointment)
    
    interaccion:interaccion


    
} 
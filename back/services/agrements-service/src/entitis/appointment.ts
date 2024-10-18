import { Column, Entity, ManyToMany, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from "typeorm";
// import { statusAppointment } from "./statusAppointment";
import { agrements } from "./agrements";
import { users } from "./users";
import { Providers } from "./provedores";
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

    @OneToMany(()=>agrements,agrements=>agrements.appointment)
    agrements:agrements;

    @ManyToOne(()=> users,users=>users.appointment )
    users:users

    @ManyToOne(() => Providers, providers => providers.appointment)
    providers: Providers;
    
} 
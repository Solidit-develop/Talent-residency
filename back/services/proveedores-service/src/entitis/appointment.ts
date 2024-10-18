import { Column, Entity, ManyToMany, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from "typeorm";
// import { statusAppointment } from "./statusAppointment";
import { review } from "./review";
import { Providers } from "./provedores";
import { users } from "./users";
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

    @OneToMany(() => review, revie => revie.appointment)
    revie: review[];

    @Column({type:'varchar'})
    statusAppointment:string;

    @ManyToOne(() => Providers, providers => providers.appointment)
    providers: Providers;

    @ManyToOne(()=> users,users=>users.appointment )
    users:users;

    @OneToMany(()=>agrements,agrements=>agrements.appointment)
    agrements:agrements;
    
} 
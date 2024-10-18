import { Column, Entity, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { appointment } from "./appointment";
import { Providers } from "./provedores";
import { agrements_service } from "./agrements-service";
@Entity()

export class agrements{
    [x: string]: any;
    @PrimaryGeneratedColumn()
    id_agements:number

    @Column({type:'varchar'})
    description:string

    @Column({type:'varchar'})
    creationDate:string

    @ManyToOne(()=>appointment,appointment=>appointment.agrements)
    appointment:appointment;

    @ManyToOne(()=>Providers,Providers=>Providers.agrements)
    providers:Providers

    @OneToMany(()=>agrements_service,agrements=>agrements.agrements)
    agrements_service:agrements_service;

}
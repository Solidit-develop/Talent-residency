import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { agrements_service } from "./agrements-service";

@Entity()

export class serviceStatus{
    @PrimaryGeneratedColumn()
    id_serviceStatus:number;
    
    @Column({type:'varchar'})
    description:string;

    @Column({type:'varchar'})
    value:string

    @OneToMany(()=>agrements_service,service=>service.serviceStatus)
    status:agrements_service;
}
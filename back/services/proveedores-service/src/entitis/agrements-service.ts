import { Column, Entity, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { serviceStatus } from "./servicesStatus";
import { agrements } from "./agrements";

@Entity()

export class agrements_service{
    @PrimaryGeneratedColumn()
    id_agrements_service:number;

    @Column({type:'varchar'})
    description:string;

    @Column({type:'varchar'})
    creationDate:string;

    @ManyToOne(()=>serviceStatus,status=>status.status)
    serviceStatus:serviceStatus;

    @ManyToOne(()=>agrements,agrements=>agrements.agrements_service)
    agrements:agrements;
} 
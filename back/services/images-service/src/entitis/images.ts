import { Column, Entity, JoinTable, ManyToOne, OneToMany, PrimaryColumn, PrimaryGeneratedColumn } from "typeorm";
import { imgenRelation } from "./imagesRelation";

@Entity()
export class images{
    
    @PrimaryGeneratedColumn()
    id_mesages:number;

    @Column({type:"varchar"})
    urlLocation:string;

    @Column({type:"varchar"})
    description:string;

    @OneToMany(()=>imgenRelation,imgenRelation=>imgenRelation.images)
    imgenRelation:imgenRelation;

}
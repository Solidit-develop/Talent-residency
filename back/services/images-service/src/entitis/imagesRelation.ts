import { Column, Entity, JoinTable, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { images } from "./images";

@Entity()
export class imgenRelation{
    @PrimaryGeneratedColumn()
    id_imagesRelation:number;

    @Column({type:"varchar"})
    idUsedOn:number;
    
    @Column({type:"varchar"})
    functionalidad:string;
    
    @ManyToOne(()=>images, images=>images.imgenRelation)
    @JoinTable({name:"id_mesages"})
    images:images;
}
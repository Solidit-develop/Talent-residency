import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { Address } from "./adrdess";
import { userTypes } from "./typesUsers";

@Entity()
export class users{
    @PrimaryGeneratedColumn()
    id_user:number

    @Column({type:"varchar",length:100})
    name_User:string 

    @Column({type: "varchar", length: 100})
    lasname:string

    @Column({type: "varchar", length:100})
    email:string

    @Column({type: "varchar", length:100})
    password:string

    @Column({type:"varchar",length:30})
    age:String

    @Column({type: "varchar", length:20})
    phoneNumber:string

    @Column({type:"varchar",length:10, nullable:true })
    imageUs:string | null;

    @ManyToOne(()=>userTypes,usertypes=>usertypes.id_userType)
    usertypes:userTypes

    @ManyToOne(()=>Address, adress=>adress.id_address)
    adress:Address

}
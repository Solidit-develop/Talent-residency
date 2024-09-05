import { Column, Entity, ManyToMany, ManyToOne, OneToMany, PrimaryColumn, PrimaryGeneratedColumn } from "typeorm";
import { users } from "./users";
import { Messages } from "./messages";


@Entity()
export class Conversation{
    @PrimaryGeneratedColumn()
    id_conversation:number
    
    @Column({type:"date"})
    creationDate:Date

    @OneToMany(()=>users,users=>users.conversation)
    user:users[]

    @ManyToOne(()=>Messages,Messages=>Messages.conversation)
    messages:Messages;

}



// tomcat 
// dataservices
// srepits
// datavertions

// ruta user/Sap/sid/dataservices/

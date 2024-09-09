import { Column, Entity, JoinColumn, ManyToMany, ManyToOne, OneToMany, PrimaryColumn, PrimaryGeneratedColumn } from "typeorm";
import { users } from "./users";
import { Messages } from "./messages";


@Entity()
export class Conversation{
    @PrimaryGeneratedColumn()
    id_conversation:number
    
    @Column({type:"date"})
    creationDate:Date

    @ManyToOne(()=>users,users=>users.id_user)
    @JoinColumn()
    id_userOrigen:users

    @ManyToOne(()=>users,users=>users.id_user)
    @JoinColumn()
    id_userDestino:users

    // @ManyToOne(()=>users,users=>users.conversation)
    // user:users

    @OneToMany(()=>Messages,Messages=>Messages.conversation)
    messages:Messages[];


}



// tomcat 
// dataservices
// srepits
// datavertions

// ruta user/Sap/sid/dataservices/

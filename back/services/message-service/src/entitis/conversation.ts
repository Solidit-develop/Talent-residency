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
    @JoinColumn({name:"idUserOrigenIdUser"})
    id_userOrigen:users

    @Column({type:'int'})
    id_userDestino:number


    // @ManyToOne(()=>users,users=>users.conversation)
    // user:users

    @OneToMany(()=>Messages,Messages=>Messages.conversation)
    messages:Messages[];

}


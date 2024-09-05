import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Conversation } from "./conversation";

@Entity()
export class Messages{
    @PrimaryGeneratedColumn()
    id_messages:number

    @Column({type:"varchar" })
    contect:string

    @Column({type:"date"})
    senddate:Date

    @OneToMany(()=>Conversation,Conversation=>Conversation.messages)
    conversation:Conversation
}
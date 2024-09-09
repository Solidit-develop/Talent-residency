import { Column, Entity, JoinColumn, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Conversation } from "./conversation";

@Entity()
export class Messages{
    @PrimaryGeneratedColumn()
    id_messages:number

    @Column({type:"varchar" })
    contect:string

    @Column({type:"date"})
    senddate:Date

    @ManyToOne(()=>Conversation,Conversation=>Conversation.messages)
    @JoinColumn()
    conversation:Conversation;
}
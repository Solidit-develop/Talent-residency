import { Column, Entity, JoinColumn, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn,Timestamp } from "typeorm";
import { Conversation } from "./conversation";

@Entity()
export class Messages{
    @PrimaryGeneratedColumn()
    id_messages:number

    @Column({type:"varchar" })
    contect:string

    @Column({type:"timestamp without time zone",nullable: true })
    senddate:Timestamp | null

    @ManyToOne(()=>Conversation,Conversation=>Conversation.messages)
    @JoinColumn()
    conversation:Conversation;
}
import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { users } from "./users";
@Entity()
export class userTypes {
    @PrimaryGeneratedColumn()
    id_userType: number

    @Column({ type: "varchar", length: 400 })
    descripcion: string

    @Column({ type: Boolean })
    value: boolean

    @OneToMany(() => users, user => user.usertypes)
    user: users[];


}
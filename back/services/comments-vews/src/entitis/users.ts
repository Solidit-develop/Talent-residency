import { Column, Entity, JoinColumn, ManyToOne, OneToMany, OneToOne, PrimaryGeneratedColumn } from "typeorm";
import { Providers } from "./provedores";
import { appointment } from "./appointment";

@Entity()
export class users {
    @PrimaryGeneratedColumn()
    id_user: number;

    @Column({ type: "varchar", length: 100 })
    name_user: string;

    @Column({ type: "varchar", length: 100 })
    lastname: string;

    @Column({ type: "varchar", length: 100 })
    email: string;

    @Column({ type: "varchar", length: 100 })
    password: string;

    @Column({ type: "varchar", length: 30 })
    age: String;

    @Column({ type: "varchar", length: 20 })
    phoneNumber: string;

    @OneToOne(() => Providers, provider => provider.user)
    provedor: Providers;

    @OneToMany(()=>appointment, appointment=>appointment.users)
    appointment:appointment[];


}
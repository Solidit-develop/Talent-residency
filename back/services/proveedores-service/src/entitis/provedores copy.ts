import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, ManyToMany, OneToOne, JoinColumn, OneToMany } from 'typeorm';
import { Address } from './adrdess';
import { users } from './users';
import { Conversation } from './conversation';
import { skills } from './skill';
import { appointment } from './appointment';
import { agrements } from './agrements';

@Entity()
export class Providers {
    @PrimaryGeneratedColumn()
    id_provider: number;

    @Column({ type: 'varchar', length: 50 })
    experienceYears: string;

    @Column({ type: 'varchar', length: 100 })
    workshopName: string;

    @Column({ type: 'varchar', length: 20 })
    workshopPhoneNumber: string;

    @ManyToOne(() => Address, address => address.providers)
    address: Address;

    @ManyToMany(() => skills, skills => skills.providers)
    skills: skills[]

    // aqui tiene la llave foranea
    @OneToOne(() => users, user => user.provedor)
    @JoinColumn()
    user: users

    @OneToMany(() => appointment, appointment => appointment.providers)
    appointment: appointment[];

    @OneToMany(()=> agrements, agrements=>agrements.providers)
    agrements:agrements;

}
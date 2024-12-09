import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, ManyToMany, OneToOne, JoinColumn, OneToMany } from 'typeorm';

import { appointment } from './appointment';
import { agrements } from './agrements';
import { users } from './users';

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

    @OneToMany(()=> agrements, agrements=>agrements.providers)
    agrements:agrements;

    @OneToMany(() => appointment, appointment => appointment.providers)
    appointment: appointment[];

    @OneToOne(() => users, user => user.provedor)
    @JoinColumn()
    user: users

}
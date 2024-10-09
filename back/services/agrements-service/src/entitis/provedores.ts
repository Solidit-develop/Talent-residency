import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, ManyToMany, OneToOne, JoinColumn, OneToMany } from 'typeorm';

import { appointment } from './appointment';
import { agrements } from './agrements';

@Entity()

export class Providers {
    @PrimaryGeneratedColumn()
    id_provider: number;

    // @Column({ type: 'varchar', length: 200 })
    // skill: string;

    @Column({ type: 'varchar', length: 50 })
    experienceYears: string;

    @Column({ type: 'varchar', length: 100 })
    workshopName: string;

    @Column({ type: 'varchar', length: 20 })
    workshopPhoneNumber: string;

    // aqui tiene la llave foranea


    @OneToMany(()=> agrements, agrements=>agrements.providers)
    agrements:agrements;

}
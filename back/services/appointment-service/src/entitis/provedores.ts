import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, ManyToMany, OneToOne, JoinColumn, OneToMany } from 'typeorm';
import { users } from './users';
import { appointment } from './appointment';

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

    @Column({ type: 'varchar', length: 200, nullable: true }) // Permitir nulos
    descripcion: string;

    // aqui tiene la llave foranea
    @OneToOne(() => users, user => user.provedor)
    @JoinColumn()
    user: users

    @OneToMany(() => appointment, appointment => appointment.providers)
    appointment: appointment[];


}
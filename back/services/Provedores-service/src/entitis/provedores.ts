import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, ManyToMany, OneToOne, JoinColumn } from 'typeorm';
import { Address } from './adrdess'; // Nota: Cambié "adrdess" a "address"
import { users } from './users';
import { skills } from './skill';

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
    
    @ManyToOne(() => Address, address => address.providers)
    address: Address;

    @ManyToMany(()=>skills,skills=>skills.providers)
    skills:skills[]

    // aqui tiene la llave foranea
    
    @OneToOne(()=>users, user =>user.provedor)
    @JoinColumn()
    user:users
}

// checar la relacion entre Usuarios y provedores en el metodo donde se unen
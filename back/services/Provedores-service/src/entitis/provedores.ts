import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from 'typeorm';
import { Address } from './adrdess'; // Nota: Cambié "adrdess" a "address"

@Entity()
export class Providers {
    @PrimaryGeneratedColumn()
    id_provider: number;

    @Column({ type: 'varchar', length: 200 })
    skill: string;

    @Column({ type: 'varchar', length: 50 })
    experienceYears: string;

    @Column({ type: 'varchar', length: 100 })
    workshopName: string; 

    @Column({ type: 'varchar', length: 20 })
    workshopPhoneNumber: string; 
    
    @ManyToOne(() => Address, address => address.providers)
    address: Address;
}
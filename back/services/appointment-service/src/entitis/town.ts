import { Column, Entity, ManyToOne, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { State } from './state'; 
import { Address } from './adrdess';

@Entity()
export class Town {
    @PrimaryGeneratedColumn()
    id_town: number;

    @Column({ type: 'varchar', length: 400 })
    name_Town: string;

    @Column({ type: 'varchar', length: 10 })
    zipCode: string;

    @ManyToOne(() => State, state => state.towns)
    state: State; // Relación muchos-a-uno con State

    @OneToMany(() => Address, address => address.town)
    addresses: Address[]; // Relación uno-a-muchos con Address
}
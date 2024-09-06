//towns

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

    @ManyToOne(() => State, state => state.town)
    state: State;

    @OneToMany(() => Address, address => address.town)
    addresses: Address[];

}
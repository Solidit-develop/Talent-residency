import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { Town } from './town'; 

@Entity()
export class State {
    @PrimaryGeneratedColumn()
    id_state: number;

    @Column({ type: 'varchar', length: 400 })
    name_State: string;

    @OneToMany(() => Town, town => town.state)
    towns: Town[]; // Relación uno-a-muchos con Town
}

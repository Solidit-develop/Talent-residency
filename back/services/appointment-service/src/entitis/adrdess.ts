import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { Town } from './town'; 

@Entity()
export class Address {
    @PrimaryGeneratedColumn()
    id_address: number;

    @Column({ type: 'varchar', length: 400 })
    street_1: string;

    @Column({ type: 'varchar', length: 400, nullable: true })
    street_2: string; // `nullable: true` permite que esta columna sea opcional

    @Column({ type:'varchar',length:200 })
    localidad:string;

    @ManyToOne(() => Town, town => town.addresses)
    town: Town; // Relación muchos-a-uno con Town
}
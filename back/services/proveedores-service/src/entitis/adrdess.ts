import { Column, Entity, ManyToOne, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { Town } from './town'; 
import { Providers } from './provedores'; // Nota: Cambié "providers" a "Providers"

@Entity()
export class Address {
    @PrimaryGeneratedColumn()
    id_address: number;

    @Column({ type: 'varchar', length: 400 })
    street_1: string;

    @Column({ type: 'varchar', length: 400, nullable: true })
    street_2: string; 

    @Column({ type: 'varchar', length: 200 })
    localidad: string;

    @ManyToOne(() => Town, town => town.addresses)
    town: Town; 

    @OneToMany(() => Providers, providers => providers.address) 
    providers: Providers[];
}
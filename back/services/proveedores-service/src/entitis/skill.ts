import { Column, Entity, JoinTable, ManyToMany, PrimaryGeneratedColumn } from "typeorm";
import { Providers } from "./provedores";

@Entity()

export class skills {
    @PrimaryGeneratedColumn()
    id_skills: number;

    @Column({ type: 'varchar', length: 200 })
    name: string;

    @ManyToMany(() => Providers, providers => providers.skills)
    @JoinTable()
    providers: Providers[];
}


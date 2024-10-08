import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { imagesRelation } from "./imagesRelation";

@Entity()
export class images {

    @PrimaryGeneratedColumn()
    id_images: number;

    @Column({ type: "varchar", length: 255 })
    urlLocation: string;

    @Column({ type: "varchar", length: 255 })
    funcionality: string;

    // Relación de uno a muchos con imagesRelation
    @OneToMany(() => imagesRelation, imagesRelation => imagesRelation.images)
    imagesRelation: imagesRelation[];  // Debe ser un array
}

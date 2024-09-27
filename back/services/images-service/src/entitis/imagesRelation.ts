import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { images } from "./images";

@Entity()
export class imagesRelation {
    @PrimaryGeneratedColumn()
    id_imagesRelation: number;

    @Column({ type: "int" })
    idUsedOn: number;

    @Column({ type: "varchar", length: 255 })
    tableToRelation: string;

    // Relación de muchos a uno con images
    @ManyToOne(() => images, images => images.imagesRelation)
    images: images;  // Clave foránea que apunta a la tabla images
}

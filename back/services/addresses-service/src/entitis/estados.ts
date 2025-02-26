import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";

@Entity()
export class Estado {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ length: 10 })
  clave: string;

  @Column({ length: 40 })
  nombre: string;

  @Column({ length: 10 })
  abrev: string;

  // Cambia el tipo a boolean
  @Column({ type: "boolean", default: true })
  activo: boolean;
}

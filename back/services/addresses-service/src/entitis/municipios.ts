import { Entity, PrimaryColumn, Column } from "typeorm";

@Entity("municipios")
export class Municipio {
  @PrimaryColumn({ type: "int" })
  id: number;

  @Column({ type: "int", comment: "Relación: estados -> id" })
  estado_id: number;

  @Column({ type: "varchar", length: 3, comment: "CVE_MUN – Clave del municipio" })
  clave: string;

  @Column({ type: "varchar", length: 100, comment: "NOM_MUN – Nombre del municipio" })
  nombre: string;

  // Cambio a tipo booleano
  @Column({ type: "boolean", default: true, comment: "Activo (true = Sí, false = No)" })
  activo: boolean;
}

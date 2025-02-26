import { Entity, PrimaryColumn, Column } from "typeorm";

@Entity("localidades")
export class Localidad {
  @PrimaryColumn({ type: "int" })
  id: number;

  @Column({ type: "int", comment: "Relación: municipios -> id" })
  municipio_id: number;

  @Column({ type: "varchar", length: 4, comment: "CVE_LOC – Clave de la localidad" })
  clave: string;

  @Column({ type: "varchar", length: 100, comment: "NOM_LOC – Nombre de la localidad" })
  nombre: string;

  @Column({ type: "varchar", length: 15, comment: "LATITUD – Latitud (en DMS)" })
  latitud: string;

  @Column({ type: "varchar", length: 15, comment: "LONGITUD – Longitud (en DMS)" })
  longitud: string;

  @Column({ type: "varchar", length: 15, comment: "ALTITUD – Altitud" })
  altitud: string;

  @Column({ type: "varchar", length: 10, comment: "CVE_CARTA" })
  carta: string;

  @Column({ type: "varchar", length: 1, comment: "AMBITO" })
  ambito: string;

  @Column({ type: "int", comment: "PTOT – Población Total" })
  poblacion: number;

  @Column({ type: "int", comment: "PMAS – Población Masculina" })
  masculino: number;

  @Column({ type: "int", comment: "PFEM – Población Femenina" })
  femenino: number;

  @Column({ type: "int", comment: "VTOT – Número total de viviendas" })
  viviendas: number;

  @Column({ type: "decimal", precision: 10, scale: 7, comment: "Latitud en decimal" })
  lat: number;

  @Column({ type: "decimal", precision: 10, scale: 7, comment: "Longitud en decimal" })
  lng: number;

  // Cambio a tipo booleano
  @Column({ type: "boolean", default: true, comment: "Activo (true = Sí, false = No)" })
  activo: boolean;
}

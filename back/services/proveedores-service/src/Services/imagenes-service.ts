// userService.ts
import { ApiService } from '../ApiService/ApiService';
import config from '../config';

export class ImagenService extends ApiService {
    constructor() {
        var ambiente = config.Ambiente;
        let baseUrl: string;

        if (ambiente === 'LOCAL') {
            baseUrl = 'http://localhost:4008/'; // URL para entorno local
        } else if (ambiente === 'DEV') {
            baseUrl = 'http://relationalservice:4008/'; // URL para producción
        } else {
            baseUrl = 'http://localhost:4008/'; // Valor por defecto o para otros entornos
        }
        super(baseUrl);
    }



    public async PostImage(data: Record<string, any> = {},tableToUpload:string,): Promise<any> {
        console.log("Llamada al método POST para enviar imagen con fecha");
        try {
            // Llamada al método `post` con el endpoint y los datos
            const response = await this.post<any>(`upload/${tableToUpload}`,data);
            console.log("********Respuesta del servidor en ImagenService: " + JSON.stringify(response));
            return response;
        } catch (error) {
            console.error('Error al enviar la imagen:', error);
            throw error;  // Relanzamos el error para manejarlo donde se llame la función
        }
    }
    
    public async getImageInfo(table: string,idUsedOn:string,funcionality:string ): Promise<any> {
        console.log("Llamada del get");
        return this.get(`print/${table}/${idUsedOn}/${funcionality}`)
            .then(response => {
                console.log("********Response on provider-service: " + JSON.stringify(response));
                return response;
            });
    }
}

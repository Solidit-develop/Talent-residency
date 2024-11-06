// userService.ts
import { ApiService } from '../ApiService/ApiService';
import config from '../config';

export class ImagenService extends ApiService {
    constructor() {
        var ambiente = config.Ambiente;
        let baseUrl: string;

        if (ambiente === 'LOCAL') {
            baseUrl = 'http://localhost:4011/'; // URL para entorno local
        } else if (ambiente === 'DEV') {
            baseUrl = 'http://comments-vews:4011/'; // URL para producción
        } else {
            baseUrl = 'http://localhost:4011/'; // Valor por defecto o para otros entornos
        }
        super(baseUrl);
    }

   
    public async getUserInfo(idToFind: string, file: File): Promise<any> {
        console.log("Llamada del post con archivo");
    
        // Utilizamos el método postWithFile para enviar la solicitud POST con el archivo
        return this.postWithFile('users/profile/' + idToFind, file)
            .then(response => {
                console.log("********Response on imageServer: " + JSON.stringify(response));
                return response;
            })
            .catch(error => {
                console.error('Error al obtener la información del usuario:', error);
                throw error;  // Relanzamos el error para manejarlo donde se llame la función
            });
    }
}

// userService.ts
import { ApiService } from '../ApiService/ApiService';
import config from '../config';

export class ProviderService extends ApiService {
    constructor() {
        // Define la URL base del servicio de usuarios
        // TODO: Validar mover esto a la instanciación de la clase config
        // Para asignar todas las variables de entorno en el primer momento
        var ambiente = config.Ambiente;
        let baseUrl: string;

        if (ambiente === 'LOCAL') {
            baseUrl = 'http://localhost:4002/'; // URL para entorno local
        } else if (ambiente === 'DEV') {
            baseUrl = 'http://providerservice:4002/'; // URL para producción
        } else {
            baseUrl = 'http://localhost:4002/'; // Valor por defecto o para otros entornos
        }
        super(baseUrl);
    }

    // Método específico para obtener usuarios
    public async getUserInfo(idToFind: string): Promise<any> {
        console.log("Llamada del get");
        return this.get('users/profile/' + idToFind)
            .then(response => {
                console.log("********Response on provider-service: " + JSON.stringify(response));
                return response;
            });
    }
}

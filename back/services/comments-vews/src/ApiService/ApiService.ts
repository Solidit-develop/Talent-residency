export class ApiService{

    public baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    // Método genérico para hacer una petición POST con un archivo
    public post<T>(tableToUpload: string, data: Record<string, any> = {}): Promise<T> {
        const url = `${this.baseUrl}${tableToUpload}`;
    
        // Opciones de configuración para la solicitud POST
        const requestOptions: RequestInit = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',  // Aseguramos que el contenido es JSON
            },
            body: JSON.stringify(data),  // Convertimos el objeto a JSON para enviarlo
        };

        return fetch(url, requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error en la respuesta: ${response.status} ${response.statusText}`);
                }
                return response.json(); // Convertimos la respuesta a JSON
            })
            .then((responseData: T) => {
                console.log("Resultado en ApiService: " + JSON.stringify(responseData));
                return responseData; // Devolvemos los datos JSON tipados
            })
            .catch(error => {
                console.error('Error en la petición POST:', error);
                throw error; // Relanzamos el error para manejarlo donde se llame la función
            });
    }
}
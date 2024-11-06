export class ApiService{

    private baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    // Método genérico para hacer una petición POST con un archivo
    public postWithFile(endpoint: string, file: File): Promise<any> {
        const url = `${this.baseUrl}${endpoint}`;

        // Crear una instancia de FormData
        const formData = new FormData();
        formData.append('file', file); // Aquí agregas el archivo

        // Opciones de configuración para la solicitud POST
        const requestOptions: RequestInit = {
            method: 'POST',
            body: formData,  // El cuerpo de la solicitud es el FormData con el archivo
        };

        return fetch(url, requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error en la respuesta: ${response.status} ${response.statusText}`);
                }
                return response.json(); // Convertimos la respuesta a JSON
            })
            .then(data => {
                console.log("Resultado en ApiService: " + JSON.stringify(data));
                return data; // Devolvemos los datos JSON
            })
            .catch(error => {
                console.error('Error en la petición POST con archivo:', error);
                throw error; // Relanzamos el error para manejarlo donde se llame la función
            });
    }

}
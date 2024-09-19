export class ResponseModel {
  code: number;
  success: boolean;
  response: any;

  constructor(
    code: number,
    success: boolean,
    response: any
  ) {
    this.code = code;
    this.success = success;
    this.response = response;
  }

  // Método estático para crear una respuesta exitosa
  static successResponse(response: any): ResponseModel {
    return new ResponseModel(200, true, response);
  }

  // Método estático para crear una respuesta de error
  static errorResponse(code: number, message: string): ResponseModel {
    return new ResponseModel(code, false, message);
  }
}

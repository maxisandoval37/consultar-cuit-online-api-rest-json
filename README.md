# Consultar CUIT Online – API REST JSON

Expone una API pública para consultar CUIT y entrega la respuesta como JSON simplificado. Está construida con Spring Boot WebFlux y actúa como proxy: normaliza el parámetro de CUIT, llama al sitio original respetando los encabezados necesarios y devuelve una respuesta lista para consumir desde otras aplicaciones.

- Reemplazar `<CUIT>` por el número sin guiones ni espacios (por ejemplo `33693450239`).
- El parámetro también se acepta como `cuilt` para mantener compatibilidad con integraciones previas.


### Ejemplo rápido

[consultar-cuit-online-api-rest-json.onrender.com/api/cuit?cuit=33693450239](https://consultar-cuit-online-api-rest-json.onrender.com/api/cuit?cuit=33693450239)

Respuesta esperada:

```json
{
  "query": "33693450239",
  "nombre": "ADMINISTRACION FEDERAL DE INGRESOS PUBLICOS",
  "cuit": "33-69345023-9",
  "tipoPersona": "Persona Jurídica • Ganancias: Sicore-Impto.a Las Ganancias • IVA: Iva No Alcanzado • Empleador",
  "sexo": ""
}
```

<hr>

## Información Adicional
Para cualquier información adicional o consultas: <maxisandoval98@gmail.com>

<p align="center"><b>¡Muchas gracias! 🦔</b></p>

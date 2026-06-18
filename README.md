# TFU5 - Backend de pedidos

Backend simple en Spring Boot para manejar productos, carritos y pedidos.

La idea del flujo es:

1. Consultar los productos disponibles.
2. Crear un carrito para un cliente.
3. Agregar productos al carrito.
4. Confirmar el carrito para crear un pedido.
5. Consultar o actualizar el estado del pedido.

Los datos se guardan en memoria, por lo que al reiniciar la aplicacion se pierden los carritos, pedidos e IDs generados.

## Requisitos

- Java instalado, compatible con la version configurada en `pom.xml`.
- Maven Wrapper incluido en el repositorio (`./mvnw`), por lo que no es necesario instalar Maven manualmente.
- Opcional: extension REST Client de Visual Studio Code para ejecutar el archivo `requests.http`.

## Ejecutar la aplicacion

```bash
./mvnw spring-boot:run
```

La aplicacion corre por defecto en:

```text
http://localhost:8080
```

## Probar endpoints

Hay dos formas simples de probar la API:

- Ejecutar los ejemplos `curl` que aparecen en este README.
- Abrir `requests.http` en Visual Studio Code y ejecutar cada request con la extension REST Client.

## Productos

### Listar productos

```http
GET /products
```

Devuelve los productos disponibles para agregar al carrito.

Ejemplo:

```bash
curl http://localhost:8080/products
```

Respuesta:

```json
[
  {
    "id": 1,
    "name": "Burger",
    "price": 250
  },
  {
    "id": 2,
    "name": "Fries",
    "price": 120
  }
]
```

## Carrito

### Crear carrito

```http
POST /carrito?clientName={nombre}
```

Crea un carrito vacio para un cliente. Todavia no se crea un pedido definitivo.

Ejemplo:

```bash
curl -X POST "http://localhost:8080/carrito?clientName=Nicolas"
```

Respuesta:

```json
{
  "id": 1,
  "clientName": "Nicolas",
  "productos": []
}
```

### Agregar producto al carrito

```http
POST /carrito/{carritoId}/productos/{productId}
```

Agrega un producto existente al carrito. El `productId` debe ser el ID de un producto obtenido desde `GET /products`.

Ejemplo:

```bash
curl -X POST http://localhost:8080/carrito/1/productos/1
```

Respuesta:

```json
{
  "id": 1,
  "clientName": "Nicolas",
  "productos": [
    {
      "id": 1,
      "name": "Burger",
      "price": 250
    }
  ]
}
```

Errores posibles:

- `404 Carrito no encontrado`
- `404 Producto no encontrado`

### Confirmar carrito

```http
POST /carrito/{carritoId}/confirmar
```

Confirma el carrito y crea un pedido definitivo. Despues de confirmar, el carrito se elimina de memoria.

Ejemplo:

```bash
curl -X POST http://localhost:8080/carrito/1/confirmar
```

Respuesta:

```json
{
  "id": 1,
  "clientName": "Nicolas",
  "estado": "EN_PREPARACION",
  "productos": [
    {
      "id": 1,
      "name": "Burger",
      "price": 250
    }
  ]
}
```

## Pedidos

### Listar pedidos

```http
GET /pedidos
```

Devuelve todos los pedidos confirmados.

Ejemplo:

```bash
curl http://localhost:8080/pedidos
```

Respuesta:

```json
{
  "1": {
    "id": 1,
    "clientName": "Nicolas",
    "estado": "EN_PREPARACION",
    "productos": [
      {
        "id": 1,
        "name": "Burger",
        "price": 250
      }
    ]
  }
}
```

### Obtener pedido por ID

```http
GET /pedidos/{pedidoId}
```

Devuelve un pedido confirmado por ID.

Ejemplo:

```bash
curl http://localhost:8080/pedidos/1
```

### Marcar pedido como listo

```http
POST /pedidos/{pedidoId}/marcar-listo
```

Cambia el estado del pedido a `LISTO`.

Ejemplo:

```bash
curl -X POST http://localhost:8080/pedidos/1/marcar-listo
```

Respuesta:

```json
{
  "id": 1,
  "clientName": "Nicolas",
  "estado": "LISTO",
  "productos": [
    {
      "id": 1,
      "name": "Burger",
      "price": 250
    }
  ]
}
```

### Cancelar pedido

```http
POST /pedidos/{pedidoId}/cancelar
```

Cambia el estado del pedido a `CANCELADO`.

Ejemplo:

```bash
curl -X POST http://localhost:8080/pedidos/1/cancelar
```

Respuesta:

```json
{
  "id": 1,
  "clientName": "Nicolas",
  "estado": "CANCELADO",
  "productos": [
    {
      "id": 1,
      "name": "Burger",
      "price": 250
    }
  ]
}
```

### Marcar pedido como entregado

```http
POST /pedidos/{pedidoId}/entregar
```

Cambia el estado del pedido a `ENTREGADO`.

Reglas:

- Solo se puede entregar un pedido que este en estado `LISTO`.
- No se puede entregar un pedido en estado `EN_PREPARACION`.
- No se puede entregar un pedido en estado `CANCELADO`.

Ejemplo:

```bash
curl -X POST http://localhost:8080/pedidos/1/entregar
```

Respuesta:

```json
{
  "id": 1,
  "clientName": "Nicolas",
  "estado": "ENTREGADO",
  "productos": [
    {
      "id": 1,
      "name": "Burger",
      "price": 250
    }
  ]
}
```

Errores posibles:

- `400 Solo se pueden entregar pedidos listos`
- `400 No se puede entregar un pedido cancelado`
- `404 Pedido no encontrado`

### Calcular total del pedido

```http
GET /pedido/{pedidoId}/total
```

Calcula el total del pedido sumando los precios de sus productos.

Ejemplo:

```bash
curl http://localhost:8080/pedido/1/total
```

Respuesta:

```json
250.0
```

## Flujo completo de ejemplo

```bash
# 1. Listar productos
curl http://localhost:8080/products

# 2. Crear carrito
curl -X POST "http://localhost:8080/carrito?clientName=Nicolas"

# 3. Agregar productos al carrito
curl -X POST http://localhost:8080/carrito/1/productos/1
curl -X POST http://localhost:8080/carrito/1/productos/2

# 4. Confirmar carrito y crear pedido
curl -X POST http://localhost:8080/carrito/1/confirmar

# 5. Consultar pedido
curl http://localhost:8080/pedidos/1

# 6. Consultar total
curl http://localhost:8080/pedido/1/total

# 7. Marcar pedido como listo
curl -X POST http://localhost:8080/pedidos/1/marcar-listo

# 8. Marcar pedido como entregado
curl -X POST http://localhost:8080/pedidos/1/entregar
```

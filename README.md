# Tienda Web Express

Proyecto Spring Boot + Thymeleaf con modulo de carrito de compras usando `HttpSession`.

## Funcionalidades del carrito

- Agregar productos desde catalogo y detalle.
- Ver carrito en `/carrito` con nombre, precio, cantidad, subtotal y total.
- Eliminar productos del carrito.
- Finalizar pedido desde el carrito.
- Al finalizar, se valida stock, se descuenta inventario y se limpia el carrito.

## Ejecutar

```bash
./mvnw spring-boot:run
```

Luego abrir `http://localhost:8080`.

## Probar

```bash
./mvnw test
```


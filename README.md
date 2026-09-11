# Tiny - Bank

Código de ejemplo siguiendo arquitectura hexagonal.

API Rest para simular un pequeño banco:

- Registro usuario
- Creación de cuenta (wallet)
- Realización de depósito de dinero
- Visualización de cuenta (wallet) --> Balance y movimientos
- Transferencia de una cuenta A a una cuenta B

## Instrucciones

Por defecto el proyecto, con el spring profile 'dev' corre sobre una base de datos mongo embebida. Este perfil es el seleccionado por defecto.
Con el spring profile 'local' la aplicación usara las propiedades del yml para conectarse a una bbdd mongo externa.


## Documentación

Documentación Swagger:
http://localhost:8080/swagger-ui.html


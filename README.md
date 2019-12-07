# JSimpleChat
Un chat muy sencillo implementado en Java


## Dependencias

Este proyecto depende del proyecto **[JUtilidades](http://github.com)**. Descargalo y si usas NetBeans indica que usarás dicho proyecto para hacer la compilación (en el menú "Propiedades del proyecto" busca "Libraries")

## Arquitectura

Existen dos partes bien diferenciadas: cliente y servidor. Para comunicarse, ambos elementos usan un protocolo muy sencillo:

* /NICK pepito 
* /PUBL Hola a todos
* /PRIV juanito Hola a juanito

* /NICK permite cambiar el nick de un usuario conectado. Lo habitual es usarlo justo despues de conectar con un servidor.
* /PUBL permite enviar un mensaje a todos los usuarios conectados.
* /PRIV permite enviar un mensaje a un solo usuario.

En este programa *no se hace apenas ningún control de errores*. El programa se ha creado con fines didácticos.

## Intentando entender el programa

Si quieres comprender mejor como funciona el programa ten en cuenta este orden.

### Servidor

El orden recomendado de lectura de las clases es este:

1. ``Mensaje.java``. Dada una línea como "/NICK pepito" o "/PRIV juanito Hola juanito" nos interesa trocear dicha línea para saber la siguiente información: ¿qué tipo de mensaje es? Podemos llamar al método ``getTipoMensaje()`` y nos devolverá el texto del mensaje público con el método ``getTextoMensaje()``. Los distintos tipos de mensaje están en la enumeración ``TipoMensaje``.

2. ``ClienteConectado.java``. Creada para mayor comodidad. El servidor la usa para tener en un solo sitio el nick y los flujos de entrada y salida que nos permiten comunicarnos con ese nick.

3. ``DistribuidorMensajes.java``. Guarda todos los clientes que están conectados en un momento dado. Nos permite enviar mensajes a todos los usuarios conectados o solo a un cliente (es decir, envía mensajes públicos y/o privados). **HAY UNA SOLA INSTANCIA DEL DISTRIBUIDOR QUE SE COMPARTE ENTRE TODAS LAS PETICIONES RECIBIDAS**

4. ``Peticion.java``. Implementa el interfaz ``Runnable``. Cuando un cliente se conecta al servidor, el servidor crea un hilo para atender a ese cliente. La clase se dedica a ejecutar un bucle infinito en el que se esperan mensajes enviados desde el cliente, se examinan y en función del tipo de mensaje (NICK, PUBL o PRIV) se toma una decisión. **IMPORTANTE:** Todas las petición reciben una copia del distribuidor de mensajes, un objeto compartido que se encarga del envío de los mensajes.

5. ``Servidor.java``. Implementación típica de un servidor. Se dedica a esperar conexiones y "derivarlas" a objetos ``Peticion`` tan pronto como llegan.

### Cliente

El orden recomendado es:

1. ``EscritorMensajesRecibidos.java``. Hilo en segundo plano que usa el cliente para poder recibir y escribir mensajes en pantalla mientras el cliente está esperando a que el usuario escriba líneas.

2. ``Cliente.java`` . Cliente en sí, ejecuta un bucle en el que espera que el usuario escriba líneas para enviar al servidor.

## Clases compartidas

1. ``Utilidades.java`` Contiene clases de utilidad para gestionar flujos de entrada/salida. Está en el proyecto **[JUtilidades](http://github.com)**
2. ``Constantes.java`` Empaqueta solo el puerto usado por cliente y servidor

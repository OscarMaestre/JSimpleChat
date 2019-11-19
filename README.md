# JSimpleChat
Un chat muy sencillo implementado en Java

##Arquitectura

Existen dos partes bien diferenciadas: cliente y servidor. Para comunicarse, ambos elementos usan un protocolo muy sencillo:

* /NICK pepito 
* /PUBL Hola a todos
* /PRIV juanito Hola a juanito

* /NICK permite cambiar el nick de un usuario conectado. Lo habitual es usarlo justo despues de conectar con un servidor.
* /PUBL permite enviar un mensaje a todos los usuarios conectados.
* /PRIV permite enviar un mensaje a un solo usuario.

En este programa *no se hace apenas ningún control de errores*. El programa se ha creado con fines didácticos.


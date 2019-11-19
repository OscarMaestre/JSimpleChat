
package io.github.oscarmaestre.jsimplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    int PUERTO_ESCUCHA;

    public Servidor(int PUERTO_ESCUCHA) {
        this.PUERTO_ESCUCHA = PUERTO_ESCUCHA;
    }
    
    public void servir() throws IOException{
        
        ServerSocket socketServidor;
        socketServidor=
                new ServerSocket(this.PUERTO_ESCUCHA);
        DistribuidorMensajes distribuidor;
        distribuidor=new DistribuidorMensajes();
        while (true){
            /*1. Esperar que llegue una petición*/
            Socket socketConCliente;
            socketConCliente=socketServidor.accept();
            Peticion p=new Peticion(distribuidor, 
                    socketConCliente);
            Thread hiloAsociado=new Thread(p);
            hiloAsociado.start();
            
        }
    }
    public static void main(String[] args) throws IOException{
        Servidor servidor=
            new Servidor(Constantes.PUERTO_SERVICIO);
        servidor.servir();
    }

}

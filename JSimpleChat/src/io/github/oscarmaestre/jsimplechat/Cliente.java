
package io.github.oscarmaestre.jsimplechat;

import io.github.oscarmaestre.jutilidades.Utilidades;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {

    
    private final int    puerto                 =Constantes.PUERTO_SERVICIO;
    /* Si en 30 segundos no se lee nada,todo se cierra*/
    private final int MAXIMOS_MSG_SIN_ACTIVIDAD = 30000;
    public void chatear(String ipServidor) throws IOException, InterruptedException{
        Socket conexion;
        
        InetSocketAddress direccionServidor;
        direccionServidor=
           new InetSocketAddress(ipServidor, puerto);
        conexion=new Socket();
        conexion.connect(direccionServidor);
        PrintWriter pw=
           Utilidades.getPrintWriter(
                   conexion.getOutputStream());
    
        BufferedReader bfr=
          Utilidades.getBufferedReader(
                  conexion.getInputStream()
            );
        
        EscritorMensajesRecibidos emr=new EscritorMensajesRecibidos(bfr);
        Thread hiloEscritorMensajes=new Thread(emr);
        hiloEscritorMensajes.start();
        
        conexion.setSoTimeout(MAXIMOS_MSG_SIN_ACTIVIDAD);
        
        Scanner scanner = new Scanner(System.in);
        String linea=scanner.nextLine();
        
        while (!linea.equals("/FIN!") && (!conexion.isClosed()) ){
            pw.println(linea);
            pw.flush();
            linea=scanner.nextLine();
            if ( conexion.isClosed()  || (bfr==null) ){
                System.out.println("La conexion se ha cerrado por parte del servidor");
                break;
            }
            
        }
        /*Si llegamos aquí hay que cerrar todo*/
        /*Primero nos aseguramos de enviar el aviso al servidor*/
        pw.println("/FIN!");
        
        /*Y cerramos*/
        pw.flush();
        pw.close();
        conexion.close();
        bfr.close();
        pw.close();
    }
    
    
    /* Se puede pasar la IP de un servidor de chat por medio
    de la linea de comandos. Si no se pasa nada, se asume que
    se va a conectar con un servidor en la misma máquina*/
    
    public static void main(String[] args) throws IOException, InterruptedException{
        String ipServidor="127.0.0.1";
        Cliente c=new Cliente();
        System.out.println(args);
        if (args.length!=0){
            ipServidor=args[0];
            System.out.println(ipServidor);
        } else {
            System.out.println("No has pasado la IP de ningún servidor");
        }
        System.out.println("Intentando conectar con;"+ipServidor);
        c.chatear(ipServidor);
    }
}

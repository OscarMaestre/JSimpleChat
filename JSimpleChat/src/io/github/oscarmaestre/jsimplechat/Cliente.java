
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

    
    final int    puerto    =Constantes.PUERTO_SERVICIO;
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
        
        Scanner scanner = new Scanner(System.in);
        String linea=scanner.nextLine();
        while (!linea.equals("/fin")){
            pw.println(linea);
            pw.flush();
            linea=scanner.nextLine();
        }
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

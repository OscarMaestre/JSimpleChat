
package io.github.oscarmaestre.jsimplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Peticion implements Runnable{
    private Socket socketConCliente;
    private BufferedReader bfr;
    private PrintWriter pw;
    
    private boolean flujosConstruidos=false;
    private boolean parametrosLeidos=false;
    
    private DistribuidorMensajes distribuidor;
    private ClienteConectado cliente;
    
    public Peticion(DistribuidorMensajes d,Socket socketCliente) throws IOException {
        this.socketConCliente = socketCliente;
        this.distribuidor=d;
        this.cliente=new ClienteConectado(socketCliente);
        d.anadirReceptor(cliente);
    }     
    
    
    
    private void construirFlujos(){
        try {
            InputStream is = 
                    this.socketConCliente.getInputStream();
            OutputStream os=
                    this.socketConCliente.getOutputStream();
            this.bfr=Utilidades.getBufferedReader(is);
            this.pw =Utilidades.getPrintWriter(os);
        } catch (IOException ex) {
            this.flujosConstruidos=false;
            return ;
        }
        /* Si llegamos aquí es porque ha sido
        posible construir los flujos */
        this.flujosConstruidos=true;
    }
    private Mensaje leerMensaje() throws IOException{
        String linea=this.bfr.readLine();
        Mensaje mensaje=new Mensaje(linea);
        return mensaje;
        
    }
    private void mostrarErrorFlujos(){
        System.out.println("No fue posible "
         + "construir los flujos debido quizá a "
         + "un error de red. Conexión terminada.");
        return ;
    }
    
    
    private void mostrarErrorLectura(){
        System.out.println("Conexion cerrada por:"+
                this.cliente.getDireccionIP());
        this.distribuidor.eliminarReceptor(cliente);
    }
    
    private void mostrarErrorMensaje(String mensaje){
        InetAddress direccion = this.socketConCliente.getInetAddress();
        String ip = direccion.getHostAddress();
        System.out.println("La dirección IP:"+ip+" ha enviado"
                + "un mensaje erroneo. ");
        System.out.println("El mensaje fue--->"+mensaje);
    }
    
    
    /*Intenta enviar un mensaje. Si no se envía el error suele
    ser fatal, se recomienda salir */
    private boolean enviarMensaje(Mensaje mensajeRecibido){
        
        Mensaje.TipoMensaje tipoMensaje = 
                mensajeRecibido.getTipoMensaje();
        if (tipoMensaje ==  Mensaje.TipoMensaje.MENSAJE_ERRONEO){
            this.mostrarErrorMensaje(mensajeRecibido.getCadenaCompletaRecibida());
            return false; /*El mensaje NO SE HA ENVIADO*/
        }/*Fin del if para mensajes erroneos*/        
        
        
        /** Si es un mensaje público...*/
        if (tipoMensaje==Mensaje.TipoMensaje.MENSAJE_PUBLICO){
            String mensaje;
            mensaje=this.cliente.getNick()+">"+mensajeRecibido.getTextoMensaje();
            this.distribuidor.difundirMensaje(mensaje);
            return true;/*EXITO*/
        } /* Fin del if para mensajes públicos*/
        
        /** Si es un mensaje privado...*/
        if (tipoMensaje==Mensaje.TipoMensaje.MENSAJE_PRIVADO){
            String nickDestinatario=mensajeRecibido.getNickEstablecido();
            String textoMensaje=mensajeRecibido.getTextoMensaje();
            this.distribuidor.enviarMensajeACliente(
                    nickDestinatario,textoMensaje);
            return true;
        }/* Fin del if para mensajes privados*/


        /** Si es un mensaje para cambiar el nick...*/
        if (tipoMensaje==Mensaje.TipoMensaje.ESTABLECIMIENTO_NICK){
            String nickEstablecido;
            nickEstablecido = mensajeRecibido.getNickEstablecido();
            this.cliente.setNick(nickEstablecido);
            System.out.println("Nuevo nick:"+nickEstablecido);
            this.distribuidor.difundirMensaje(
                    "Nuevo nick en la sala:"+nickEstablecido);
            return true;
        } /* Fin del if para mensajes de tipo NICK*/
        
        
        /* Si llegamos aquí, ha habido algun error*/
        return false;
        
    }
    @Override
    public void run() {
        this.construirFlujos();
        if (!this.flujosConstruidos){
            this.mostrarErrorFlujos();
            return ;
        }
        while (true){
            try {
                Mensaje mensajeRecibido=
                this.leerMensaje();
                boolean exito=this.enviarMensaje(mensajeRecibido);
            } catch (IOException ex) {
                /*Si llegamos aquí es porque
                ha habido un fallo de lectura. Salimos... */
                this.mostrarErrorLectura();
                return;
            } /*Fin del try/catch*/
        } /*Fin del bucle infinito*/
    } /*Fin del método run*/
}

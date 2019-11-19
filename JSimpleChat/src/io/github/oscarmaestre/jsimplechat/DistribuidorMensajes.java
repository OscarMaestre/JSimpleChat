/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.oscarmaestre.jsimplechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author ogomez
 */
public class DistribuidorMensajes {
    private ArrayList<ClienteConectado> direcciones;
    public DistribuidorMensajes(){
        this.direcciones=new ArrayList<>();
    }
    public void anadirReceptor(ClienteConectado c) throws IOException{
        this.direcciones.add(c);
    }
    public void eliminarReceptor(ClienteConectado c){
        this.direcciones.remove(c);
    }
    public void difundirMensaje(String mensaje){
        for (ClienteConectado c:this.direcciones){
            c.enviarMensaje(mensaje);
        }
    }
    
    public ClienteConectado getCliente(String nick){
        System.out.println("Buscando a "+nick);
        for (ClienteConectado c:this.direcciones){
            System.out.println("Revisando nick:"+c.getNick());
            if (nick.equals(c.getNick())){
                return c;
            }
        }
        return null;
    }
    
    public void enviarMensajeACliente(String nick, String mensaje){
        ClienteConectado cliente = this.getCliente(nick);
        if (cliente!=null){
            cliente.enviarMensaje(mensaje);
        } else {
            System.out.println("Posible error!");
            System.out.println("Se ha pedido enviar un mensaje "+
                    "al nick "+nick+" pero no existe");
        }
    }
}

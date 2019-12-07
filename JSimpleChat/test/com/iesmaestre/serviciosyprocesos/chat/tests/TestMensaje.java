/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iesmaestre.serviciosyprocesos.chat.tests;

import io.github.oscarmaestre.jsimplechat.Mensaje;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogomez
 */
public class TestMensaje {
    
    @Test
    public void testMensajeNick(){
        String tipo="/NICK";
        String textoMensaje="minick";
        Mensaje m=new Mensaje(tipo+" "+textoMensaje);
        Assert.assertEquals(Mensaje.TipoMensaje.ESTABLECIMIENTO_NICK,
                m.getTipoMensaje());
        
    }
    
    @Test
    public void testMensajeNickConEspacios(){
        String tipo="/NICK";
        String nick="minick";
        Mensaje m=new Mensaje(tipo+" "+nick);
        
        Assert.assertEquals(nick, m.getNickEstablecido());    
    }
    
    @Test
    public void testMensajeNickErroneo(){
        String tipo="/NICK";
        /*Construimos un mensaje claramente incorrecto, en el que falta
        el nick  */
        Mensaje m=new Mensaje(tipo);
        
        Assert.assertEquals(Mensaje.TipoMensaje.MENSAJE_ERRONEO, 
                m.getTipoMensaje());
    }
    
    
    
    @Test
    public void testMensajePublico(){
        String tipo="/PUBL";
        String textoMensaje="Hola mundo";
        Mensaje m=new Mensaje(tipo+" "+textoMensaje);
        Assert.assertEquals(Mensaje.TipoMensaje.MENSAJE_PUBLICO,
                m.getTipoMensaje());
        
    }
    
    @Test
    public void testMensajePublicoErroneo(){
        String tipo="/PUBL";
        /*Construimos un mensaje claramente erróneo, no hay texto*/
        Mensaje m=new Mensaje(tipo);
        Assert.assertEquals(Mensaje.TipoMensaje.MENSAJE_ERRONEO,
                m.getTipoMensaje());
        System.out.println("Mensaje:"+m.getTextoMensaje());
        
    }
    
    @Test
    public void testMensajePrivado(){
        String tipo="/PRIV";
        String nickDestinatario="pepito";
        String textoMensaje="Hola mundo";
        Mensaje m=new Mensaje(tipo+" "+nickDestinatario+" "+textoMensaje);
        Assert.assertEquals(Mensaje.TipoMensaje.MENSAJE_PRIVADO,
                m.getTipoMensaje());
        
        Assert.assertEquals(textoMensaje, m.getTextoMensaje());
        
    }
    
    
    
    
}

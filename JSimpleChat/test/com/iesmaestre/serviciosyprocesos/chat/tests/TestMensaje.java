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
    public void testMensajeErroneo(){
        String tipo="/NICK";
        String textoMensaje="Hola mundo";
        Mensaje m=new Mensaje(tipo+" "+textoMensaje);
        Assert.assertEquals(Mensaje.TipoMensaje.ESTABLECIMIENTO_NICK,
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

package io.github.oscarmaestre.jsimplechat;

import java.util.Random;

public class Mensaje {

    

    public enum TipoMensaje{
        ESTABLECIMIENTO_NICK, MENSAJE_PUBLICO, 
        MENSAJE_PRIVADO, MENSAJE_FIN_CONEXION;
    }
    private final String NICK       =   "NICK";
    private final String PUBLICO    =   "PUBL";
    private final String PRIVADO    =   "PRIV";
    
    private final String FINAL      =   "FIN!";
    
    private TipoMensaje         tipoMensaje;
    private String              destinatarioMensaje;
    private String              textoMensaje;
    private String              nickEstablecido;
    
    private final String        cadenaCompletaRecibida;
    
    private boolean erroneo;
    
    
    /* Se asume que todo mensaje tiene la misma estructura
       /NICK pepito ----->Para cambiar el nick
       /PUBL Hola gente-->Para enviar un mensaje a todo el mundo
       /PRIV pepito ¿Qué tal?-->Para enviar un mensaje privado a alguien
    
     Cualquier desviación de este protocolo se asume como un error
    */
    public Mensaje(String cadenaRecibida){
        this.cadenaCompletaRecibida=cadenaRecibida;
        this.erroneo=false;
        /*Primero miramos el primer caracter*/
        if (esMensajeConEstructuraErronea(cadenaRecibida)){
            this.erroneo=true;
            return;
        }
        
        
        /*Ahora examinamos los caracteres 1-4 para ver si es
        un NICK, un PUBL un PRIV o un FIN!*/
        String tipoMensajeRecibido=cadenaRecibida.substring(1,5);

        /*Comprobando si es un /FIN!*/
        if (tipoMensajeRecibido.equals(FINAL)){
            System.out.println("Detectado final!");
            this.tipoMensaje            =   TipoMensaje.MENSAJE_FIN_CONEXION;
            this.destinatarioMensaje    =   null;
            this.nickEstablecido        =   "";
            return ;
        }
        /*Comprobando si es un /NICK*/
        if (tipoMensajeRecibido.equals(NICK)){
            this.destinatarioMensaje    =null;
            this.nickEstablecido        =this.getRestoMensajeNick(
                    cadenaRecibida);
            this.tipoMensaje            =TipoMensaje.ESTABLECIMIENTO_NICK;
            return ;
        } /*Fin del if*/
        
        /*Comprobando si es un /PUBL*/
        if (tipoMensajeRecibido.equals(PUBLICO)){
            this.destinatarioMensaje    =null;
            this.textoMensaje           =this.getRestoMensaje(
                    cadenaRecibida);
            this.tipoMensaje            =TipoMensaje.MENSAJE_PUBLICO;
            return ;
        } /*Fin del if*/
        
        
        if (tipoMensajeRecibido.equals(PRIVADO)){
            this.rellenarCamposMensajePrivado(cadenaRecibida);
            this.tipoMensaje            =TipoMensaje.MENSAJE_PRIVADO;
            return ;
        }
        
        /* Si se llega aquí es porque no hemos encontrado
        un mensaje con una estructura válida. Marcamos
        el error y salimos*/
        this.erroneo=true;
        
    }
    
    private String getRestoMensajeNick(String cadenaRecibida){
        /*En este punto no sabemos si despues aparece un nick o no, así
        que primero lo comprobamos*/
        if (cadenaRecibida.length()<6){
            /*En este punto el usuario ha enviado un mensaje correcto
            pero no ha enviado un nick, así que inventamos uno */
            return this.inventarNick();
        }
        return cadenaRecibida.substring(6, cadenaRecibida.length());
    }
    
    private String getRestoMensaje(String cadenaRecibida){
        String resto=cadenaRecibida.substring(6, cadenaRecibida.length());
        if (resto.equals("")){
            this.erroneo=false;
        }
        return resto;
    }
    
    private void rellenarCamposMensajePrivado(String cadenaRecibida){
        /*Troceamos el mensaje*/
        String[] trozos = cadenaRecibida.split(" ");
        if (trozos.length<3){
            this.erroneo=true;
            return ;
        }
        System.out.println("Trozos en mensaje privado:"+trozos.length);
        /*El elemento 0 es el mensaje y el 1 el nick destinatario*/
        this.destinatarioMensaje=trozos[1]; 
        System.out.println("El destinario del privado es:>>"+this.destinatarioMensaje+"<<");
        if (this.destinatarioMensaje.equals("")){
            this.erroneo=true;
        }
        /*Y extraemos el trozo de cadena que va más allá
        del tipo de mensaje y del nick. Añadimos 1 por la /,
        1 por el espacio entre /PRIV y el nick y otro
        1 por el espacio entre el nick y el mensaje*/
        int posMensaje=1+PRIVADO.length()+1+destinatarioMensaje.length()+1;
        System.out.println("El texto privado empieza en "+posMensaje+" dentro de >"+
                cadenaRecibida);
        try{
            this.textoMensaje=
                cadenaRecibida.substring(posMensaje, 
                        cadenaRecibida.length());
            System.out.println("El texto privado es:>"+this.textoMensaje+"<");
        }
        catch ( StringIndexOutOfBoundsException e){
            this.erroneo=true;
        }
    }
    
    private boolean esMensajeConEstructuraErronea(String cadenaRecibida) {
        final boolean MENSAJE_ES_ERRONEO=true;
        final boolean MENSAJE_PARECE_CORRECTO=false;
        
        if (cadenaRecibida.equals("")){
            return MENSAJE_ES_ERRONEO;
        }
        /*Si no empieza por / el mensaje sí es erroneo*/
        if (cadenaRecibida.charAt(0)!='/'){
            return MENSAJE_ES_ERRONEO; 
        }
        /*Se comprueba si es un mensaje de FIN,que es un caso especial*/
        String posibleFIN=cadenaRecibida.substring(1,5);
        if (posibleFIN.equals(FINAL)){
            return MENSAJE_PARECE_CORRECTO;
        }
        
        /* Resto de casos*/
        
        
        /*Dado nuestro protocolo, cualquier mensaje 
        debe tener como mínimo 6 caracteres*/
        if (cadenaRecibida.length()<6){
            
            return MENSAJE_ES_ERRONEO;
        }
        
        /* El simbolo 5 DEBE SER SIEMPRE un espacio*/
        if (cadenaRecibida.charAt(5)!=' '){
            return MENSAJE_ES_ERRONEO;
        }
        
        /*Si llegamos aquí parece estar todo bien*/
        return MENSAJE_PARECE_CORRECTO;
    }



    private String inventarNick() {
        String prefijo="Usuario";
        Random r=new Random();
        int numAzar = 50+r.nextInt(100);
        return prefijo+numAzar;
    }
    /*Getters*/

    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    public String getDestinatarioMensaje() {
        return destinatarioMensaje;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public String getNickEstablecido() {
        return nickEstablecido;
    }
    
    public String getCadenaCompletaRecibida() {
        return cadenaCompletaRecibida;
    }
    
    
    
    /*Fin de los getters*/

    public boolean isErroneo() {
        return erroneo;
    }

    
}

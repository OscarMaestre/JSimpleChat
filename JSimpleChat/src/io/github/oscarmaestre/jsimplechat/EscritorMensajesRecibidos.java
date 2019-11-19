package io.github.oscarmaestre.jsimplechat;


import java.io.BufferedReader;
import java.io.IOException;


public class EscritorMensajesRecibidos implements Runnable{
    private BufferedReader bfr;

    public EscritorMensajesRecibidos(BufferedReader bfr) {
        this.bfr = bfr;
    }
    
    
    @Override
    public void run() {
        try {
            String lineaLeida=bfr.readLine();
            while (lineaLeida!=null){
                System.out.println(lineaLeida);
                lineaLeida=bfr.readLine();
            }
        } catch (IOException ex) {
            System.out.println("Chat cerrado. Gracias por su tiempo");
        }
    }
}
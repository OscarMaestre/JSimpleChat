
package io.github.oscarmaestre.jsimplechat;

import io.github.oscarmaestre.jutilidades.Utilidades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteConectado {
    private final Socket socket;
    private final PrintWriter pw;
    private final BufferedReader bfr;
    private String nick;
    private String direccionIP;
    public ClienteConectado(Socket s) throws IOException{
        this.socket=s;
        this.pw=Utilidades.getPrintWriter(s.getOutputStream());
        this.bfr=Utilidades.getBufferedReader(s.getInputStream());
        this.direccionIP=this.socket.getInetAddress().getHostAddress();
    }
    
    public void enviarMensaje(String mensaje){
        this.pw.println(mensaje);
        this.pw.flush();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    
    public String getDireccionIP(){
        return this.direccionIP;
    }
    
    
    
    
}

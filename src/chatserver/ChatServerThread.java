package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServerThread extends Thread{
    
    private int id;
    private final Socket socket;
    private DataInputStream flujoE;
    private DataOutputStream flujoS;
    private boolean run = true;
    private String nombre ="";
    private ChatServer server;

    public ChatServerThread(ChatServer server, Socket servidor) {
        this.socket = servidor;
        this.server = server;
        
        try {
            flujoE = new DataInputStream(servidor.getInputStream());
            flujoS = new DataOutputStream(servidor.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ChatServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        String text;
        while(run){
            try {
                text = flujoE.readUTF();
                
                if(nombre.equals("")){
                    setNombre(text);
                }else{
                    if(text.contains("//")){
                        String[] cadena = text.split("//");
                        String usuario = cadena[0];
                        String mensaje = cadena[1];
                        server.mensajesPrivados(mensaje + "\n", usuario, nombre);
                    }else{
                        server.broadcast("[" + nombre + "]: " + text + "\n");
                    }
                }
                
            } catch (IOException ex) {
                server.broadcast(nombre + " se ha ido de la sesion");
                cerrarServer();
            }
            
        }
    }
    
    public void send(String text){
        try {
                flujoS.writeUTF(text);
                flujoS.flush();//siempre despues de un write para limpiar el buffer
            } catch (IOException ex) {
                Logger.getLogger(ChatServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void setId(int id){
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
    private void cerrarServer(){
        try{
            run = false;
            flujoE.close();
            flujoS.close();
            socket.close();
        }catch(IOException ex){
            
        }
    }
    
    
    
}

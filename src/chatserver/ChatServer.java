package chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ChatServer {

    private ServerSocket servicio;
    private boolean run = true;
    private List<ChatServerThread> serverThreads = new ArrayList<>();
    ChatServerThread serverthread;
    String usuario;

    public ChatServer(int port) {

        try {

            servicio = new ServerSocket(port);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void startService() {

        Thread hebraprincipal = new Thread() {
            @Override
            public void run() {
                Socket servidor;

                while (run) {
                    try {
                        servidor = servicio.accept();
                        serverthread = new ChatServerThread(ChatServer.this, servidor);
                        serverThreads.add(serverthread);
                        serverthread.setId(serverThreads.indexOf(serverthread));
                        
                        listarClientes();
                        serverthread.start();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                        
                    }
                }

            }

        };

        hebraprincipal.start();

    }

    public void listarClientes() {
        String usuario = JOptionPane.showInputDialog("Introduzca un nombre de usuario:");
        broadcast(usuario + " se ha unido al servidor" + "\n");
        serverthread.setNombre(usuario);
        
    }

    public void broadcast(String text) {
        for (ChatServerThread client : serverThreads) {
            client.send(text);
        }
    }

    public void mensajesPrivados(String text, String usuario, String nombre) {
        for (ChatServerThread cliente : serverThreads) {
            if (cliente.getNombre().equalsIgnoreCase(usuario)) {
                cliente.send("[" + nombre + "] Mensaje privado: " + text);
            }
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(5000);
        chatServer.startService();
    }

}

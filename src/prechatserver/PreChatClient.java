package prechatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PreChatClient {
    
    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("localhost", 5000);
            //cliente.getLocalPort()
            System.out.println("puerto: " + cliente.getLocalPort());
            DataInputStream flujoE = new DataInputStream(cliente.getInputStream());
            DataOutputStream flujoS = new DataOutputStream(cliente.getOutputStream());
            String valor = flujoE.readUTF();
            System.out.println("cliente recibe: "+valor);
            flujoS.writeUTF("adios servidor");
            flujoE.close();
            flujoS.close();
            cliente.close();
        } catch (IOException ex) {
        }
    }

}
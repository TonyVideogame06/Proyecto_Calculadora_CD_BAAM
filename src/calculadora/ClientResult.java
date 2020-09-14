package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ClientResult implements Runnable {
    DataInputStream inNewSocket;
    DataOutputStream outNewSocket;
    String result = "";

    //Arrancamos con el hilo
    @Override
    public void run() {
        try {
                ServerSocket resultServer = new ServerSocket(4998);
                Socket newResult = resultServer.accept();
                inNewSocket =new DataInputStream(newResult.getInputStream());
                outNewSocket = new DataOutputStream(newResult.getOutputStream());
                result = inNewSocket.readUTF();
                System.out.println(result);
                newResult.close();

                } catch (IOException ex) {
                    System.out.println("ERROR");
                }


            }


}

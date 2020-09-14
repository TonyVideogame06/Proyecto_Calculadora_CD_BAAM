package calculadora;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeHandler implements Runnable {
    public ServerSocket nodeHandlerServerSocket;
    int initialPort;

    public NodeHandler(int nodeNumber, int original) {
        System.out.println("Node is listenning at port " + original);
        try {
            this.nodeHandlerServerSocket = new ServerSocket(nodeNumber);
        } catch (IOException ex) {
            Logger.getLogger(NodeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.initialPort = original;
    }

    @Override
    public void run() {
        while(true)
        {
            Socket nodeSocket;
            String message;
            try{
                nodeSocket = nodeHandlerServerSocket.accept();
                DataInputStream inNodeSocket = new DataInputStream(nodeSocket.getInputStream());
                DataOutputStream outNodeSocket = new DataOutputStream(nodeSocket.getOutputStream());
                //message = in.readUTF();

                //System.out.println(message);
                nodeSocket.close();

            } catch (IOException ex) {
                System.out.println("Connection failed");
            }

        }
    }
}

package calculadora;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{

    public ServerSocket listenClient; //Server para escuchar cliente
    int puerto_I;
    int Cliente;

    public ClientHandler(int Num_Nodo, int puerto_I, int Cliente) {
        try {
            this.listenClient = new ServerSocket(Num_Nodo);
        } catch (IOException ex) {
            Logger.getLogger(NodeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.puerto_I = puerto_I;
        this.Cliente = Cliente;
    }

    @Override
    public void run() {
        System.out.println("Listen client: "+ Cliente);
        while(true)
        {
            Socket clientSocket;
            String message="";
            try{
                System.out.println("Connection with: "+ listenClient.getLocalPort());
                clientSocket = listenClient.accept();
                DataInputStream inClientSocket = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream outClientSocket = new DataOutputStream(clientSocket.getOutputStream());
                message = inClientSocket.readUTF();
                System.out.println(message);
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println("Fail Connection");
            }
            String Nodes ="";
            try {
                Socket askNodes = new Socket("127.0.0.1",puerto_I);
                DataInputStream inAskNodes = new DataInputStream(askNodes.getInputStream());
                DataOutputStream outAskNodes = new DataOutputStream(askNodes.getOutputStream());

                outAskNodes.writeUTF(message);
                Nodes = inAskNodes.readUTF();
                System.out.println(Nodes);
                askNodes.close();

            } catch (IOException ex) {
                System.out.println("Fail in the connection");
            }
            String[] Node = Nodes.split(" ");
            for (int  i = 0; i < Node.length;i++)
            {
                try {
                    Socket sendNodes = new Socket("127.0.0.1",Integer.parseInt(Node[i]));
                    DataOutputStream outSendNodes = new DataOutputStream(sendNodes.getOutputStream());

                    outSendNodes.writeUTF(message);
                    sendNodes.close();

                } catch (IOException ex) {
                    System.out.println("Fail in the connection");
                }

            }
        }
    }

}

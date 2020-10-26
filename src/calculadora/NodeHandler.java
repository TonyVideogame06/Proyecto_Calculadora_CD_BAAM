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
    int anotherPortNode;

    public NodeHandler(int nodeNumber, int original, int anotherNodePort) {
        System.out.println("Node is listening at port " + original);
        try {
            this.nodeHandlerServerSocket = new ServerSocket(nodeNumber);
        } catch (IOException ex) {
            Logger.getLogger(NodeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.initialPort = original;
        this.anotherPortNode = anotherNodePort;
    }


    @Override
    public void run() {
        System.out.println("Me crearon para escuchar a " + anotherPortNode);
        while (true) {
            Socket nodeSocket;
            String message = "";
            try {
                System.out.println("Conectanto a server socket: " + nodeHandlerServerSocket.getLocalPort());
                nodeSocket = nodeHandlerServerSocket.accept();
                DataInputStream inNodeSocket = new DataInputStream(nodeSocket.getInputStream());
                DataOutputStream outNodeSocket = new DataOutputStream(nodeSocket.getOutputStream());
                message = inNodeSocket.readUTF();
                System.out.println("Algo se trata de comunicar "+ message);
                nodeSocket.close();

            } catch (IOException ex) {
                System.out.println("Fallo la conexion");
            }
            String[] identify = message.split(" ");
            if (Integer.parseInt(identify[0]) >= 5) //lego un resultado
            {
                System.out.println("Se conecto un servidor");
                String serverNodes = "";
                try {
                    Socket requestNodes = new Socket("127.0.0.1", initialPort);
                    DataInputStream inRequestNodes = new DataInputStream(requestNodes.getInputStream());
                    DataOutputStream outRequestNodes = new DataOutputStream(requestNodes.getOutputStream());

                    outRequestNodes.writeUTF(message);
                    serverNodes = inRequestNodes.readUTF();
                    System.out.println(serverNodes);
                    requestNodes.close();

                } catch (IOException ex) {
                    System.out.println("Fallo, error en la conexion");
                }
                String[] serverNodeSplit = serverNodes.split(" ");
                for (int i = 0; i < serverNodeSplit.length; i++) {
                    if (serverNodeSplit[i].indexOf("6") != -1) {
                        try {
                            Socket sendMessageServer = new Socket("127.0.0.1", Integer.parseInt(serverNodeSplit[i]));
                            DataOutputStream outSendMessageServer = new DataOutputStream(sendMessageServer.getOutputStream());

                            outSendMessageServer.writeUTF(message);
                            sendMessageServer.close();

                        } catch (IOException ex) {
                            System.out.println("Fallo, error en la conexcion");
                        }
                    }
                }
            } else {
                System.out.println("Fue un Cliente");
                String clientNodes = "";
                try {
                    Socket askClientNodes = new Socket("127.0.0.1", initialPort);
                    DataInputStream inAskClientNodes  = new DataInputStream(askClientNodes.getInputStream());
                    DataOutputStream outAskClientNodes  = new DataOutputStream(askClientNodes.getOutputStream());

                    outAskClientNodes .writeUTF(message);
                    clientNodes = inAskClientNodes .readUTF();
                    System.out.println(clientNodes);
                    askClientNodes.close();

                } catch (IOException ex) {
                    System.out.println("No se pudo conectar al socket");
                }
                String[] clientNodesSplit = clientNodes.split(" ");
                String serverPort = "" + (initialPort + 199);
                for (int i = 0; i < clientNodesSplit.length; i++) {
                    System.out.println(clientNodesSplit[i]);
                    if (clientNodesSplit[i].indexOf(serverPort) != -1) {
                        try {
                            Socket sendMessageClient = new Socket("127.0.0.1", Integer.parseInt(clientNodesSplit[i]));
                            DataOutputStream outSendMessageClient = new DataOutputStream(sendMessageClient.getOutputStream());

                            outSendMessageClient.writeUTF(message);
                            sendMessageClient.close();

                        } catch (IOException ex) {
                            System.out.println("No se pudo conectar al socket");
                        }
                    }
                }
            }

        }
    }
}

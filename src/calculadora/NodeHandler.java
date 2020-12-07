package calculadora;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeHandler implements Runnable{

    public ServerSocket nodeHandlerServerSocket;
    int initialPort;
    int anotherPortNode;

    public NodeHandler(int nodeNumber, int original, int anotherNodePort) {
        System.out.println(original);
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
        System.out.println("Create to listen:" + anotherPortNode);
        while(true)
        {
            Socket nodeSocket;
            String message="";
            try{
                System.out.println("Connection with: "+ nodeHandlerServerSocket.getLocalPort());
                nodeSocket = nodeHandlerServerSocket.accept();
                DataInputStream inNodeSocket = new DataInputStream(nodeSocket.getInputStream());
                DataOutputStream outNodeSocket = new DataOutputStream(nodeSocket.getOutputStream());
                message = inNodeSocket.readUTF();
                nodeSocket.close();

            } catch (IOException ex) {
                System.out.println("Fail Connection");
            }
            String[] identify = message.split(" ");
            if(Integer.parseInt(identify[0])>=5) //Llego Resultado
            {
                String Nodes ="";
                try {
                    Socket askForNodes = new Socket("127.0.0.1", initialPort);
                    DataInputStream inAskForNodes = new DataInputStream(askForNodes.getInputStream());
                    DataOutputStream outAskForNodes = new DataOutputStream(askForNodes.getOutputStream());

                    outAskForNodes.writeUTF(message);
                    Nodes = inAskForNodes.readUTF();
                    askForNodes.close();

                } catch (IOException ex) {
                    System.out.println("Fail in the connection");
                }
                String[] Node = Nodes.split(" ");
                for(int i=0; i< Node.length;i++)
                {
                    if(Integer.parseInt(Node[i])>=(initialPort +190) || Integer.parseInt(Node[i]) <7000 )
                    {
                        System.out.println(Node[i]);
                        try {
                            Socket Send = new Socket("127.0.0.1",Integer.parseInt(Node[i]));
                            DataOutputStream outSend = new DataOutputStream(Send.getOutputStream());

                            outSend.writeUTF(message);
                            Send.close();

                        } catch (IOException ex) {
                            System.out.println("Fail in the connection");
                        }
                    }
                }
            }
            else
            {
                String anotherNodes ="";
                try {
                    Socket askForAnotherNodes = new Socket("127.0.0.1", initialPort);
                    DataInputStream inAskForAnotherNodes = new DataInputStream(askForAnotherNodes.getInputStream());
                    DataOutputStream outAskForAnotherNodes = new DataOutputStream(askForAnotherNodes.getOutputStream());

                    outAskForAnotherNodes.writeUTF(message);
                    anotherNodes = inAskForAnotherNodes.readUTF();
                    askForAnotherNodes.close();

                } catch (IOException ex) {
                    System.out.println("Fail in the connection");
                }
                String[] anotherNode = anotherNodes.split(" ");
                for(int i=0; i< anotherNode.length;i++)
                {
                    System.out.println(anotherNode[i]);
                    if(Integer.parseInt(anotherNode[i])>=(initialPort +190) && Integer.parseInt(anotherNode[i])<=(initialPort +197))
                    {
                        try {
                            Socket sendAnotherNode = new Socket("127.0.0.1",Integer.parseInt(anotherNode[i]));
                            DataOutputStream outSendAnotherNode = new DataOutputStream(sendAnotherNode.getOutputStream());

                            outSendAnotherNode.writeUTF(message);
                            sendAnotherNode.close();

                        } catch (IOException ex) {
                            System.out.println("Fail in the connection");
                        }
                    }
                }
            }

        }
    }
}

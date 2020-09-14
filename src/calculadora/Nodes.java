package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Nodes {
    static Vector<NodeHandler> nodeVector = new Vector<>();
    static ArrayList<Integer> lisPortsNodes = new ArrayList<>();
    //static ArrayList<Integer> lisPortsServers = new ArrayList<>();

    private static ServerSocket create(int firstPort) throws IOException {
        String host = "127.0.0.1";
        boolean flag = true;
        while(flag){
            if(firstPort < 6000){
                try {
                    return new ServerSocket(firstPort);
                } catch (IOException ex) {
                    Socket socketCreation = new Socket(host,firstPort);
                    DataInputStream inSocketCreation = new DataInputStream(socketCreation.getInputStream());
                    DataOutputStream out = new DataOutputStream(socketCreation.getOutputStream());
                    out.writeUTF("whoAmI");
                    String message = inSocketCreation.readUTF();
                    switch(message)
                    {
                        case "Node":
                            lisPortsNodes.add(firstPort);
                            break;
                        default:
                            System.out.println("Can't create new nodes. There are enough nodes connected");
                            break;
                    }
                    socketCreation.close();
                    firstPort=firstPort+100;
                    continue; // try next port
                }
            }else
            {
                flag = false;
            }
        }
        return null;
    }


    public static void main(String[] args) throws IOException {
        String aCalcular = "";
        String serverResult = "";
        String option = "";
        ServerSocket nodeServerSocket = create(5000);
        int localPort = nodeServerSocket.getLocalPort();
        String localHost ="127.0.0.1";
        int ports = 5000;
        while(localPort > ports)
        {
            try {
                Socket newNodeSocket = new Socket(localHost,ports);
                //DataInputStream inNewNodeSocket = new DataInputStream(newNodeSocket.getInputStream());
                DataOutputStream outNewNodeSocket = new DataOutputStream(newNodeSocket.getOutputStream());
                outNewNodeSocket.writeUTF("Node,"+localPort);
                newNodeSocket.close();
                ports = ports +100; //Brinca ports de nodos para que el siguiente este vacio
            }catch (IOException ex) {
                System.out.println("Warning: Can't connect with others nodes");
            }
        }
        for(int i = 0; i< lisPortsNodes.size(); i++) //Mandar mensaje a all nodos conectados
        {
            Socket enlazarNode = new Socket(localHost, lisPortsNodes.get(i));
            DataOutputStream outEnlazarNode = new DataOutputStream(enlazarNode.getOutputStream());
            outEnlazarNode.writeUTF("Node,"+localPort);
            enlazarNode.close();
        }
        while(true)
        {

            Socket universalNodeSocket;
            try {
                System.out.println("Listen port: "+localPort);
                universalNodeSocket = nodeServerSocket.accept();
                DataInputStream inUniversalNodeSocket = new DataInputStream(universalNodeSocket.getInputStream());
                DataOutputStream outUniversalNodeSocket = new DataOutputStream(universalNodeSocket.getOutputStream());
                String universalMessage = inUniversalNodeSocket.readUTF();
                System.out.println(universalMessage);
                String[] universalMessageSplit = universalMessage.split(",");
                option = universalMessageSplit[0];
                switch(option)
                {
                    case "Node":
                        lisPortsNodes.add(Integer.parseInt(universalMessageSplit[1]));
                        //NodeHandler newNode = new NodeHandler(localPort+lisPortsNodes.size(),localPort);
                        //Thread newNodeThread = new Thread(newNode);
                        //nodeVector.add(newNode);
                        //newNodeThread.start();
                        break;
                    case "Client":
                        aCalcular = universalMessage;
                        final int portServer = 6000;
                        DataInputStream inServer;
                        DataOutputStream outServer;
                        try {
                            System.out.println("Node "+ localPort + " connected to the server");
                            //Create the socket to connect with server
                            Socket servidorSocket = new Socket("127.0.0.1", portServer);

                            inServer = new DataInputStream(servidorSocket.getInputStream());
                            outServer = new DataOutputStream(servidorSocket.getOutputStream());

                            //Send one message to server
                            outServer.writeUTF(aCalcular);
                            serverResult = inServer.readUTF();
                            System.out.println(option);
                            System.out.println(serverResult);
                            outUniversalNodeSocket.writeUTF(serverResult);
                            servidorSocket.close();

                        } catch (IOException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Result":
                        serverResult = option;
                        final int portClient = 4998;
                        DataInputStream inClient;
                        DataOutputStream outClient;
                        try {
                            System.out.println("Node "+ localPort + " connected to the client");
                            //Create the socket to connect with server
                            Socket clientSocket = new Socket("127.0.0.1", portClient);

                            //inServer = new DataInputStream(clientSocket.getInputStream());
                            //outServer = new DataOutputStream(clientSocket.getOutputStream());

                            //Send one message to server
                            //outServer.writeUTF(aCalcular);
                            //serverResult = inServer.readUTF();
                            //System.out.println(serverResult);
                            outUniversalNodeSocket.writeUTF(serverResult);
                            clientSocket.close();

                        } catch (IOException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "whoAmI":
                        outUniversalNodeSocket.writeUTF("Node");
                        break;
                    default:
                        System.out.println("Can't identify node connection");
                        break;
                }

            } catch (IOException ex) {
                System.out.println("Connection failed");
            }
            System.out.println("Nodes that are listening on ports" + lisPortsNodes);
        }

    }

}

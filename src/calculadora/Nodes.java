package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Nodes {

    static Vector<NodeHandler> nodeVector = new Vector<>();
    static Vector<ClientHandler> clientVector = new Vector<>();
    static Vector<ServerHandler> serverVector = new Vector<>();
    static List<Integer> listPortsNodes = new ArrayList<>();
    //static List<Integer> listPortsServers = new ArrayList<>();

    static public ServerSocket create(int firstPort) throws IOException {
        String host = "127.0.0.1";
        var flag = true;
        while(flag){
            if(firstPort < 65000){
                try {
                    return new ServerSocket(firstPort);
                } catch (IOException ex) {
                    Socket socketCreation = new Socket(host,firstPort);
                    DataInputStream inSocketCreation = new DataInputStream(socketCreation.getInputStream());
                    DataOutputStream outSocketCreation = new DataOutputStream(socketCreation.getOutputStream());
                    outSocketCreation.writeUTF("whoAmI");
                    String message = inSocketCreation.readUTF();
                    switch(message)
                    {
                        case "0100": //Nodo
                            listPortsNodes.add(firstPort);
                            break;
                        default:
                            System.out.println("Este puerto es basura");
                    }
                    socketCreation.close();
                    firstPort=firstPort+200;
                    continue; // try next port
                }
            }else
            {
                flag=false;
            }
        }
        return null;
    }


    public static void main(String[] args) throws IOException {
        int client = -5;
        int server= -5;
        String option = "";
        ServerSocket nodeServerSocket = create(8000);
        int localPort = nodeServerSocket.getLocalPort();
        String localHost ="127.0.0.1";
        DataInputStream inNewNodeSocket;
        DataOutputStream outNewNodeSocket;
        int puertos = 8000;
        while(localPort > puertos)
        {
            try {
                Socket newNodeSocket = new Socket(localHost,puertos);
                inNewNodeSocket = new DataInputStream(newNodeSocket.getInputStream());
                outNewNodeSocket = new DataOutputStream(newNodeSocket.getOutputStream());
                outNewNodeSocket.writeUTF("0100 "+localPort);
                newNodeSocket.close();
                puertos = puertos +200;
            }catch (IOException ex) {
                System.out.println("Fallo, error en la conexcion para los demas");
            }
        }

        for(int i = 0; i< listPortsNodes.size(); i++)
        {
            NodeHandler newNodeThread = new NodeHandler(localPort+i+1,localPort, listPortsNodes.get(i)+ listPortsNodes.size());
            Thread NNT = new Thread(newNodeThread);
            nodeVector.add(newNodeThread);
            NNT.start();

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
                String[] universalMessageSplit = universalMessage.split(" ");
                option = universalMessageSplit[0];
                switch(option)
                {
                    case "0100": //Nodo
                        listPortsNodes.add(Integer.parseInt(universalMessageSplit[1]));
                        NodeHandler TreadNodeHandler = new NodeHandler(localPort+ listPortsNodes.size(),localPort,Integer.parseInt(universalMessageSplit[1]));
                        Thread TNH = new Thread(TreadNodeHandler);
                        nodeVector.add(TreadNodeHandler);
                        TNH.start();
                        break;
                    case "whoAmI":
                        outUniversalNodeSocket.writeUTF("0100");
                        break;
                    case "0001": //Cliente
                        if (client !=-5)
                        {
                            outUniversalNodeSocket.writeUTF("0");
                        }else
                        {
                            client = Integer.parseInt(universalMessageSplit[1]);
                            outUniversalNodeSocket.writeUTF(""+(localPort+198));
                            ClientHandler TreadClientHandler = new ClientHandler(localPort+198,localPort,Integer.parseInt(universalMessageSplit[1]));
                            Thread TCH = new Thread(TreadClientHandler);
                            clientVector.add(TreadClientHandler);
                            TCH.start();
                        }
                        break;
                    case "0010": //Servidor
                        if (server !=-5)
                        {
                            outUniversalNodeSocket.writeUTF("0");
                        }else
                        {
                            server = Integer.parseInt(universalMessageSplit[1]);
                            outUniversalNodeSocket.writeUTF(""+(localPort+199));
                            ServerHandler TreadServerHandler = new ServerHandler(localPort+199,localPort,Integer.parseInt(universalMessageSplit[1]));
                            Thread TSC = new Thread(TreadServerHandler);
                            serverVector.add(TreadServerHandler);
                            TSC.start();
                        }
                        break;
                    case "1000": //Suma
                        String A = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            A= A + (listPortsNodes.get(i)+i+1) + " ";
                        }
                        if (server != -5)
                        {
                            A= A + (localPort + 199);
                        }
                        System.out.println(A);
                        outUniversalNodeSocket.writeUTF(A);
                        break;
                    case "1001": //Resta
                        String B = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            B= B + (listPortsNodes.get(i)+i+1) + " ";
                        }
                        if (server != -5)
                        {
                            B= B + (localPort + 199);
                        }
                        System.out.println(B);
                        outUniversalNodeSocket.writeUTF(B);
                        break;
                    case "1100": //Multiplicacion
                        String C = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            C= C + (listPortsNodes.get(i)+i+1) + " ";
                        }
                        if (server != -5)
                        {
                            C= C + (localPort + 199);
                        }
                        System.out.println(C);
                        outUniversalNodeSocket.writeUTF(C);
                        break;
                    case "1101": //Division
                        String D = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            D= D + (listPortsNodes.get(i)+i+1) + " ";
                        }
                        if (server != -5)
                        {
                            D= D + (localPort + 199);
                        }
                        System.out.println(D);
                        outUniversalNodeSocket.writeUTF(D);
                        break;
                    case "0111": //Resultado de la Suma
                        System.out.println("Llego el resultado de la suma para el cliente " + client);
                        String E = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            E= E + listPortsNodes.get(i) + " ";
                        }
                        if (client != -5)
                        {
                            E= E + client;
                        }
                        System.out.println(E);
                        outUniversalNodeSocket.writeUTF(E);
                        break;
                    case "0110": //Resultado de la Resta
                        String F = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            F= F + (listPortsNodes.get(i)+i+1) + " ";
                        }
                        if (server != -5)
                        {
                            F= F + client;
                        }
                        System.out.println(F);
                        outUniversalNodeSocket.writeUTF(F);
                        break;
                    case "0011": //Resultado de la Multiplicacion
                        String G = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            G= G + (listPortsNodes.get(i)+i+1) + " ";
                        }
                        if (server != -5)
                        {
                            G= G + client;
                        }
                        System.out.println(G);
                        outUniversalNodeSocket.writeUTF(G);
                        break;
                    case "1010": //Resultado de la Division
                        String H = "";
                        for(int i = 0; i< listPortsNodes.size(); i++)
                        {
                            H= H + (listPortsNodes.get(i)+i+1) + " ";
                        }
                        if (server != -5)
                        {
                            H= H + client;
                        }
                        System.out.println(H);
                        outUniversalNodeSocket.writeUTF(H);
                        break;
                    default:
                        System.out.println("No se identifico");
                        break;
                }

            } catch (IOException ex) {
                System.out.println("Fallo la conexion");
            }
            System.out.println(listPortsNodes);
        }

    }



}

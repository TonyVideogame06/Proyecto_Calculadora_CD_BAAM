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
    static List<Integer> listPortNodes = new ArrayList<>();
    static List<Integer> listPortServers = new ArrayList<>();

    static public ServerSocket create(int firstPort) throws IOException {
        var flag = true;
        while(flag){
            if(firstPort < 65000){
                try {
                    return new ServerSocket(firstPort);
                } catch (IOException ex) {
                    Socket socketCreation = new Socket("127.0.0.1",firstPort);
                    DataInputStream inSocketCreation = new DataInputStream(socketCreation.getInputStream());
                    DataOutputStream outSocketCreation = new DataOutputStream(socketCreation.getOutputStream());
                    outSocketCreation.writeUTF("whoAmI");
                    String message = inSocketCreation.readUTF();
                    String[] messageSplit = message.split(" ");
                    switch(messageSplit[0])
                    {
                        case "Nodo":
                            listPortNodes.add(Integer.parseInt(messageSplit[1]));
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
        int server= 0;
        String option = "";
        ServerSocket nodePortServer = create(8000);
        int localPort = nodePortServer.getLocalPort();
        String localHost ="127.0.0.1";
        //DataInputStream inNewNodeSocket;
        DataOutputStream outNewNodeSocket;
        int puertos = 8000;
        int cont=1;
        while(localPort > puertos)
        {
            try {
                Socket newNodeSocket = new Socket(localHost,puertos);
                //inNewNodeSocket = new DataInputStream(newNodeSocket.getInputStream());
                outNewNodeSocket = new DataOutputStream(newNodeSocket.getOutputStream());
                outNewNodeSocket.writeUTF("Nodo "+(localPort+cont));
                newNodeSocket.close();
                puertos = puertos +200;
            }catch (IOException ex) {
                System.out.println("Fail in the connection with the others");
            }
            cont++;
        }

        for(int i = 0; i< listPortNodes.size(); i++)
        {
            NodeHandler newNodeThread = new NodeHandler(localPort+i+1,localPort, listPortNodes.get(i));
            Thread NNT = new Thread(newNodeThread);
            nodeVector.add(newNodeThread);
            NNT.start();

        }
        while(true)
        {
            Socket universalNodeSocket;
            try {
                System.out.println("Node connected");
                universalNodeSocket = nodePortServer.accept();
                DataInputStream inUniversalNodeSocket = new DataInputStream(universalNodeSocket.getInputStream());
                DataOutputStream outUniversalNodeSocket = new DataOutputStream(universalNodeSocket.getOutputStream());
                String universalMessage = inUniversalNodeSocket.readUTF();
                //System.out.println(universalMessage);
                String[] universalMessageSplit = universalMessage.split(" ");
                option = universalMessageSplit[0];
                switch(option)
                {
                    case "Nodo": //Nodo
                        listPortNodes.add(Integer.parseInt(universalMessageSplit[1]));
                        NodeHandler NuevoHilo1 = new NodeHandler(localPort+ listPortNodes.size(),localPort,Integer.parseInt(universalMessageSplit[1]));
                        Thread t1 = new Thread(NuevoHilo1);
                        nodeVector.add(NuevoHilo1);
                        t1.start();
                        break;
                    case "whoAmI": //Identificar nodo
                        outUniversalNodeSocket.writeUTF("Nodo "+(localPort+cont));
                        cont++;
                        break;
                    case "Cliente":
                        if (client !=-5)
                        {
                            outUniversalNodeSocket.writeUTF("0");
                        }else
                        {
                            client = Integer.parseInt(universalMessageSplit[1]);
                            outUniversalNodeSocket.writeUTF(""+(localPort+198));
                            ClientHandler Cliente = new ClientHandler(localPort+198,localPort,Integer.parseInt(universalMessageSplit[1]));
                            Thread tCl = new Thread(Cliente);
                            clientVector.add(Cliente);
                            tCl.start();
                        }
                        break;
                    case "Servidor":
                        if (server >= 5)
                        {
                            outUniversalNodeSocket.writeUTF("0");
                        }else
                        {
                            listPortServers.add(localPort+server+190);
                            outUniversalNodeSocket.writeUTF(""+(localPort+server+190));
                            ServerHandler Servidor = new ServerHandler(localPort+server+190,localPort,Integer.parseInt(universalMessageSplit[1]));
                            Thread tCl = new Thread(Servidor);
                            serverVector.add(Servidor);
                            tCl.start();
                            server++;
                        }
                        break;
                    case "0":
                        String X = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            X= X + listPortNodes.get(i) + " ";
                        }

                        if (server != 0)
                        {
                            for(int i = 0; i< listPortServers.size(); i++)
                            {
                                X= X + listPortServers.get(i) + " ";
                            }
                        }
                        outUniversalNodeSocket.writeUTF(X);
                        break;
                    case "1": //Suma
                        String A = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            A= A + listPortNodes.get(i) + " ";
                        }

                        if (server != 0)
                        {
                            for(int i = 0; i< listPortServers.size(); i++)
                            {
                                A= A + listPortServers.get(i) + " ";
                            }
                        }
                        outUniversalNodeSocket.writeUTF(A);
                        break;
                    case "2": //Resta
                        String B = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            B= B + listPortNodes.get(i) + " ";
                        }
                        if (server != 0)
                        {
                            for(int i = 0; i< listPortServers.size(); i++)
                            {
                                B= B + listPortServers.get(i) + " ";
                            }
                        }
                        outUniversalNodeSocket.writeUTF(B);
                        break;
                    case "3": //Multiplicacion
                        String C = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            C= C + listPortNodes.get(i) + " ";
                        }
                        if (server != 0)
                        {
                            for(int i = 0; i< listPortServers.size(); i++)
                            {
                                C= C + listPortServers.get(i) + " ";
                            }
                        }
                        outUniversalNodeSocket.writeUTF(C);
                        break;
                    case "4": //Division
                        String D = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            D= D + listPortNodes.get(i) + " ";
                        }
                        if (server != 0)
                        {
                            for(int i = 0; i< listPortServers.size(); i++)
                            {
                                D= D + listPortServers.get(i) + " ";
                            }
                        }
                        outUniversalNodeSocket.writeUTF(D);
                        break;
                    case "5": //Resultado Suma
                        String E = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            E= E + listPortNodes.get(i) + " ";
                        }
                        if (client != -5)
                        {
                            E= E + client;
                        }
                        outUniversalNodeSocket.writeUTF(E);
                        break;
                    case "6": //Resultado Resta
                        String F = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            F= F + listPortNodes.get(i) + " ";
                        }
                        if (client != -5)
                        {
                            F= F + client;
                        }
                        outUniversalNodeSocket.writeUTF(F);
                        break;
                    case "7": //Resultado Multiplicacion
                        String G = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            G= G + listPortNodes.get(i) + " ";
                        }
                        if (client != -5)
                        {
                            G= G + client;
                        }
                        outUniversalNodeSocket.writeUTF(G);
                        break;
                    case "8": //Resultado Division
                        String H = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            H= H + listPortNodes.get(i) + " ";
                        }
                        if (client != -5)
                        {
                            H= H + client;
                        }
                        outUniversalNodeSocket.writeUTF(H);
                        break;
                    case "9": //Enviar a cliente
                        int acliente = client;
                        switch(universalMessageSplit[2])
                        {
                            case "1":
                                acliente = client + 1;
                                break;
                            case "2":
                                acliente = client + 2;
                                break;
                            case "3":
                                acliente = client + 3;
                                break;
                            case "4":
                                acliente = client + 4;
                                break;
                        }
                        String I = "";
                        for(int i = 0; i< listPortNodes.size(); i++)
                        {
                            I= I + listPortNodes.get(i) + " ";
                        }
                        if (client != -5)
                        {
                            I= I + acliente;
                        }
                        outUniversalNodeSocket.writeUTF(I);
                        break;

                    default:
                        System.out.println("Can't identify who is");
                        break;
                }

            } catch (IOException ex) {
                System.out.println("Fail in the connection");
            }
        }

    }

}
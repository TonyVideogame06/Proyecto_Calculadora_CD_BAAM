package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler implements Runnable{

    public ServerSocket serverSocket;
    int puerto_I;
    int Servidor;

    public ServerHandler(int Num_Nodo, int puerto_I, int Servidor) {
        try {
            this.serverSocket = new ServerSocket(Num_Nodo);
        } catch (IOException ex) {
            System.out.println("Fallo en la conexion");
        }
        this.puerto_I = puerto_I;
        this.Servidor = Servidor;
    }

    @Override
    public void run() {
        System.out.println("Listen server: " +  Servidor);
        String message="";
        while(true)
        {
            Socket messageSocket;

            String Result ="";
            try{
                System.out.println("Connection with: "+ serverSocket.getLocalPort());
                messageSocket = serverSocket.accept();
                DataInputStream inMessageSocket = new DataInputStream(messageSocket.getInputStream());
                message = inMessageSocket.readUTF();
                System.out.println("Message from server: "+ message);
                messageSocket.close();
            } catch (IOException ex) {
                System.out.println("Fallo la conexion");
                continue;
            }
            System.out.println(message);
            if(Integer.parseInt(message.split(" ")[0]) < 5 && !message.split(" ")[0].equals("400"))
            {
                try {
                    Socket Operation = new Socket("127.0.0.1",Servidor);
                    DataOutputStream outOperation = new DataOutputStream(Operation.getOutputStream());
                    DataInputStream inOperation = new DataInputStream(Operation.getInputStream());
                    outOperation.writeUTF(message);
                    Result = inOperation.readUTF();
                    System.out.println(Result);
                    Operation.close();

                } catch (IOException ex) {
                    System.out.println("Fail in the connection");
                    continue;
                }
                String[] resultSplit = Result.split(" ");
                if(Integer.parseInt(resultSplit[0])!= 0)
                {
                    String Nodes ="";
                    try {
                        Socket askForNodes = new Socket("127.0.0.1",puerto_I);
                        DataInputStream inAskForNodes = new DataInputStream(askForNodes.getInputStream());
                        DataOutputStream outAskForNodes = new DataOutputStream(askForNodes.getOutputStream());

                        outAskForNodes.writeUTF(Result);
                        Nodes = inAskForNodes.readUTF();
                        askForNodes.close();

                    } catch (IOException ex) {
                        System.out.println("Fail in the connection");
                        continue;
                    }
                    String[] Node = Nodes.split(" ");
                    for (int  i = 0; i < Node.length;i++)
                    {
                        try {
                            Socket sendNode = new Socket("127.0.0.1",Integer.parseInt(Node[i]));
                            DataOutputStream outSendNode = new DataOutputStream(sendNode.getOutputStream());
                            System.out.println(Node[i]);
                            outSendNode.writeUTF(Result);
                            sendNode.close();

                        } catch (IOException ex) {
                            System.out.println("Fail in the connection");
                            continue;
                        }
                    }
                }
            }
            else
            {
                if(!message.split(" ")[0].equals("400"))
                {
                    String anotherNode ="";
                    System.out.println(message);
                    try {
                        Socket askForAnotherNode = new Socket("127.0.0.1",puerto_I);
                        DataInputStream inAskForAnotherNode = new DataInputStream(askForAnotherNode.getInputStream());
                        DataOutputStream outAskForAnotherNode = new DataOutputStream(askForAnotherNode.getOutputStream());

                        outAskForAnotherNode.writeUTF(message);
                        anotherNode = inAskForAnotherNode.readUTF();
                        askForAnotherNode.close();

                    } catch (IOException ex) {
                        System.out.println("Fail in the connection");
                        continue;
                    }
                    String[] anotherNodeSplit = anotherNode.split(" ");
                    for (int  i = 0; i < anotherNodeSplit.length;i++)
                    {
                        try {
                            Socket sendAnotherNode = new Socket("127.0.0.1",Integer.parseInt(anotherNodeSplit[i]));
                            DataOutputStream outSendAnotherNode = new DataOutputStream(sendAnotherNode.getOutputStream());
                            System.out.println(anotherNodeSplit[i]);
                            outSendAnotherNode.writeUTF(message);
                            sendAnotherNode.close();

                        } catch (IOException ex) {
                            System.out.println("Fail in the connection");
                            continue;
                        }
                    }
                }
            }

        }
    }

}
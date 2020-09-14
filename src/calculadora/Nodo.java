package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Nodo implements Runnable {

    static ArrayList<Integer> Nodes = new ArrayList<Integer>();
    static ArrayList<Integer> Servers = new ArrayList<Integer>();
    ServerSocket nodo = null;
    Socket nodoSocket = null;
    DataInputStream inNodo;
    DataOutputStream outNodo;
    String aCalcular = "";
    String resultado = "";
    String type = "Node";
    //puerto del nodo
    int puertoMinNodo = 5000;
    int puertoMaxNodo = 5100;
    int puertoNodoActual;
    boolean isClient = false;
    byte firstMessageByte;

    @SuppressWarnings("unused")
    private ServerSocket create(int puertoPrimerNodo,String type) throws IOException {
        var flag = true;
        while(flag){
            if(puertoPrimerNodo < puertoMaxNodo){
                try {
                    return new ServerSocket(puertoPrimerNodo);
                } catch (IOException ex) {
                    Socket socketCreation = new Socket("127.0.0.1",puertoPrimerNodo);
                    DataInputStream in = new DataInputStream(socketCreation.getInputStream());
                    DataOutputStream out = new DataOutputStream(socketCreation.getOutputStream());
                    //out.writeUTF("¿Que eres?");
                    //String mensaje = in.readUTF();
                    switch(type)
                    {
                        case "Node":
                            Nodes.add(puertoPrimerNodo);
                            break;
                        case "Server":

                            break;
                        default:
                            System.out.println("Este puerto es basura");
                    }
                    socketCreation.close();
                    Nodes.add(puertoPrimerNodo);
                    puertoPrimerNodo++;
                    continue; // try next port
                }
            }else
            {
                flag=false;
            }
        }

        // if the program gets here, no port in the range was found
        throw new IOException("no free port found");
    }
    @Override
    public void run(){
        //Nodes.add(2000);
        try {
            nodo = create(puertoMinNodo, type);
            for (int i = 0; i < Nodes.size() ; i++) {
                System.out.println(Nodes.get(i));
            }
            System.out.println("Listen port: " + String.valueOf(nodo.getLocalPort()));
            puertoNodoActual = nodo.getLocalPort();
            int ports = puertoMinNodo;
            while(puertoNodoActual > ports) {
                    try {
                        Socket newSocket = new Socket("127.0.0.1", ports);
                        DataOutputStream outNewSocket = new DataOutputStream(newSocket.getOutputStream());
                        outNewSocket.writeUTF("Node-"+puertoNodoActual);
                        //Nodo conexionServidor = new Nodo();
                        //Thread hiloNodo = new Thread(conexionServidor);
                        //hiloNodo.start();
                        newSocket.close();
                        ports = ports+1;
                    } catch (IOException ex) {
                        System.out.println("ERROR");
                    }

            }
            System.out.println("Start Node");
            /*ServerSocket port5000 = new ServerSocket(2000); // Siempre a la escucha del puerto 5000
            port5000.accept();
            Socket socketPort5000  = port5000.accept();
            DataInputStream inPort5000 = new DataInputStream(socketPort5000.getInputStream());
            DataOutputStream outPort5000 = new DataOutputStream(socketPort5000.getOutputStream());*/
            //outNewSocket.writeUTF("Node-"+puertoNodoActual);
            //puertoMinNodo = nodo.getLocalPort();
            //System.out.println("Listen port: " + String.valueOf(nodo.getLocalPort()));
            //puertoNodoActual++;
            //while (true){*/
                while (true) {
                    System.out.println("Reset Node");
                    nodoSocket = nodo.accept();
                    inNodo = new DataInputStream(nodoSocket.getInputStream());
                    outNodo = new DataOutputStream(nodoSocket.getOutputStream());
                    //firstMessageByte = inNodo.readByte();
                    //System.out.println(firstMessageByte);
                    //Espero a que el cliente se conecte al nodo
                    //nodoSocket = nodo.accept();
                    //inNodo = new DataInputStream(nodoSocket.getInputStream());
                    //outNodo = new DataOutputStream(nodoSocket.getOutputStream());
                    //operacion = inNodo.readUTF();
                    //resultado = inNodo.readUTF();
                    /*if (firstMessageByte == 1)
                    {
                        isClient = true;
                    }
                    //final String HOST = "127.0.0.1";
                    //Puerto del servidor*/
                    if (isClient)
                    {
                        aCalcular = inNodo.readUTF();
                        int puertoMinServidor = 6000;
                        int puertoMaxServidor = 6100;
                        int puertoServidorActual = puertoMinServidor;
                        DataInputStream inServer;
                        DataOutputStream outServer;
                        try {
                            System.out.println("Nodo se conecto al servidor");
                            //Create the socket to connect
                            //Creo el socket para conectarme con el servidor
                            Socket servidorSocket = new Socket("127.0.0.1", puertoMinServidor);

                            inServer = new DataInputStream(servidorSocket.getInputStream());
                            outServer = new DataOutputStream(servidorSocket.getOutputStream());

                            //Send two messages to server
                            outServer.writeUTF(aCalcular);
                            //outServer.writeUTF(operacion);
                            //outServer.writeUTF(resultado);
                            resultado = inServer.readUTF();
                            outNodo.writeUTF(resultado);
                            servidorSocket.close();

                        } catch (IOException ex) {
                            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                            isClient = false;
                        }
                        //El nodo devuelve el mensaje que es del servidor al cliente
                        //outNodo.writeUTF(aCalcular);
                        //"El cliente recibe el resultado del nodo que es: " +
                    }

                    nodoSocket.close();
                }
            //}

        } catch (IOException ex) {
            isClient = false;
            //Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("UNABLE TO SET MYPORT AT PORT " + puertoNodoActual);

        }

    }
}

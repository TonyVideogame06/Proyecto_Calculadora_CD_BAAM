package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHandler implements Runnable{

    public ServerSocket SS;//server socket
    int puerto_I;
    int Servidor;

    public ServerHandler(int Num_Nodo, int puerto_I, int Servidor) {
        try {
            this.SS = new ServerSocket(Num_Nodo);
        } catch (IOException ex) {
            Logger.getLogger(NodeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.puerto_I = puerto_I;
        this.Servidor = Servidor;
    }



    @Override
    public void run() {
        System.out.println("Yo escucho al servidor de " +  Servidor);
        while(true)
        {
            Socket elsocket;
            String Mensaje="";
            String Resultado ="";
            try{
                System.out.println("Se conect� en:  "+SS.getLocalPort());
                elsocket = SS.accept();
                DataInputStream in = new DataInputStream(elsocket.getInputStream());
                DataOutputStream out = new DataOutputStream(elsocket.getOutputStream());
                Mensaje = in.readUTF();
                System.out.println(Mensaje);
                elsocket.close();
            } catch (IOException ex) {
                System.out.println("No se pudo conectar");
            }
            try {
                Socket Operar = new Socket("0.0.0.0",Servidor);
                DataOutputStream outO = new DataOutputStream(Operar.getOutputStream());
                DataInputStream inO = new DataInputStream(Operar.getInputStream());
                outO.writeUTF(Mensaje);
                Resultado = inO.readUTF();
                Operar.close();

            } catch (IOException ex) {
                System.out.println("No se pudo conectar");
            }
            String Nodos ="";
            try {
                Socket s_request = new Socket("0.0.0.0",puerto_I);//pedimos nodos del socket
                DataInputStream in = new DataInputStream(s_request.getInputStream());
                DataOutputStream out = new DataOutputStream(s_request.getOutputStream());

                out.writeUTF(Resultado);
                Nodos = in.readUTF();
                System.out.println(Nodos);
                s_request.close();

            } catch (IOException ex) {
                System.out.println("No se pudo conectar");
            }
            System.out.println(Nodos);
            String[] Nodo = Nodos.split(" ");
            for (int  i = 0; i < Nodo.length;i++)
            {
                try {
                    Socket s = new Socket("0.0.0.0",Integer.parseInt(Nodo[i]));//creamos socket del server
                    DataOutputStream salida = new DataOutputStream(s.getOutputStream());

                    salida.writeUTF(Resultado);
                    s.close();

                } catch (IOException ex) {
                    System.out.println("No se pudo conectar");
                }
            }
        }
    }

}

package calculadora;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler  implements Runnable{

    public ServerSocket SS; //Server para escuchar cliente
    int puerto;
    int Cliente;

    public ClientHandler(int Num_Nodo, int puerto, int Cliente) {
        try {
            this.SS = new ServerSocket(Num_Nodo);
        } catch (IOException ex) {
            Logger.getLogger(NodeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.puerto = puerto;
        this.Cliente = Cliente;
    }

    @Override
    public void run() {
        System.out.println("Escuchando al cliente: "+ Cliente);
        while(true)
        {
            Socket s;
            String Mensaje="";
            try{
                System.out.println("Se acaba de conectar "+SS.getLocalPort());
                s = SS.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                Mensaje = in.readUTF();
                System.out.println(Mensaje );
                s.close();
            } catch (IOException ex) {
                System.out.println("Fallo la conexion");
            }
            String Nodos ="";
            try {
                Socket s_request = new Socket("127.0.0.1",puerto);//pedimos los nodos
                DataInputStream in = new DataInputStream(s_request.getInputStream());
                DataOutputStream out = new DataOutputStream(s_request.getOutputStream());

                out.writeUTF(Mensaje);
                Nodos = in.readUTF();
                System.out.println(Nodos);
                s_request.close();

            } catch (IOException ex) {
                System.out.println("No se pudo realizar la conexi�n");
            }
            String[] Nodo = Nodos.split(" ");
            for (int  i = 0; i < Nodo.length;i++)
            {
                try {
                    Socket Enviar = new Socket("127.0.0.1",Integer.parseInt(Nodo[i]));
                    DataOutputStream salida = new DataOutputStream(Enviar.getOutputStream());

                    salida.writeUTF(Mensaje);
                    Enviar.close();

                } catch (IOException ex) {
                    System.out.println("No se pudo realizar la conexi�n");
                }

            }
        }
    }

}

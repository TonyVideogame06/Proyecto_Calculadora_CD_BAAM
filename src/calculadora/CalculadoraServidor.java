package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

public class CalculadoraServidor {

    static public ServerSocket create(int[] ports) throws IOException {
        for (int port : ports) {
            try {
                return new ServerSocket(port);
            } catch (IOException ex) {
                continue; // try next port
            }
        }

        // if the program gets here, no port in the range was found
        throw new IOException("no free port found");
    }

    public static void main(String[] args) throws IOException {
        int puertos[] = new int[1000];

        for(int i=0;i<puertos.length; i++)
        {
            puertos[i]=i+7000;
        }
        ServerSocket Servidor = create(puertos);

        String mensaje="";
        final String HOST ="0.0.0.0";
        int PUERTO =8000;
        DataInputStream in;
        DataOutputStream out;
        var Buscando=true;
        //Sacamos el timestamp para saber cuando se creo el servidor
        Timestamp newTimeStampMillis =new Timestamp(System.currentTimeMillis());
        Date clientCreationDate =new Date(newTimeStampMillis.getTime());
        String stringClientCreationDate = clientCreationDate.toString();

        while(Buscando){
            try {
                Socket s = new Socket(HOST,PUERTO);
                in = new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());

                out.writeUTF("0010 "+Servidor.getLocalPort());

                mensaje = in.readUTF();
                System.out.println(mensaje);
                if (Integer.parseInt(mensaje) !=0)
                {
                    System.out.println(PUERTO);
                    System.out.println(mensaje);
                    Buscando = false;
                    break;
                }
                s.close();

            } catch (IOException ex) {
                System.out.println("No se pudo conectar");
            }
            PUERTO=PUERTO+200;
            if (PUERTO>60000)
            {
                Buscando = false;
            }
        }

        ReceiveData Serv= new ReceiveData(Servidor);
        int myPort = Serv.SocketServidor.getLocalPort();
        Thread llegada = new Thread(Serv);

        llegada.start();


    }

}




package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculadoraServidor {

    public static double Operaciones(String CalcularOperaciones){

        String[] numeroOperadorSplit = CalcularOperaciones.split(",");
        double primerNumero = Double.parseDouble(numeroOperadorSplit[2]);
        double segundoNumero = Double.parseDouble(numeroOperadorSplit[3]);
        String operador = numeroOperadorSplit[1];
        double respuesta=0;
        switch(operador) {
            case "1":
                respuesta = primerNumero + segundoNumero;
                break;
            case "2":
                respuesta = primerNumero - segundoNumero;
                break;
            case "3":
                respuesta = primerNumero * segundoNumero;
                break;
            case "4":
                if(segundoNumero==0){
                    System.out.println("ERROR Vuelva a intentarlo");
                    return -1; //Error
                }
                respuesta = primerNumero / segundoNumero;
                break;

        }
        return respuesta;
    }

    public static void main(String[] args) throws IOException {

        ServerSocket servidor = null;
        Socket servidorSocket = null;
        DataInputStream in;
        DataOutputStream out;
        int puertoServidorMin = 6000;
        int puertoServidorMax = 6100;
        int puertoServidorActual = puertoServidorMin;
        boolean isServer;
        int localSocketPort;
        int totalConnections;
        Socket localSocket;
        Socket dummySocket;
        //puerto de nuestro servidor
        //final int PUERTO = 5000;
            try {
                //Creamos el socket del servidor
                isServer = true;
                servidor = new ServerSocket(puertoServidorActual);
                System.out.println("Servidor iniciado");

                //Siempre estara escuchando peticiones
                while (true) {

                    //Espero a que el nodo se conecte
                    servidorSocket = servidor.accept();

                    System.out.println("Cliente conectado");
                    in = new DataInputStream(servidorSocket.getInputStream());
                    out = new DataOutputStream(servidorSocket.getOutputStream());
                    //Leo la operacion que realizo la calculadora que me envia el nodo
                    //String mensaje1 = in.readUTF();
                    //System.out.println("El servidor recibio del nodo el resultado de la operacion " + mensaje1);

                    //Leo el numero que me envia el nodo
                    //String mensaje2 = in.readUTF();

                    //System.out.println("Servidor recibe del nodo el numero: " + mensaje2);
                    String numeroOperadorACalcular = in.readUTF();
                    System.out.println(numeroOperadorACalcular);
                    String resultado = String.valueOf(Operaciones(numeroOperadorACalcular));
                    System.out.println(resultado);
                    out.writeUTF(resultado);
                    //Cierro el socket
                    servidorSocket.close();
                    System.out.println("Nodo desconectado");
                }

            } catch (IOException ex) {
                Logger.getLogger(CalculadoraServidor.class.getName()).log(Level.SEVERE, null, ex);
            }


    }

}




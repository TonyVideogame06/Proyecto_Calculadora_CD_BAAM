package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Dividir {

    public Dividir(String Mensaje, int puerto) throws IOException, InterruptedException {
        String[] args = Mensaje.split(" ");
        String Resul = "";
        double num1 = Integer.parseInt(args[3]);
        double num2 = Integer.parseInt(args[4]);
        double resultado = num1 / num2;
        Resul = "8 " + args[1] + " " + args[2] + " " + args[3] + " " + args[4] + " " + resultado;
        System.out.println(Resul);
        final String HOST ="127.0.0.1";
        DataOutputStream out;
        try {
            Socket elsocket = new Socket(HOST,puerto);
            out = new DataOutputStream(elsocket.getOutputStream());

            out.writeUTF(Resul);


            elsocket.close();

        } catch (IOException ex) {
            System.out.println("Fallo, error en la conexcion");
        }

    }
}

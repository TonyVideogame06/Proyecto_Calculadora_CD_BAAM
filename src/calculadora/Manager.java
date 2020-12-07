package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;

public class Manager {

    public static void main(String[] args) throws IOException {
        Scanner ingresarCodeHash = new Scanner(System.in);
        int goToNode=8000;
        final String localHost ="127.0.0.1";
        var flag = true;
        while(flag){
            try {
                Socket socketManager = new Socket(localHost,goToNode);
                DataOutputStream outSocketManager = new DataOutputStream(socketManager.getOutputStream());
                outSocketManager.writeUTF("I'm the server manager");
                socketManager.close();
                flag=false;

            } catch (IOException ex) {
                System.out.println("Fail in the connection");
                goToNode=goToNode+200;
            }
        }

        byte[] bytesSumar = Files.readAllBytes(Paths.get("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Operaciones\\Sumar.jar"));
        byte[] bytesRestar = Files.readAllBytes(Paths.get("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Operaciones\\Restar.jar"));
        byte[] bytesMultiplicar = Files.readAllBytes(Paths.get("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Operaciones\\Multiplicar.jar"));
        byte[] bytesDividir = Files.readAllBytes(Paths.get("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Operaciones\\Dividir.jar"));

        String operationSumar = Base64.getEncoder().encodeToString(bytesSumar);
        String operationRestar = Base64.getEncoder().encodeToString(bytesRestar);
        String operationMultiplicar = Base64.getEncoder().encodeToString(bytesMultiplicar);
        String operationDividir = Base64.getEncoder().encodeToString(bytesDividir);
        String serverConfiguration = "";
        while(true){
            System.out.println("--------------------MANUAL DE USO DEL GESTOR------------------");
            System.out.println("Primero digite el numero de la operacion que desea que el servidor realice");
            System.out.println("1000: Suma     1010: Resta    1001: Multiplicacion   1011:Division");
            System.out.println("Despues, seguido de un espacio, ingrese el hash SHA1 que pertenece al servidor");
            System.out.println("Ejemplo del Formato: 1010 f866110d89990f49ba038e74ff05b3d7942367b9");
            String serverHash = ingresarCodeHash.nextLine();
            String[] serverHashSplit = serverHash.split(" ");
            String onlyServerHash = serverHashSplit[1];
            String operationServer= serverHashSplit[0];
            if (operationServer.equals("1000"))
            {
                operationServer = "1";
            }
            if (operationServer.equals("1010"))
            {
                operationServer = "2";
            }
            if (operationServer.equals("1001"))
            {
                operationServer = "3";
            }
            if (operationServer.equals("1011"))
            {
                operationServer = "4";
            }
            switch(operationServer){
                case "1": //Suma
                    serverConfiguration = "0 " + operationServer+ " " + onlyServerHash + " " + operationSumar;
                    System.out.println("El servidor con el hash SHA1: " + onlyServerHash + "ahora solo puede Sumar");
                    break;
                case "2": //Resta
                    serverConfiguration = "0 " + operationServer+ " " + onlyServerHash + " " + operationRestar;
                    System.out.println("El servidor con el hash SHA1: " + onlyServerHash + "ahora solo puede Restar");
                    break;
                case "3": //Multiplicacion
                    serverConfiguration = "0 " + operationServer+ " " + onlyServerHash + " " + operationMultiplicar;
                    System.out.println("El servidor con el hash SHA1: " + onlyServerHash + "ahora solo puede Multiplicar");
                    break;
                case "4": //Division
                    serverConfiguration = "0 " + operationServer+ " " + onlyServerHash + " " + operationDividir;
                    System.out.println("El servidor con el hash SHA1: " + onlyServerHash + "ahora solo puede Dividirr");
                    break;
            }
            try {
                Socket nodeSocket = new Socket(localHost,goToNode+198);
                DataOutputStream outNodeSocket = new DataOutputStream(nodeSocket.getOutputStream());

                outNodeSocket.writeUTF(serverConfiguration);

                nodeSocket.close();

            } catch (IOException ex) {
                System.out.println("Fail in the connection");
                goToNode=goToNode+200;
            }
        }
    }

}

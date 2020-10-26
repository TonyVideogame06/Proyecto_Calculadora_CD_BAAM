package calculadora;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

public class ReceiveData implements Runnable{

    public static String CalculateOperation(String totalMessageToOperate) throws IOException
    {
        String[] messageToCalculateSplit = totalMessageToOperate.split(" ");
        double num1 =Double.parseDouble(messageToCalculateSplit[1]);
        double num2 =Double.parseDouble(messageToCalculateSplit[2]);
        String stringNum1 = messageToCalculateSplit[1];
        String stringNum2 = messageToCalculateSplit[2];
        String hashMD5Cliente = messageToCalculateSplit[4];
        double answer=0;
        String operationResultCode="";
        switch(messageToCalculateSplit[0])
        {
            case "1000": //Suma
                String dataSuma="";
                Process processOperationSuma = null;

                processOperationSuma = Runtime.getRuntime().exec("cmd /c start java -jar \"D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Suma\\Suma.jar\" " + num1 + " " + num2 );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                try {
                    File sumaTxt = new File("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Operaciones\\suma.txt");
                    Scanner myReader = new Scanner(sumaTxt);
                    while (myReader.hasNextLine()) {
                        dataSuma = myReader.nextLine();
                        System.out.println(dataSuma);
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                answer = Double.parseDouble(dataSuma);
                operationResultCode="0111";
                break;
            case "1001": //Resta
                String dataResta="";
                Process processOperationResta = null;

                processOperationResta = Runtime.getRuntime().exec("cmd /c start java -jar \"D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Resta\\Resta.jar\" " + num1 + " " + num2 );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                try {
                    File restaTxt = new File("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Operaciones\\resta.txt");
                    Scanner myReader = new Scanner(restaTxt);
                    while (myReader.hasNextLine()) {
                        dataResta = myReader.nextLine();
                        System.out.println(dataResta);
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                answer = Double.parseDouble(dataResta);
                operationResultCode="0110";
                break;
            case "1100": //Multiplicacion
                String dataMultiplicacion="";
                Process processOperationMultiplicacion = null;

                processOperationMultiplicacion = Runtime.getRuntime().exec("cmd /c start java -jar \"D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Multiplicacion\\Multiplicacion.jar\" " + num1 + " " + num2 );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                try {
                    File multiplicacionTxt = new File("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Operaciones\\multiplicacion.txt");
                    Scanner myReader = new Scanner(multiplicacionTxt);
                    while (myReader.hasNextLine()) {
                        dataMultiplicacion = myReader.nextLine();
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                answer = Double.parseDouble(dataMultiplicacion);
                operationResultCode="0011";
                break;
            case "1101": //Division
                String dataDivision="";
                Process processOperationDivision = null;

                processOperationDivision = Runtime.getRuntime().exec("cmd /c start java -jar \"D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Division\\Division.jar\" " + num1 + " " + num2 );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                try {
                    File divisionTxt = new File("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Operaciones\\division.txt");
                    Scanner myReader = new Scanner(divisionTxt);
                    while (myReader.hasNextLine()) {
                        dataDivision = myReader.nextLine();
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                answer = Double.parseDouble(dataDivision);
                operationResultCode="1010";
                break;
            default:
                answer=num1;
                break;
        }
        String finalResult = operationResultCode + " "+stringNum1+ " "+ stringNum2 +" " + answer +" "+ hashMD5Server +" "+ hashMD5Cliente;
        return finalResult;
    }

    static public ServerSocket SocketServidor;

    public ReceiveData(ServerSocket ports) throws IOException {
        this.SocketServidor = ports;
    }

    //Sacamos el timestamp para saber cuando se creo el servidor
    static Timestamp newTimeStampMillis =new Timestamp(System.currentTimeMillis());
    static  Date serverCreationDate =new Date(newTimeStampMillis.getTime());
    static String stringServerCreationDate = serverCreationDate.toString();
    static Cifrar createHashMD5Server = new Cifrar();
    static String hashMD5Server = createHashMD5Server.md5(stringServerCreationDate);

    @Override
    public void run() {


        System.out.println("Server Hash MD5 Code = " + hashMD5Server);
        while(true)
        {
            Socket serverSocketOperation;
            try {
                System.out.println("Se acaba de conectar "+SocketServidor.getLocalPort());
                serverSocketOperation = SocketServidor.accept();
                DataInputStream inServerSocketOperation = new DataInputStream(serverSocketOperation.getInputStream());
                DataOutputStream outServerSocketOperation = new DataOutputStream(serverSocketOperation.getOutputStream());
                String message = inServerSocketOperation.readUTF();
                System.out.println(message);
                String resultado = CalculateOperation(message);
                outServerSocketOperation.writeUTF(resultado);
                System.out.println(resultado);
                serverSocketOperation.close();
            } catch (IOException ex) {
                System.out.println("Fallo la conexion");
            }
        }
    }
}

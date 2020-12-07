package calculadora;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.io.FileUtils.copyDirectory;

public class Controller implements Initializable {

    String acumulate ="";
    int goToNode =0;
    @FXML
    private Button num1;
    @FXML
    private Button num2;
    @FXML
    private Button num3;
    @FXML
    private Button num4;
    @FXML
    private Button num5;
    @FXML
    private Button num6;
    @FXML
    private Button num7;
    @FXML
    private Button num8;
    @FXML
    private Button num9;
    @FXML
    private Button num0;
    @FXML
    private Button punt;
    @FXML
    private Button bora;
    @FXML
    private Button suma;
    @FXML
    private Button rest;
    @FXML
    private Button mult;
    @FXML
    private Button divi;
    @FXML
    private TextField ResultadoTotal;
    @FXML
    private TextArea showAnswerOnClientSuma;
    @FXML
    private TextArea showAnswerOnClientResta;
    @FXML
    private TextArea showAnswerOnClientMultiplicacion;
    @FXML
    private TextArea showAnswerOnClientDivisiones;

    double makeDoubleNum =0;
    char operation ='0';
    int i = 1;
    int localPort;
    Thread TReceiveMessage;
    String hashSHA1;
    static String my_path = "D:/Documentos/UniversidadPanamericana/Semestres/Computo Distribuido/Proyecto/Proyecto_Calculadora_ArandaMejiaBrianAntonio/out/artifacts/";
    static String general_folder = "CalculadoraServidor";
    static String my_folder = "CalculadoraServidor";
    static ArrayList<String> listEventSuma = new ArrayList<>();
    static ArrayList<String> listEventResta = new ArrayList<>();
    static ArrayList<String> listEventMultiplicacion = new ArrayList<>();
    static ArrayList<String> listEventDivision = new ArrayList<>();
    List<tempEvent> listSumas = new ArrayList<tempEvent>();
    List<tempEvent> listRestas = new ArrayList<tempEvent>();
    List<tempEvent> listMultiplicaciones = new ArrayList<tempEvent>();
    List<tempEvent> listDivisiones = new ArrayList<tempEvent>();
    List<Servers> ServersSumas = new ArrayList<Servers>();
    List<Servers> ServersRestas = new ArrayList<Servers>();
    List<Servers> ServersMultiplicaciones = new ArrayList<Servers>();
    List<Servers> ServersDivisiones = new ArrayList<Servers>();
    int incrementEvent =0;
    int folioSuma = 0;
    int folioResta = 0;
    int folioMultiplicacion = 0;
    int folioDivision = 0;

    public class Servers {
        String Server;
        boolean doesItAppear;
    }

    public class tempEvent
    {
        int tempNumEvent;
        String operation;
        public double answer = -123;

        public tempEvent(int number, String operation)
        {
            tempNumEvent = number;
            this.operation = operation;
        }
    }

    int resultReceivedSumas = 0;
    int resultReceivedRestas = 0;
    int resultReceivedMultiplicacion = 0;
    int resultReceivedDivision =0;
    int acusesNecesarios = 2;

    public ServerSocket create(int ports){
        for (int port= ports;port < 7000; port+= 5) {
            try {
                return new ServerSocket(port);
            } catch (IOException ex) {
                continue; // try next port
            }
        }
        try {
            // if the program gets here, no port in the range was found
            throw new IOException("no free port found");
        } catch (IOException ex) {
            Logger.getLogger(CalculadoraCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    static void DuplicateServer()
    {
        System.out.println("Pido duplicacion XD");
        String program_to_run = "";
        File temp = new File(my_path + general_folder);
        int i = 1;
        while (temp.exists())
        {
            temp = new File(my_path + general_folder + i);
            i++;
        }
        i--;
        try {
            copyDirectory(new File(my_path + my_folder), temp, false);
            program_to_run = my_path + general_folder + i + "\\CalculadoraServidor.jar";
            System.out.println("PROGRAM TO RUN: " + program_to_run);
            //Runtime.getRuntime().exec(new String[] {"java", "-jar", program_to_run, "Echo", general_folder+ i });
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\ && java -jar CalculadoraServidor"+i+"\\CalculadoraServidor.jar\"");
            //Runtime.getRuntime().exec("cmd /c start /K \"cd D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\ && java -jar CalculadoraServidor"+i+"\\CalculadoraServidor.jar\"");

        }catch (IOException ex){
            System.out.println("ERROR DUPLICATING SERVER");
        }
    }

    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            hashSHA1 =   sha1(String.valueOf(System.currentTimeMillis()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("El hash SHA1 de esta calculadora cliente es: "+hashSHA1);
        ServerSocket clientServerSocket = create(6000);
        localPort = clientServerSocket.getLocalPort();
        String message="";
        final String localHost ="127.0.0.1";
        int nodePort =8000;
        DataInputStream inClientSocket;
        DataOutputStream outClientSocket;
        var searching=true;
        while(searching){
            try {
                Socket clientSocket = new Socket(localHost,nodePort);
                inClientSocket = new DataInputStream(clientSocket.getInputStream());
                outClientSocket = new DataOutputStream(clientSocket.getOutputStream());

                outClientSocket.writeUTF("Cliente "+clientServerSocket.getLocalPort());

                message = inClientSocket.readUTF();
                if (Integer.parseInt(message) !=0)
                {
                    searching = false;
                    break;
                }
                clientSocket.close();

            } catch (IOException ex) {
                System.out.println("Fallo, error en la conexcion");
            }
            nodePort= nodePort+200;
            if (nodePort>60000)
            {
                searching = false;
            }
        }
        goToNode = Integer.parseInt(message);

        ReceiveThead ReceiveMessage = new ReceiveThead(clientServerSocket);
        TReceiveMessage = new Thread(ReceiveMessage);
        TReceiveMessage.start();
    }


    @FXML
    private void buttonNumberOne(ActionEvent event) {
        acumulate = acumulate +"1";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberTwo(ActionEvent event) {
        acumulate = acumulate +"2";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberThree(ActionEvent event) {
        acumulate = acumulate +"3";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberFour(ActionEvent event) {
        acumulate = acumulate +"4";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberFive(ActionEvent event) {
        acumulate = acumulate +"5";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberSix(ActionEvent event) {
        acumulate = acumulate +"6";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberSeven(ActionEvent event) {
        acumulate = acumulate +"7";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberEight(ActionEvent event) {
        acumulate = acumulate +"8";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberNine(ActionEvent event) {
        acumulate = acumulate +"9";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonNumberZero(ActionEvent event) {
        acumulate = acumulate +"0";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonPunto(ActionEvent event) {
        acumulate = acumulate +".";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonAC(ActionEvent event) {
        acumulate = "";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonSuma(ActionEvent event) {

        makeDoubleNum = Double.parseDouble(acumulate);
        operation = '1';
        acumulate = acumulate + " + ";
        ResultadoTotal.setText(acumulate);

    }

    @FXML
    private void buttonResta(ActionEvent event) {

        makeDoubleNum = Double.parseDouble(acumulate);
        operation = '2';
        acumulate = acumulate + " - ";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonMultiplicacion(ActionEvent event) {
        makeDoubleNum = Double.parseDouble(acumulate);
        operation = '3';
        acumulate = acumulate + " * ";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void buttonDivision(ActionEvent event) {
        makeDoubleNum = Double.parseDouble(acumulate);
        operation = '4';
        acumulate = acumulate + " / ";
        ResultadoTotal.setText(acumulate);
    }

    @FXML
    private void botonEnviar(ActionEvent event) {
        String[] sendMessageSplit = acumulate.split(" ");
        String num1 = sendMessageSplit[0];
        String num2 = sendMessageSplit[2];
        String numerosAOperar = num1 + " "+ num2;
        Thread Time = null;
        switch(operation)
        {

            case '1':
                String k = Integer.toString(incrementEvent);
                listSumas.add(new tempEvent(listSumas.size(),numerosAOperar));
                Connection(("1 "+(listSumas.size()-1)+" "+ hashSHA1 +" "+numerosAOperar),'+');
                listEventSuma.add("Folio: "+ folioSuma+" "+"Evento: "+k+" "+"CC: 1 "+" Numeros a Sumar: "+numerosAOperar+" Operacion: + "+" Hash MD5 del Cliente: "+hashSHA1);
                incrementEvent++;
                folioSuma++;
                ContadorMaximoAcusesSumas contenTime_s = new ContadorMaximoAcusesSumas();
                Time = new Thread(contenTime_s);
                break;
            case '2':
                String o = Integer.toString(incrementEvent);
                listRestas.add(new tempEvent(listRestas.size(),numerosAOperar));
                Connection(("2 "+(listRestas.size()-1)+" "+ hashSHA1 +" "+numerosAOperar),'-');
                listEventResta.add("Folio: "+ folioResta+" "+"Evento: "+o+" "+"CC: 2 "+" Numeros a Restar: "+numerosAOperar+" Operacion: - "+" Hash MD5 del Cliente: "+hashSHA1);
                incrementEvent++;
                folioResta++;
                ContadorMaximoAcusesRestas ContenTime_r = new ContadorMaximoAcusesRestas();
                Time = new Thread(ContenTime_r);
                break;
            case '3':
                String x = Integer.toString(incrementEvent);
                listMultiplicaciones.add(new tempEvent(listMultiplicaciones.size(),numerosAOperar));
                Connection(("3 "+(listMultiplicaciones.size()-1)+" "+ hashSHA1 +" "+numerosAOperar),'*');
                listEventMultiplicacion.add("Folio: "+ folioMultiplicacion+" "+"Evento: "+x+" "+"CC: 3 "+" Numeros a Multiplicar: "+numerosAOperar+" Operacion: * "+" Hash MD5 del Cliente: "+hashSHA1);
                incrementEvent++;
                folioMultiplicacion++;
                ContadorMaximoAcusesMultiplicaciones ContenTime_m = new ContadorMaximoAcusesMultiplicaciones();
                Time = new Thread(ContenTime_m);
                break;
            case '4':
                String y = Integer.toString(incrementEvent);
                listDivisiones.add(new tempEvent(listDivisiones.size(),numerosAOperar));
                Connection(("4 "+(listDivisiones.size()-1)+" "+ hashSHA1 +" "+numerosAOperar),'/');
                listEventDivision.add("Folio: "+ folioDivision+" "+"Evento: "+y+" "+"CC: 4 "+" Numeros a Dividir: "+numerosAOperar+" Operacion: / "+" Hash MD5 del Cliente: "+hashSHA1);
                incrementEvent++;
                folioDivision++;
                ContadorMaximoAcusesDivisiones Content_d = new ContadorMaximoAcusesDivisiones();
                Time = new Thread(Content_d);
                break;
        }

        Time.start();
        acumulate ="";
        ResultadoTotal.setText(acumulate);

    }
    //funcion donde se recibe el resultado y el tipo de operaci�n
    public void Connection(String Total, char operacion)
    {
        String mensaje="";
        final String HOST ="127.0.0.1";
        DataInputStream in;
        DataOutputStream out;



        try {
            Socket elsocket = new Socket(HOST,goToNode);
            out = new DataOutputStream(elsocket.getOutputStream());

            out.writeUTF(Total);


            elsocket.close();

        } catch (IOException ex) {
            System.out.println("Fallo, error en la conexcion");
        }
    }


    public class ReceiveThead implements Runnable{

        ServerSocket clientPort;

        public ReceiveThead(ServerSocket Creado) {
            this.clientPort = Creado;
        }


        @Override
        public void run() {
            while(true)
            {
                Socket newSocketClient;
                String message="";
                try{
                    System.out.println("Client is in port "+ clientPort.getLocalPort());
                    newSocketClient = clientPort.accept();
                    DataInputStream in = new DataInputStream(newSocketClient.getInputStream());
                    message = in.readUTF();
                    System.out.println(message);
                    newSocketClient.close();
                } catch (IOException ex) {
                    System.out.println("Fail in the connection");
                }
                String[] messageSplit = message.split(" ");
                int ajuste = Math.max(0, hashSHA1.length() - 4);
                String anotherHashSHA1 = hashSHA1.substring(0, ajuste);
                String contentCodeAnswers = messageSplit[0];
                String validationHashSHA1 = messageSplit[2];
                String firstNumber = messageSplit[3];
                String secondNumber = messageSplit[4];
                if(Integer.parseInt(contentCodeAnswers) >= 5 && (validationHashSHA1.equals(anotherHashSHA1)|| validationHashSHA1.equals(hashSHA1)))
                {
                    String showAnswerClient ="";
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //switch (messageSplit[0])
                    switch (contentCodeAnswers)
                    {
                        case "5": //Resultado Suma
                            showAnswerClient = firstNumber +" + "+  secondNumber + " = " + messageSplit[5];
                            tempEvent S = listSumas.get(Integer.parseInt(messageSplit[1]));
                            if(S.answer == -123)
                            {
                                S.answer = Double.parseDouble(messageSplit[5]);
                                listSumas.set(Integer.parseInt(messageSplit[1]),S);
                                if(acusesNecesarios >= 1 && resultReceivedSumas < acusesNecesarios)
                                {
                                    DuplicateServer();
                                }
                                if(resultReceivedSumas >= acusesNecesarios)
                                {
                                    System.out.println("Lista de foliaje de las sumas: "+ listEventSuma);
                                    showAnswerOnClientSuma.appendText(showAnswerClient + "\n");
                                }

                            }
                            break;
                        case "6": //Resultado Resta
                            showAnswerClient = firstNumber +" - "+  secondNumber + " = " + messageSplit[5];
                            tempEvent B = listRestas.get(Integer.parseInt(messageSplit[1]));
                            if(B.answer ==-123){
                                B.answer = Double.parseDouble(messageSplit[5]);
                                listRestas.set(Integer.parseInt(messageSplit[1]),B);
                                if(acusesNecesarios >= 1 && resultReceivedRestas < acusesNecesarios)
                                {
                                    DuplicateServer();
                                }
                                if(resultReceivedRestas >= acusesNecesarios)
                                {
                                    System.out.println("Lista de foliaje de las restas: "+ listEventResta);
                                    showAnswerOnClientResta.appendText(showAnswerClient+ "\n");
                                }
                            }
                            break;
                        case "7": //Resultado Multiplicacion
                            showAnswerClient = firstNumber +" * "+  secondNumber + " = " + messageSplit[5];
                            tempEvent C = listMultiplicaciones.get(Integer.parseInt(messageSplit[1]));
                            if(C.answer ==-123){
                                C.answer = Double.parseDouble(messageSplit[5]);
                                listMultiplicaciones.set(Integer.parseInt(messageSplit[1]),C);
                                if(acusesNecesarios >= 1 && resultReceivedMultiplicacion < acusesNecesarios)
                                {
                                    DuplicateServer();
                                }
                                if(resultReceivedMultiplicacion >= acusesNecesarios)
                                {
                                    System.out.println("Lista de foliaje de las multiplicaciones: "+ listEventMultiplicacion);
                                    showAnswerOnClientMultiplicacion.appendText(showAnswerClient+ "\n");
                                }

                            }
                            break;
                        case "8": //Resultado Division
                            showAnswerClient = firstNumber +" / "+  secondNumber + " = " + messageSplit[5];
                            tempEvent D = listDivisiones.get(Integer.parseInt(messageSplit[1]));
                            if(D.answer ==-123){
                                D.answer = Double.parseDouble(messageSplit[5]);
                                listDivisiones.set(Integer.parseInt(messageSplit[1]),D);
                                if(acusesNecesarios >= 1 && resultReceivedDivision < acusesNecesarios)
                                {
                                    DuplicateServer();
                                }
                                if(resultReceivedDivision >= acusesNecesarios)
                                {
                                    System.out.println("Lista de foliaje de las divisiones: "+ listEventDivision);
                                    showAnswerOnClientDivisiones.appendText(showAnswerClient+ "\n");
                                }

                            }
                            break;
                    }
                }

            }

        }

    }



    public  class ContadorAcusesSuma implements Runnable{

        boolean exit = true;
        ServerSocket AcusesSuma;

        ContadorAcusesSuma(ServerSocket AcusesSuma){
            this.AcusesSuma = AcusesSuma;
        }

        @Override
        public void run() {
            for(int i = 0; i < ServersSumas.size(); i++)
            {
                Servers serversSuma = new Servers();
                serversSuma.doesItAppear = false ;
                serversSuma.Server = ServersSumas.get(i).Server;
                ServersSumas.set(i,serversSuma);
            }
            while(exit){
                Socket socketSuma;
                String message="";

                try{
                    socketSuma = AcusesSuma.accept();
                    DataInputStream inSocketSuma = new DataInputStream(socketSuma.getInputStream());
                    message = inSocketSuma.readUTF();
                    if(message.split(" ")[4].equals("1"))
                    {
                        resultReceivedSumas++;
                        boolean A = false;
                        System.out.println(message.split(" ")[1]);
                        for(int i = 0; i < ServersSumas.size(); i++)
                        {
                            if(ServersSumas.get(i).Server.equals(message.split(" ")[1]))
                            {
                                ServersSumas.get(i).doesItAppear =true;
                                A=true;
                                break;
                            }
                        }
                        if(!A)
                        {
                            Servers aux = new Servers();
                            aux.doesItAppear =true;
                            aux.Server = message.split(" ")[1];
                            ServersSumas.add(aux);
                        }
                    }
                    socketSuma.close();
                } catch (IOException ex) {
                    break;
                }
            }
        }

    }

    public class ContadorMaximoAcusesSumas implements Runnable
    {

        @Override
        public void run() {
            resultReceivedSumas = 0;
            try {
                long start = System.nanoTime();
                ServerSocket maximoAcusesSuma = new ServerSocket(localPort+1);
                ContadorAcusesSuma contar = new ContadorAcusesSuma(maximoAcusesSuma);
                Thread ThreadContar = new Thread(contar);
                ThreadContar.start();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                maximoAcusesSuma.close();
                maximoAcusesSuma=null;
                System.out.println("Numero de acuses de suma: "+ resultReceivedSumas);
                if(resultReceivedSumas < acusesNecesarios){
                    System.out.println("No hay suficientes servidores de suma, vuelva a intentarlo");
                    for(int i = 0; i < ServersSumas.size(); i++)
                    {
                        if (!ServersSumas.get(i).doesItAppear)
                        {
                            //Mandar a levantar
                            int j=0;
                            String wakeUpServer ="";
                            while(j< ServersSumas.size())
                            {
                                if (ServersSumas.get(j).doesItAppear)
                                {
                                    wakeUpServer = ServersSumas.get(j).Server;
                                    j= ServersSumas.size()+20;
                                }
                                j++;
                            }
                            WakeUp Fall = new WakeUp(ServersSumas.get(i).Server,wakeUpServer);
                            Thread TWakeUp = new Thread(Fall);
                            TWakeUp.start();
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    public  class ContadorAcusesResta implements Runnable{

        boolean exit = true;
        ServerSocket Tickets;

        ContadorAcusesResta(ServerSocket Tickets){
            this.Tickets = Tickets;
        }

        @Override
        public void run() {
            for(int i = 0; i < ServersRestas.size(); i++)
            {
                Servers A = new Servers();
                A.doesItAppear = false ;
                A.Server = ServersRestas.get(i).Server;
                ServersRestas.set(i,A);
            }
            while(exit){
                Socket elsocket;
                String Mensaje="";

                try{
                    elsocket = Tickets.accept();
                    DataInputStream in = new DataInputStream(elsocket.getInputStream());
                    Mensaje = in.readUTF();
                    if(Mensaje.split(" ")[4].equals("1"))
                    {
                        resultReceivedRestas++;
                        boolean A = false;
                        System.out.println(Mensaje.split(" ")[1]);
                        for(int i = 0; i < ServersRestas.size(); i++)
                        {
                            if(ServersRestas.get(i).Server.equals(Mensaje.split(" ")[1]))
                            {
                                ServersRestas.get(i).doesItAppear =true;
                                A=true;
                                break;
                            }
                        }
                        if(!A)
                        {
                            Servers aux = new Servers();
                            aux.doesItAppear =true;
                            aux.Server = Mensaje.split(" ")[1];
                            ServersRestas.add(aux);
                        }
                    }
                    System.out.println(Mensaje);
                    elsocket.close();
                } catch (IOException ex) {
                    break;
                }
            }
        }

    }

    public class ContadorMaximoAcusesRestas implements Runnable
    {

        @Override
        public void run() {
            resultReceivedRestas = 0;
            try {
                long start = System.nanoTime();
                ServerSocket socketRestas = new ServerSocket(localPort+2);
                ContadorAcusesResta contar1 = new ContadorAcusesResta(socketRestas);
                Thread Tcontar = new Thread(contar1);
                Tcontar.start();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                socketRestas.close();
                socketRestas=null;
                System.out.println("Numero de acuses de resta: "+ resultReceivedRestas);
                if(resultReceivedRestas < acusesNecesarios){
                    System.out.println("No hay suficientes servidores de resta, vuelva a intentarlo");
                }
                for(int i = 0; i < ServersRestas.size(); i++)
                {
                    if (!ServersRestas.get(i).doesItAppear)
                    {
                        //Mandar a levantar
                        int j=0;
                        String ServUp ="";
                        while(j< ServersRestas.size())
                        {
                            if (ServersRestas.get(j).doesItAppear)
                            {
                                ServUp = ServersRestas.get(j).Server;
                                j= ServersRestas.size()+20;
                            }
                            j++;
                        }
                        WakeUp Fall = new WakeUp(ServersRestas.get(i).Server,ServUp);
                        Thread Lev = new Thread(Fall);
                        Lev.start();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    public  class ContadorAcusesMultiplicacion implements Runnable{

        boolean exit = true;
        ServerSocket acusesMultiplicacion;

        ContadorAcusesMultiplicacion(ServerSocket acusesMultiplicacion){
            this.acusesMultiplicacion = acusesMultiplicacion;
        }

        @Override
        public void run() {
            for(int i = 0; i < ServersMultiplicaciones.size(); i++)
            {
                Servers A = new Servers();
                A.doesItAppear = false ;
                A.Server = ServersMultiplicaciones.get(i).Server;
                ServersMultiplicaciones.set(i,A);
            }
            while(exit){
                Socket elsocket;
                String Mensaje="";

                try{
                    elsocket = acusesMultiplicacion.accept();
                    DataInputStream in = new DataInputStream(elsocket.getInputStream());
                    Mensaje = in.readUTF();
                    if(Mensaje.split(" ")[4].equals("1"))
                    {
                        resultReceivedMultiplicacion++;
                        boolean A = false;
                        System.out.println(Mensaje.split(" ")[1]);
                        for(int i = 0; i < ServersMultiplicaciones.size(); i++)
                        {
                            if(ServersMultiplicaciones.get(i).Server.equals(Mensaje.split(" ")[1]))
                            {
                                ServersMultiplicaciones.get(i).doesItAppear =true;
                                A=true;
                                break;
                            }
                        }
                        if(!A)
                        {
                            Servers aux = new Servers();
                            aux.doesItAppear =true;
                            aux.Server = Mensaje.split(" ")[1];
                            ServersMultiplicaciones.add(aux);
                        }
                    }
                    System.out.println(Mensaje);
                    elsocket.close();
                } catch (IOException ex) {
                    break;
                }
            }
        }

    }

    public class ContadorMaximoAcusesMultiplicaciones implements Runnable
    {

        @Override
        public void run() {
            resultReceivedMultiplicacion = 0;
            try {
                long start = System.nanoTime();
                ServerSocket socketMultipliacion = new ServerSocket(localPort+3);
                ContadorAcusesMultiplicacion contar = new ContadorAcusesMultiplicacion(socketMultipliacion);
                Thread Vamoaver = new Thread(contar);
                Vamoaver.start();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                socketMultipliacion.close();
                socketMultipliacion=null;
                System.out.println("Numero de acuses de multiplicacion: "+ resultReceivedMultiplicacion);
                if(resultReceivedMultiplicacion < acusesNecesarios){
                    System.out.println("No hay suficientes servidores de multiplicacion, vuelva a intentarlo");
                }
                for(int i = 0; i < ServersMultiplicaciones.size(); i++)
                {
                    if (!ServersMultiplicaciones.get(i).doesItAppear)
                    {
                        //Mandar a levantar
                        int j=0;
                        String ServUp ="";
                        while(j< ServersMultiplicaciones.size())
                        {
                            if (ServersMultiplicaciones.get(j).doesItAppear)
                            {
                                ServUp = ServersMultiplicaciones.get(j).Server;
                                j= ServersMultiplicaciones.size()+20;
                            }
                            j++;
                        }
                        WakeUp Fall = new WakeUp(ServersMultiplicaciones.get(i).Server,ServUp);
                        Thread Lev = new Thread(Fall);
                        Lev.start();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    public  class ContadorAcusesDivision implements Runnable{

        boolean exit = true;
        ServerSocket acusesDivision;

        ContadorAcusesDivision(ServerSocket acusesDivision){
            this.acusesDivision = acusesDivision;
        }

        @Override
        public void run() {
            for(int i = 0; i < ServersDivisiones.size(); i++)
            {
                Servers A = new Servers();
                A.doesItAppear = false ;
                A.Server = ServersDivisiones.get(i).Server;
                ServersDivisiones.set(i,A);
            }
            while(exit){
                Socket elsocket;
                String Mensaje="";

                try{
                    elsocket = acusesDivision.accept();
                    DataInputStream in = new DataInputStream(elsocket.getInputStream());
                    Mensaje = in.readUTF();
                    if(Mensaje.split(" ")[4].equals("1"))
                    {
                        resultReceivedDivision++;
                        boolean A = false;
                        System.out.println(Mensaje.split(" ")[1]);
                        for(int i = 0; i < ServersDivisiones.size(); i++)
                        {
                            if(ServersDivisiones.get(i).Server.equals(Mensaje.split(" ")[1]))
                            {
                                ServersDivisiones.get(i).doesItAppear =true;
                                A=true;
                                break;
                            }
                        }
                        if(!A)
                        {
                            Servers aux = new Servers();
                            aux.doesItAppear =true;
                            aux.Server = Mensaje.split(" ")[1];
                            ServersDivisiones.add(aux);
                        }
                    }
                    System.out.println(Mensaje);
                    elsocket.close();
                } catch (IOException ex) {
                    break;
                }
            }
        }

    }

    public class ContadorMaximoAcusesDivisiones implements Runnable
    {

        @Override
        public void run() {
            resultReceivedDivision = 0;
            try {
                long start = System.nanoTime();
                ServerSocket socketDivisiones = new ServerSocket(localPort+4);
                ContadorAcusesDivision contar = new ContadorAcusesDivision(socketDivisiones);
                Thread Tcontar = new Thread(contar);
                Tcontar.start();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                socketDivisiones.close();
                socketDivisiones=null;
                System.out.println("Numero de acuses de division: "+ resultReceivedDivision);
                if(resultReceivedSumas < acusesNecesarios){
                    System.out.println("No hay suficientes servidores de division, vuelva a intentarlo");
                }
                for(int i = 0; i < ServersDivisiones.size(); i++)
                {
                    if (!ServersDivisiones.get(i).doesItAppear)
                    {
                        //Mandar a levantar
                        int j=0;
                        String ServUp ="";
                        while(j< ServersDivisiones.size())
                        {
                            if (ServersDivisiones.get(j).doesItAppear)
                            {
                                ServUp = ServersDivisiones.get(j).Server;
                                j= ServersDivisiones.size()+20;
                            }
                            j++;
                        }
                        WakeUp Fall = new WakeUp(ServersDivisiones.get(i).Server,ServUp);
                        Thread Lev = new Thread(Fall);
                        Lev.start();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }


    public class WakeUp implements Runnable
    {
        String hashSHA1;
        String isAlive;

        WakeUp(String hashSHA1, String isAlive){
            this.hashSHA1 = hashSHA1;
            this.isAlive = isAlive;
        }

        @Override
        public void run() {
            String message="";
            final String localHost ="127.0.0.1";
            DataOutputStream outSocketWakeUp;



            try {
                Socket socketWakeUp = new Socket(localHost, goToNode);
                outSocketWakeUp = new DataOutputStream(socketWakeUp.getOutputStream());

                outSocketWakeUp.writeUTF("0 "+ hashSHA1 +" "+ isAlive);


                socketWakeUp.close();

            } catch (IOException ex) {
                System.out.println("Fail in the connection");
            }
        }

    }
}


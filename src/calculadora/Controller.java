package calculadora;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author Hp
 */
public class Controller implements Initializable {

    String suma="";
    String resta="";
    String multiplicacion ="";
    String division ="";
    double makeDoubleNum =0;
    String operacion="";
    //Listas y arrays para operaciones
    List<String> Sumas = new ArrayList<String>();
    List<String> Restas = new ArrayList<String>();
    List<String> Multiplicaciones= new ArrayList<String>();
    List<String> Divisiones = new ArrayList<String>();
    static String value="";
    static ArrayList<String> aux = new ArrayList<>();
    static ArrayList<String> serverAcuses = new ArrayList<>();
    static ArrayList<String> listServers = new ArrayList<>();
    static ArrayList<String> listClients = new ArrayList<>();
    static ArrayList<String> listEventSuma = new ArrayList<>();
    static ArrayList<String> listEventResta = new ArrayList<>();
    static ArrayList<String> listEventMultiplicacion = new ArrayList<>();
    static ArrayList<String> listEventDivision = new ArrayList<>();
    //TIMESTAMP
    static Timestamp newTimeStampMillis =new Timestamp(System.currentTimeMillis());
    static  Date clientCreationDate =new Date(newTimeStampMillis.getTime());
    static String stringClientCreationDate = clientCreationDate.toString();
    //////////////
    String acumulate ="";
    //Puerto del nodo a conectar
    int goToNode =0;
    int incrementEvent =0;
    int folioSuma = 0;
    int folioResta = 0;
    int folioMultiplicacion = 0;
    int folioDivision = 0;

    @FXML
    private TextField ResultadoTotal;
    @FXML
    private TextArea ResultSuma;
    @FXML
    private TextArea ResultResta;
    @FXML
    private TextArea ResultMultiplicacion;
    @FXML
    private TextArea ResultDivivision;

    //Funcion para crear ss con el puerto bueno
    public ServerSocket create(int ports){
        for (int port= ports;port < 7000; port++) {
            try {
                return new ServerSocket(port);
            } catch (IOException ex) {
                continue; // try next port
            }
        }
        try {
            // if the program gets here, no port in the range was found
            throw new IOException("No hay puerto disponible para el ss");
        } catch (IOException ex) {
            Logger.getLogger(CalculadoraCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Abrimos server socket para el cliente
        ServerSocket clientServerSocket = create(6000);

        String message="";
        final String HOST ="127.0.0.1";
        //Iniciamos puerto en el 800, porque el nodo principal corre en ese puerto
        int nodePort =8000;
        //Canal de entrada y salida
        DataInputStream inClientSocket;
        DataOutputStream outClientSocket;
        //Bandera que busca la conexi�n
        var Searching=true;
        while(Searching){
            try {
                //Creamos socket en el puerto 8000
                Socket clientSocket = new Socket(HOST,nodePort);
                inClientSocket = new DataInputStream(clientSocket.getInputStream());
                outClientSocket = new DataOutputStream(clientSocket.getOutputStream());
                //Da a conocer al nodo que es cliente y le env�amos el puerto
                outClientSocket.writeUTF("0001 "+clientServerSocket.getLocalPort());
                //Mensaje que recibimos del nodo con el puerto
                message = inClientSocket.readUTF();
                if (Integer.parseInt(message) !=0)
                {
                    Searching = false;
                    break;
                }
                //Cerramos socket
                clientSocket.close();

            } catch (IOException ex) {
                System.out.println("No se pudo generar socket del cliente al nodo");
            } //Se le suma 200 para ver si en ese puerto se est� escuchando otro nodo y conectarlos en el siguiente while
            nodePort= nodePort+200;
            //Si excede de 60,0000 ya no se busca m�s
            if (nodePort>60000)
            {
                Searching = false;
            }
        }
        //Obtenemos puerto nodo-cliente
        goToNode = Integer.parseInt(message);
        //Generamos el hilo para escuchar en ese puertos
        ThreadReceiveMessage ReceiveMessage = new ThreadReceiveMessage(clientServerSocket);
        Thread TRM = new Thread(ReceiveMessage);
        TRM.start();
    }


    @FXML
    private void BotonesNumerosOperadoresIgual(ActionEvent event) {
        value=((Button)event.getSource()).getText();
        aux.add(value);
        System.out.println(value);
        switch(value) {
            //Case para cada boton
            case "0":
                acumulate = acumulate +"0";
                ResultadoTotal.setText(acumulate);
                break;
            case "1":
                acumulate = acumulate +"1";
                ResultadoTotal.setText(acumulate);
                break;
            case "2":
                acumulate = acumulate +"2";
                ResultadoTotal.setText(acumulate);
                break;
            case "3":
                acumulate = acumulate +"3";
                ResultadoTotal.setText(acumulate);
                break;
            case "4":
                acumulate = acumulate +"4";
                ResultadoTotal.setText(acumulate);
                break;
            case "5":
                acumulate = acumulate +"5";
                ResultadoTotal.setText(acumulate);
                break;
            case "6":
                acumulate = acumulate +"6";
                ResultadoTotal.setText(acumulate);
                break;
            case "7":
                acumulate = acumulate +"7";
                ResultadoTotal.setText(acumulate);
                break;
            case "8":
                acumulate = acumulate +"8";
                ResultadoTotal.setText(acumulate);
                break;
            case "9":
                acumulate = acumulate +"9";
                ResultadoTotal.setText(acumulate);
                break;
            case ".":
                acumulate = acumulate +".";
                ResultadoTotal.setText(acumulate);
                break;
            case "+":
                makeDoubleNum = Double.parseDouble(acumulate);
                operacion = "1000";
                acumulate = acumulate + " + ";
                ResultadoTotal.setText(acumulate);
                break;
            case "-":
                makeDoubleNum = Double.parseDouble(acumulate);
                operacion = "1001";
                acumulate = acumulate + " - ";
                ResultadoTotal.setText(acumulate);
                break;
            case "*":
                makeDoubleNum = Double.parseDouble(acumulate);
                operacion = "1100";
                acumulate = acumulate + " * ";
                ResultadoTotal.setText(acumulate);
                break;
            case "/":
                makeDoubleNum = Double.parseDouble(acumulate);
                operacion = "1101";
                acumulate = acumulate + " / ";
                ResultadoTotal.setText(acumulate);
                break;
            //Si damos igual, separamos el mensaje que son los botones que se presionan
            case "Enviar":
                String[] sendMessageSplit = acumulate.split(" ");
                String num1 = sendMessageSplit[0];
                String num2 = sendMessageSplit[2];
                String numerosAOperar = num1 + " "+ num2;

                Cifrar cifrado = new Cifrar();

                String texto = stringClientCreationDate;
                String huellaMD5Cliente = cifrado.md5(texto);
                listClients.add(huellaMD5Cliente);
                switch(operacion)
                {
                    case "1000": //Suma
                        suma=suma+"\n"+ acumulate;
                        String k = Integer.toString(incrementEvent);
                        Connection(("1000 "+numerosAOperar+" evento"+k+" "+huellaMD5Cliente),'+');
                        listEventSuma.add("Folio: "+ folioSuma+" "+"Evento: "+k+" "+"CC: 1000 "+" Numeros a Sumar: "+numerosAOperar+" Operacion: + "+" Hash MD5 del Cliente: "+huellaMD5Cliente);
                        incrementEvent++;
                        folioSuma++;
                        break;
                    case "1001": //Resta
                        resta=resta+"\n"+ acumulate;
                        String o = Integer.toString(incrementEvent);
                        Connection(("1001 "+numerosAOperar+" evento"+o+" "+huellaMD5Cliente),'-');
                        //lista_eventos_resta.add("1001"+" evento"+o+" "+numerosAOperar+" - "+ cifrado.sha1(texto)+ " "+huellaMD5Cliente);
                        listEventResta.add("Folio: "+ folioResta+" "+"Evento: "+o+" "+"CC: 1001 "+" Numeros a Restar: "+numerosAOperar+" Operacion: - "+" Hash MD5 del Cliente: "+huellaMD5Cliente);
                        incrementEvent++;
                        folioResta++;
                        break;
                    case "1100": //Multiplicacion
                        multiplicacion = multiplicacion +"\n"+ acumulate;
                        String x = Integer.toString(incrementEvent);
                        Connection(("1100 "+numerosAOperar+" evento"+x+" "+huellaMD5Cliente),'*');
                        //lista_eventos_mult.add("1100"+" evento"+x+" "+numerosAOperar+" * "+ cifrado.sha1(texto)+ " "+huellaMD5Cliente);
                        listEventMultiplicacion.add("Folio: "+ folioMultiplicacion+" "+"Evento: "+x+" "+"CC: 1100 "+" Numeros a Multiplicar: "+numerosAOperar+" Operacion: * "+" Hash MD5 del Cliente: "+huellaMD5Cliente);
                        incrementEvent++;
                        folioMultiplicacion++;
                        break;
                    case "1101": //Division
                        division = division +"\n"+ acumulate;
                        String y = Integer.toString(incrementEvent);
                        Connection(("1101 "+numerosAOperar+" evento"+y+" "+huellaMD5Cliente),'/');
                        listEventDivision.add("Folio: "+ folioDivision+" "+"Evento: "+y+" "+"CC: 1101 "+" Numeros a Dividir: "+numerosAOperar+" Operacion: / "+" Hash MD5 del Cliente: "+huellaMD5Cliente);
                        incrementEvent++;
                        folioDivision++;
                        break;
                }
                acumulate ="";
                ResultadoTotal.setText(acumulate);
                break;

            default:
                break;
        }
    }



    //funcion donde se recibe el resultado y el tipo de operaci�n
    public void Connection(String Total, char operacion)
    {
        //inicializamos
        String message="";
        final String HOST ="127.0.0.1";
        DataInputStream inConnectNode;
        DataOutputStream outConnectNode;

        try {
            //Generamos socket
            Socket connectNode = new Socket(HOST, goToNode);
            outConnectNode = new DataOutputStream(connectNode.getOutputStream());
            //Total contiene n�mero de case, resultado,operaci�n
            System.out.println(Total);
            outConnectNode.writeUTF(Total);


            connectNode.close();

        } catch (IOException ex) {
            System.out.println("No se genero socket");
        }
    }

    public class ThreadReceiveMessage implements Runnable{

        ServerSocket newClientCreated;

        public ThreadReceiveMessage(ServerSocket clientServerSocketCreated) {
            this.newClientCreated = clientServerSocketCreated;
        }


        @Override
        public void run() {
            while(true)
            {
                Socket socketClientCreated;
                String message="";
                try{
                    System.out.println("Se acaba de conectar socket al server "+ newClientCreated.getLocalPort());
                    socketClientCreated = newClientCreated.accept();
                    DataInputStream in = new DataInputStream(socketClientCreated.getInputStream());
                    message = in.readUTF();
                    System.out.println("Se recibi� mensaje del server: " + message);
                    socketClientCreated.close();
                } catch (IOException ex) {
                    System.out.println("Fallo la conexion");
                }
                String[] op = message.split(" ");
                String finalOperation = op[0];
                String finalNum1 = op[1];
                String finalNum2 = op[2];
                String finalAnswer = op[3];
                String clientConnected = op[5];
                //op[4] tiene el hash de los servidores
                System.out.println("Hash de validacion del server conectado: " + op[4]);
                //Pausamos el cliente un segundo para esperar los acuses totales
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                //Verificamos si la lista contiene acuses
                if(listServers.isEmpty()) {
                    listServers.add(op[4]);
                    serverAcuses.add(op[4]);
                }else {

                    if(listServers.contains(op[4])) {
                        serverAcuses.add(op[4]);

                    }else {
                        listServers.add(op[4]);
                        serverAcuses.add(op[4]);
                    }
                }
                if(serverAcuses.size() >= 3 && listClients.contains(clientConnected)){
                    //ResultSuma.appendText( "Acuses completos\n");
                    System.out.println("Los acuses estan completos");
                    System.out.println("Lista de servidores existentes: "+ listServers);
                    System.out.println("Lista de foliaje de las sumas: "+ listEventSuma);
                    System.out.println("Lista de foliaje de las restas: "+ listEventResta);
                    System.out.println("Lista de foliaje de las multiplicaciones: "+ listEventMultiplicacion);
                    System.out.println("Lista de foliaje de las divisiones: "+ listEventDivision);
                    if(Integer.parseInt(op[0]) >= 5)
                    {
                        String finalResult ="";
                        switch (finalOperation)
                        {
                            case "0111": //Resultados de la Suma
                                finalResult = finalNum1 +" + "+  finalNum2 + " = " + finalAnswer;
                                ResultSuma.appendText(finalResult + "\n");
                                break;
                            case "0110": //Resultados de la Resta
                                finalResult = finalNum1 +" - "+  finalNum2+ " = " + finalAnswer;
                                ResultResta.appendText(finalResult+ "\n");
                                break;
                            case "0011": //Resultados de la Multiplicacion
                                finalResult = finalNum1 +" * "+  finalNum2+ " = " + finalAnswer;
                                ResultMultiplicacion.appendText(finalResult+ "\n");
                                break;
                            case "1010": //Resultados de la Division
                                finalResult = finalNum1 +" / "+  finalNum2+ " = " + finalAnswer;
                                ResultDivivision.appendText(finalResult+ "\n");
                                break;
                        }
                        // lista_servidores.clear();
                        System.out.println("Lista de los hash MD5 de los acuses de los servidores: ");
                        System.out.println(serverAcuses);
                        serverAcuses.clear();
                    }
                }else {
                    System.out.println("Acuses incompletos");
                    //ResultSuma.appendText( "Acuses incompletos\n");
                    //Aqu� generamos otro servidor
                    // Runtime runTime = Runtime.getRuntime();

                    //String executablePath = "cmd /c start C:\\Users\\compi.exe";

            	 /* try {
					Process process = runTime.exec(executablePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
                }



            }

        }

    }
    @FXML
    private void BotonBorrar(ActionEvent event) {
        ResultadoTotal.setText("");
        acumulate = "";
    }

}


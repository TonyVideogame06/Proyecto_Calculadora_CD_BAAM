package calculadora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculadoraServidor {

    static public ServerSocket create(int[] ports) throws IOException {
        for (int port : ports) {
            try {
                return new ServerSocket(port);
            } catch (IOException ex) {
                continue; // try next port
            }
        }
        throw new IOException("no free port found");
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

    public static void main(String[] args) throws IOException {

        if(args.length==0){
            String hashSHA1="";
            try {
                hashSHA1 =   sha1(String.valueOf(System.currentTimeMillis()));
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CalculadoraServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("El hash SHA1 de este servidor es: "+hashSHA1);
            int ports[] = new int[500];
            int j =0;
            for(int i=0;i<ports.length; i++)
            {
                ports[i]=j+7000;
                j+=2;
            }
            ServerSocket serverSocketClientServer = create(ports);

            String nodeMessage="";
            final String localHost ="127.0.0.1";
            int nodePort =8000;
            DataInputStream inSockectClientServer;
            DataOutputStream outSockectClientServer;
            var searching=true;
            while(searching){
                try {
                    Socket sockectClientServer = new Socket(localHost,nodePort);
                    inSockectClientServer = new DataInputStream(sockectClientServer.getInputStream());
                    outSockectClientServer = new DataOutputStream(sockectClientServer.getOutputStream());

                    outSockectClientServer.writeUTF("Servidor "+serverSocketClientServer.getLocalPort());

                    nodeMessage = inSockectClientServer.readUTF();
                    if (Integer.parseInt(nodeMessage) !=0)
                    {
                        searching = false;
                        break;
                    }
                    sockectClientServer.close();

                } catch (IOException ex) {
                    System.out.println("Fail in the connection");
                }
                nodePort=nodePort+200;
                if (nodePort>60000)
                {
                    searching = false;
                }
            }

            ReceiveData serverOperation = new ReceiveData(serverSocketClientServer,hashSHA1,nodeMessage,"1","1","1","1");
            int myPort = serverOperation.serverSocketReceiveData.getLocalPort();
            Thread TServerOperation = new Thread(serverOperation);

            try {
                File myObj = new File("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Duplicacion\\"+hashSHA1+".txt");
                if (myObj.createNewFile()) {
                    try {
                        FileWriter writeANewFile = new FileWriter("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Duplicacion\\"+hashSHA1+".txt");
                        writeANewFile.write(hashSHA1+"\n");
                        writeANewFile.write(myPort+"\n");
                        writeANewFile.write(nodeMessage+"\n");
                        writeANewFile.write(1+"\n");
                        writeANewFile.write(1+"\n");
                        writeANewFile.write(1+"\n");
                        writeANewFile.write(1+"\n");
                        writeANewFile.close();
                    } catch (IOException e) {
                        System.out.println("An error occurred");
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("File already exists.");
                }
            }
            catch (IOException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }

            TServerOperation.start();
        }else
        {
            if(args[0].equals("1") ||args[0].equals("0") )
            {
                String hashSHA1="";
                try {
                    hashSHA1 =   sha1(String.valueOf(System.currentTimeMillis()));
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(CalculadoraServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(hashSHA1);
                int ports[] = new int[500];
                int j =0;
                for(int i=0;i<ports.length; i++)
                {
                    ports[i]=j+7000;
                    j+=2;
                }
                ServerSocket Servidor = create(ports);

                String anotherNodeMessage="";
                final String localHost ="127.0.0.1";
                int nodePort =8000;
                DataInputStream inAnotherSockectClientServer;
                DataOutputStream out;
                var searching=true;
                while(searching){
                    try {
                        Socket anotherSockectClientServer = new Socket(localHost,nodePort);
                        inAnotherSockectClientServer = new DataInputStream(anotherSockectClientServer.getInputStream());
                        out = new DataOutputStream(anotherSockectClientServer.getOutputStream());

                        out.writeUTF("Servidor "+Servidor.getLocalPort());

                        anotherNodeMessage = inAnotherSockectClientServer.readUTF();
                        if (Integer.parseInt(anotherNodeMessage) !=0)
                        {
                            searching = false;
                            break;
                        }
                        anotherSockectClientServer.close();

                    } catch (IOException ex) {
                        System.out.println("Fail in the connection");
                    }
                    nodePort=nodePort+200;
                    if (nodePort>60000)
                    {
                        searching = false;
                    }
                }

                ReceiveData Serv= new ReceiveData(Servidor,hashSHA1,anotherNodeMessage,args[0],args[1],args[2],args[3]);
                int mipuerto = Serv.serverSocketReceiveData.getLocalPort();
                Thread llegada = new Thread(Serv);

                try {
                    File myObj = new File("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Duplicacion\\"+hashSHA1+".txt");
                    if (myObj.createNewFile()) {
                        try {
                            FileWriter fileWriter = new FileWriter("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Duplicacion\\"+hashSHA1+".txt");
                            fileWriter.write(hashSHA1+"\n");
                            fileWriter.write(mipuerto+"\n");
                            fileWriter.write(anotherNodeMessage+"\n");
                            fileWriter.write(args[0]+"\n");
                            fileWriter.write(args[1]+"\n");
                            fileWriter.write(args[2]+"\n");
                            fileWriter.write(args[3]+"\n");
                            fileWriter.close();
                        } catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }

                    } else {
                        System.out.println("File already exists.");
                    }
                }
                catch (IOException e) {
                    System.out.println("An error occurred");
                    e.printStackTrace();
                }

                llegada.start();
            }
            else
            {
                File myObj = new File(args[0]+".txt");
                Scanner myReader = new Scanner(myObj);
                String hash = myReader.nextLine();
                String port = myReader.nextLine();
                String node = myReader.nextLine();
                String operationSumar = myReader.nextLine();
                String operationRestar = myReader.nextLine();
                String operationMultiplicar = myReader.nextLine();
                String operationDivision = myReader.nextLine();
                myReader.close();
                ServerSocket  anotherClientServerSocket = null;
                try{
                    anotherClientServerSocket = new ServerSocket(Integer.parseInt(port));
                }catch(IOException ex){
                    System.exit(1);
                }
                ReceiveData anotherClientServer= new ReceiveData(anotherClientServerSocket,hash,node, operationSumar, operationRestar, operationMultiplicar, operationDivision);
                Thread TAnotherClientServer = new Thread(anotherClientServer);
                TAnotherClientServer.start();
            }
        }

    }
}




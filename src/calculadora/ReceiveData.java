package calculadora;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Base64;


public class ReceiveData implements Runnable{

    public ServerSocket serverSocketReceiveData;
    public String hashSHA1;
    int port;
    String operationSumar;
    String operationRestar;
    String operationMultiplicar;
    String operationDividir;

    int haveSuma =0;
    int haveResta =0;
    int haveMultiplicacion =0;
    int haveDivision =0;

    public ReceiveData(ServerSocket ports, String hashSHA1, String port, String operationSumar, String operationRestar, String operationMultiplicar, String operationDividir) throws IOException {
        this.serverSocketReceiveData = ports;
        this.hashSHA1 = hashSHA1;
        this.port = Integer.parseInt(port);
        this.operationSumar = operationSumar;
        this.operationRestar = operationRestar;
        this.operationMultiplicar = operationMultiplicar;
        this.operationDividir = operationDividir;
    }

    private void writeBytesToFile(String jarDivision, byte[] fileBytes) throws IOException{
        try (FileOutputStream fos = new FileOutputStream(jarDivision)) {
            fos.write(fileBytes);
        }
    }

    public class calculateOperations implements Runnable
    {
        String serverMessage;
        int port;
        int localPort;
        String operationSuma;
        String operationResta;
        String operationMultiplicacion;
        String operationDivision;

        calculateOperations(String serverMessage, int port, int localPort, String operationSuma, String operationResta, String operationMultiplicacion, String operationDivision){
            this.serverMessage = serverMessage;
            this.port = port;
            this.localPort = localPort;
            this.operationSuma = operationSuma;
            this.operationResta = operationResta;
            this.operationMultiplicacion = operationMultiplicacion;
            this.operationDivision = operationDivision;
            System.out.println(port);
        }

        @Override
        public void run() {
            URL[] classLoaderUrls;
            URLClassLoader urlClassLoader;
            Class<?> clazz;
            String operationFileName = "";
            String[] serverMessageSplit = serverMessage.split(" ");
            String operation = serverMessageSplit[0];
            switch (operation) {
                case "1":
                    operationFileName = "Sumar";
                    break;
                case "2":
                    operationFileName = "Restar";
                    break;
                case "3":
                    operationFileName = "Multiplicar";
                    break;
                case "4":
                    operationFileName = "Dividir";
                    break;
            }

            try {
                System.out.println("Pidiendo a realizar la operacion " + operationFileName);
                classLoaderUrls = new URL[]{new URL("file:////D:/Documentos/UniversidadPanamericana/Semestres/Computo Distribuido/Proyecto/Proyecto_Calculadora_ArandaMejiaBrianAntonio/out/artifacts/Operaciones/" +operationFileName + ".jar")};
                urlClassLoader = new URLClassLoader(classLoaderUrls);
                clazz = urlClassLoader.loadClass(operationFileName.toLowerCase()+ "." +operationFileName);
                Constructor<?> ct = clazz.getConstructor(String.class, int.class);
                ct.newInstance(serverMessage, port);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
        while(true)
        {
            Socket socketReceiveData;
            String serverMessage="";
            int i=0;
            try {
                System.out.println("Connection with: "+ serverSocketReceiveData.getLocalPort());
                socketReceiveData = serverSocketReceiveData.accept();
                DataInputStream inSocketReceiveData = new DataInputStream(socketReceiveData.getInputStream());
                DataOutputStream outSocketReceiveData = new DataOutputStream(socketReceiveData.getOutputStream());
                serverMessage = inSocketReceiveData.readUTF();
                System.out.println(serverMessage);
                String[] serverMessageSplit = serverMessage.split(" ");
                String checkServer = serverMessageSplit[0];
                String checkOperation = serverMessageSplit[1];
                String checkHashSHA1 = serverMessageSplit[2];
                String checkBase64HashSHA1 = serverMessageSplit[3];
                //i=Integer.parseInt(serverMessageSplit[0]);
                i=Integer.parseInt(checkServer);
                String j="0";
                Process process = null;
                //switch(serverMessageSplit[0])
                switch(checkServer)
                {
                    case "0":
                        //if(serverMessageSplit[2].equals(hashSHA1))
                        if(checkHashSHA1.equals(hashSHA1))
                        {
                            //if(serverMessageSplit[1].equals("1")||serverMessageSplit[1].equals("2")||serverMessageSplit[1].equals("3")||serverMessageSplit[1].equals("4")){
                            if(checkOperation.equals("1")||checkOperation.equals("2")||checkOperation.equals("3")||checkOperation.equals("4")){
                                byte[] decode = Base64.getDecoder().decode(checkBase64HashSHA1);
                                try {
                                    switch (checkOperation){
                                        case "1":
                                            writeBytesToFile("D:/Documentos/UniversidadPanamericana/Semestres/Computo Distribuido/Proyecto/Proyecto_Calculadora_ArandaMejiaBrianAntonio/out/artifacts/Operaciones/Sumar.jar", decode);
                                            haveSuma =1;
                                            break;
                                        case "2":
                                            writeBytesToFile("D:/Documentos/UniversidadPanamericana/Semestres/Computo Distribuido/Proyecto/Proyecto_Calculadora_ArandaMejiaBrianAntonio/out/artifacts/Operaciones/Restar.jar", decode);
                                            haveResta =1;
                                            break;
                                        case "3":
                                            writeBytesToFile("D:/Documentos/UniversidadPanamericana/Semestres/Computo Distribuido/Proyecto/Proyecto_Calculadora_ArandaMejiaBrianAntonio/out/artifacts/Operaciones/Multiplicar.jar", decode);
                                            haveMultiplicacion =1;
                                            break;
                                        case "4":
                                            writeBytesToFile("D:/Documentos/UniversidadPanamericana/Semestres/Computo Distribuido/Proyecto/Proyecto_Calculadora_ArandaMejiaBrianAntonio/out/artifacts/Operaciones/Dividir.jar", decode);
                                            haveDivision =1;
                                            break;
                                    }
                                } catch (IOException ex) {
                                    System.out.println("Error making the file");
                                }
                            }
                            else
                            {
                                process = Runtime.getRuntime().exec("cmd /c java -jar \"D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\CalculadoraServidor\\CalculadoraServidor.jar\" \"D:\\\\Documentos\\\\UniversidadPanamericana\\\\Semestres\\\\Computo Distribuido\\\\Proyecto\\\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\\\src\\\\Duplicacion\\\\"+checkOperation+"\"");
                            }
                        }
                        break;
                    case "1":
                        if(haveSuma ==1)
                        {
                            j= operationSumar;
                        }
                        else
                        {
                            i=0;
                        }
                        break;
                    case "2":
                        if(haveResta ==1)
                        {
                            j= operationRestar;
                        }
                        else
                        {
                            i=0;
                        }
                        break;
                    case "3":
                        if(haveMultiplicacion ==1)
                        {
                            j= operationMultiplicar;
                        }
                        else
                        {
                            i=0;
                        }
                        break;
                    case "4":
                        if(haveDivision ==1)
                        {
                            j= operationDividir;
                        }
                        else
                        {
                            i=0;
                        }
                        break;
                }
                outSocketReceiveData.writeUTF("9 "+ hashSHA1 + " " + serverMessageSplit[0]+ " " +serverMessageSplit[2] + " " +j);
                socketReceiveData.close();
            } catch (IOException ex) {
                System.out.println("Fail in the connection");
            }
            if(i>0)
            {
                calculateOperations makeOperation = new calculateOperations(serverMessage, port, serverSocketReceiveData.getLocalPort(), operationSumar, operationRestar, operationMultiplicar, operationDividir);
                Thread threadMakeOperation = new Thread(makeOperation);
                threadMakeOperation.start();
            }
        }

    }
}

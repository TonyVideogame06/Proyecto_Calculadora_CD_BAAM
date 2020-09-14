package calculadora;

import calculadora.CalculadoraCliente.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;




public class Controller implements Initializable {

    String aCalcularEnServidor ="";
    String nuevoPrimerNumero="";
    String nuevoSegundoNumero="";
    String nuevoOperador="";
    String guardarCalculo="";
    String resultadosSumas="";
    String resultadosRestas="";
    String resultadosMultiplicaciones="";
    String resultadosDivisiones="";
    String aCalcularFormateado = "";
    boolean numero1 = true;
    boolean numero2 = false;
    ArrayList<String> operacionesRealizadas = new ArrayList<String>();
    ArrayList<String> guardarCalculoComas = new ArrayList<String>();
    ArrayList<String> resultadoPrueba = new ArrayList<String>();
    int i=0;

    @FXML
    private TextField numeroOperador;

    @FXML
    private Label labelSuma;

    @FXML
    private Label labelResta;

    @FXML
    private Label labelMultiplicacion;

    @FXML
    private Label labelDivision;

    @FXML
    private void BotonesNumeros(ActionEvent event) {
        //numeroOperador.setText("");
        //aCalcular = nuevoOperador + ",";
        Button btn = (Button) event.getSource();
        if (numero1)
        {
            nuevoPrimerNumero = numeroOperador.getText() + btn.getText();
            numeroOperador.setText(nuevoPrimerNumero);
        }
        if (numero2)
        {
            nuevoSegundoNumero = numeroOperador.getText() + btn.getText();
            numeroOperador.setText(nuevoSegundoNumero);
            //numero1=false;
        }
    }
    @FXML
    private void BotonesOperadores(ActionEvent event) {
        numero1 = false;
        numero2 = true;
        //numeroOperador.setText("");
        //aCalcular = nuevoNumero + ",";
        Button btn = (Button) event.getSource();
        switch (btn.getText())
        {
            case "+":
                nuevoOperador = "1";
                break;
            case "-":
                nuevoOperador = "2";
                break;
            case "*":
                nuevoOperador = "3";
                break;
            case "/":
                nuevoOperador = "4";
                break;
        }
        //nuevoOperador = btn.getText();
        //numeroOperador.setText(nuevoOperador);
        numeroOperador.setText("");

    }

    @FXML
    public void BotonCalcularResultado(ActionEvent event) {
        numero1 = true;
        numero2 = false;
        //Host del servidor
        final String host = "127.0.0.1";
        //Puerto del servidor
        final int portClient = 4998;
        final int portNodo = 5000;
        DataInputStream in;
        DataOutputStream out;

        try {
            //Creo el socket para conectarme con el cliente
            Socket sc = new Socket(host, portNodo);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            //Envio un mensaje al cliente
            System.out.println(nuevoPrimerNumero);
            System.out.println(nuevoOperador);
            System.out.println(nuevoSegundoNumero);
            aCalcularEnServidor = "Client," + nuevoOperador + "," + nuevoPrimerNumero + "," + nuevoSegundoNumero;
            switch (nuevoOperador)
            {
                case "1":
                    nuevoOperador = "+";
                    break;
                case "2":
                    nuevoOperador = "-";
                    break;
                case "3":
                    nuevoOperador = "*";
                    break;
                case "4":
                    nuevoOperador = "/";
                    break;
            }
            guardarCalculo = nuevoPrimerNumero + nuevoOperador + nuevoSegundoNumero + "=";
            operacionesRealizadas.add(guardarCalculo);
            aCalcularFormateado = nuevoOperador + "," + nuevoPrimerNumero + "," + nuevoSegundoNumero;
            guardarCalculoComas.add(aCalcularFormateado);
            System.out.println(aCalcularEnServidor);
            System.out.println(operacionesRealizadas);
            System.out.println(operacionesRealizadas.size());
            out.writeUTF(aCalcularEnServidor);
            String resultado = in.readUTF();
            System.out.println(resultado);
            resultadoPrueba.add(resultado);
            numeroOperador.setText(resultado);
            do {
                String[] imprimirCalculoSplit = guardarCalculoComas.get(i).split(",");
                switch(imprimirCalculoSplit[0]) {
                    case "+":
                        resultadosSumas += operacionesRealizadas.get(i) + resultadoPrueba.get(i) + "\n";
                        labelSuma.setText(resultadosSumas);
                        i++;
                        break;
                    case "-":
                        resultadosRestas += operacionesRealizadas.get(i) + resultadoPrueba.get(i) + "\n";
                        labelResta.setText(resultadosRestas);
                        i++;
                        break;
                    case "*":
                        resultadosMultiplicaciones += operacionesRealizadas.get(i) + resultadoPrueba.get(i) + "\n";
                        labelMultiplicacion.setText(resultadosMultiplicaciones);
                        i++;
                        break;
                    case "/":
                        resultadosDivisiones += operacionesRealizadas.get(i) + resultadoPrueba.get(i) + "\n";
                        labelDivision.setText(resultadosDivisiones);
                        i++;
                        break;

                }
            }while (i != operacionesRealizadas.size());

            aCalcularEnServidor = "";
            nuevoPrimerNumero = "";
            nuevoSegundoNumero = "";
            nuevoOperador = "";
            guardarCalculo = "";

            System.out.println(resultado);

            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /*@FXML
    private void imprimirResultados(ActionEvent event) {
        for (int i = 0; i < operacionesRealizadas.size(); i++) {
            String[] imprimirCalculoSplit = guardarCalculoComas.get(i).split(",");
            switch(imprimirCalculoSplit[1]) {
                case "+":
                    labelSuma.setText(operacionesRealizadas.get(i));
                    break;
                case "-":
                    labelResta.setText(operacionesRealizadas.get(i));
                    break;
                case "*":
                    labelMultiplicacion.setText(operacionesRealizadas.get(i));
                    break;
                case "/":
                    labelDivision.setText(operacionesRealizadas.get(i));
                    break;

            }
        }

    }*/

    @FXML
    private void BotonBorrar(ActionEvent event) {
        numeroOperador.setText("");
    }

    @FXML
    private void BotonOperacion(ActionEvent event) {
        numeroOperador.setText("");
        Button btn = (Button) event.getSource();
        //nuevoNumero = numeroOperador.getText() + btn.getText();
        //numeroOperador.setText(nuevoNumero);
        //nuevoNumero = nuevoNumero + ",";
        //System.out.println(nuevoNumero);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}


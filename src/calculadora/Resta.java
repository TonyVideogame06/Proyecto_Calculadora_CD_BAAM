package calculadora;

import java.io.FileWriter;
import java.io.IOException;

public class Resta {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("Argumentos: "+args[0]+" "+args[1]);
        String n1 = args[0];
        String n2 = args[1];
        double num1 = Double.parseDouble(n1);
        double num2 = Double.parseDouble(n2);
        double resultado = num1 - num2;
        String res = Double.toString(resultado);
        try {
            FileWriter writer = new FileWriter("D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\src\\Operaciones\\resta.txt", false);
            writer.write(res);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

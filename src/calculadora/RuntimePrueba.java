package calculadora;


public class RuntimePrueba {


        public static void main(String[] args) {
        /*try {

            // print a message
            System.out.println("Executing notepad.exe");

            // create a process and execute notepad.exe
            Process process = Runtime.getRuntime().exec("cmd /c start java -jar \"D:\\Documentos\\UniversidadPanamericana\\Semestres\\Computo Distribuido\\Proyecto\\Proyecto_Calculadora_ArandaMejiaBrianAntonio\\out\\artifacts\\Nodos\\Nodos.jar\"");

            // print another message
            System.out.println("Notepad should now open.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
            Cifrar objCifrar = new Cifrar();

            String texto = "hola";
            System.out.println("Texto 'hola' cifrado con MD5 " + objCifrar.md5(texto));
            System.out.println("Texto 'hola' cifrado con SHA1 " + objCifrar.sha1(texto));
        }
    }

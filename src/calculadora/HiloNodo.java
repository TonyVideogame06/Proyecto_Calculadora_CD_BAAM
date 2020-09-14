package calculadora;

public class HiloNodo {
    public static void main(String[] args) {
        Nodo conexionServidor = new Nodo();
        Thread hiloNodo = new Thread(conexionServidor);
        hiloNodo.start();
    }
}

package Logica;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable{

    private ServerSocket servidor;
    private String ip = "localhost";

    private Socket cliente;

    private Socket miServidor;
    public ObjectInputStream datosEntrada;
    public ObjectOutputStream datosSalida;
    private int puerto;
    private boolean yourTurn;
    private boolean circle;

    private boolean activado;
    public Triki estadoTriki;
    public OutputStream outStream;

    public Servidor() {
        activado = false;
        puerto = 5612;
    }

    public void activar(boolean b) {
        activado = b;
    }


    public void run() {

                try {
                    this.escucharClientes();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


    }

    public void escucharClientes() throws IOException, ClassNotFoundException {
        Socket cliente = null;

        cliente = this.servidor.accept();
        datosSalida =  new ObjectOutputStream(cliente.getOutputStream());

        datosEntrada =new ObjectInputStream(cliente.getInputStream());
        System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
//        activado = true;
        while (activado) {


            estadoTriki = (Triki) datosEntrada.readObject();
            System.out.println(estadoTriki);

//        InputStream inStream = cliente.getInputStream();
            outStream = cliente.getOutputStream();

            datosSalida.writeObject(estadoTriki);
        }




    }

    public void initializeServer() {
        try {
            servidor = new ServerSocket(puerto, 8, InetAddress.getByName(ip));
        } catch (Exception e) {
            e.printStackTrace();
        }
        yourTurn = true;
        circle = false;
    }

    // *********** Funcion para unirse al juego ***********

//    private boolean iniciar() {
//        try {
//            miServidor = new Socket(ip, puerto);
//            datosEntrada = new DataInputStream(miServidor.getInputStream());
//            datosSalida = new DataOutputStream(miServidor.getOutputStream());
//            activado = true;
//        } catch (IOException e) {
//            System.out.println("Unable to connect to the address: " + ip + ":" + puerto + " | Starting a server");
//            return false;
//        }
//        System.out.println("Successfully connected to the server.");
//        return true;
//    }


}

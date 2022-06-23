package Logica;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private ServerSocket serverSocket;
    private String ip = "localhost";
    private int puerto ;

    private String ipCliente;
    private String puertoCliente;

    private Socket socket;

    private DataOutputStream datosSalida;

    public DataOutputStream getDatosSalida() {
        return datosSalida;
    }

    public void setDatosSalida(DataOutputStream datosSalida) {
        this.datosSalida = datosSalida;
    }

    public DataInputStream getDatosEntrada() {
        return datosEntrada;
    }

    public void setDatosEntrada(DataInputStream datosEntrada) {
        this.datosEntrada = datosEntrada;
    }

    private DataInputStream datosEntrada;
    private boolean unableToCommunicateWithOpponent;

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    private boolean activado;

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public void setSocket(String ipCliente, String puertoCliente){
        this.ipCliente = ipCliente;
        this.puertoCliente = puertoCliente;
    }

    public String getIpCliente(){
        return ipCliente;
    }

    public String getPuertoCliente(){
        return puertoCliente;
    }

    private boolean aceptado = false;

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    private boolean yourTurn;

    public boolean isCircle() {
        return circle;
    }

    public void setCircle(boolean circle) {
        this.circle = circle;
    }

    private boolean circle;

    public Servidor() {
        activado = false;
        puerto = 5313;
    }

    public void activar(boolean b) {
        activado = b;
    }

    public void escucharClientes(){
        Socket socket = null;

        try {
            socket = this.serverSocket.accept();
            this.datosSalida = new DataOutputStream(socket.getOutputStream());
            this.datosEntrada = new DataInputStream(socket.getInputStream());
            this.aceptado = true;
            System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public boolean conectar() {
        try {
            this.socket = new Socket(this.ip, this.puerto);
            this.datosSalida = new DataOutputStream(this.socket.getOutputStream());
            this.datosEntrada = new DataInputStream(this.socket.getInputStream());
            this.aceptado = true;
        } catch (IOException var2) {
            System.out.println("Unable to connect to the address: " + this.ip + ":" + this.puerto + " | Starting a server");
            return false;
        }

        System.out.println("Successfully connected to the server.");
        return true;
    }

    public void initializeServer() {
        try {
            this.serverSocket = new ServerSocket(puerto, 8, InetAddress.getByName(ip));
        } catch (Exception e) {
            e.printStackTrace();
        }
        yourTurn = true;
        circle = false;
    }
}

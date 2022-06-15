package Logica;


import Presentacion.Modelo;
import Presentacion.VistaJuego;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Cliente  {

    private String ip;

    private Socket miServidor;
    public ObjectInputStream datosEntrada;
    public ObjectOutputStream datosSalida;
    private int puerto;
    private boolean yourTurn;
    private boolean circle;

    private boolean activado;
    public Triki estadoTriki;

    private VistaJuego ventana;
    private Modelo modelo;


    public Cliente(VistaJuego v) {
        ventana = v;
        modelo= v.getMiModelo();
    }



    //    private Triki getEstadoTriki() {
//        if (estadoTriki == null) {
//            estadoTriki = new Triki();
//        }
//        return estadoTriki;
//    }
    // *********** Funcion para unirse al juego ***********
    public boolean conectar( ) throws  IOException, ClassNotFoundException {

        ip = "localhost";
        puerto = 5612;

        try {
            miServidor = new Socket(ip, puerto);

            datosSalida = new ObjectOutputStream(miServidor.getOutputStream());
            datosEntrada = new ObjectInputStream(miServidor.getInputStream());


            while(true){
                estadoTriki = (Triki) datosEntrada.readObject();


                modelo.updateSistema(estadoTriki);

                System.out.println("Mensaje del servidor: " + estadoTriki);
            }



        } catch (IOException e) {
            System.out.println("Unable to connect to the address: " + ip + ":" + puerto + " | Starting a server");
            return false;
        }
    }


}

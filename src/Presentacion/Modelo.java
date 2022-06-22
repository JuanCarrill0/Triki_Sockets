/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Logica.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juan
 */
public class Modelo implements Runnable{
    private Triki triki;
    private VistaJuego ventanaJuego;
    private VistaMenu ventanaMenu;
    private ConfiguracionDibujo config;
    private boolean reiniciar;
    public int numeroServidor = 1;
    
    private Canvas lienzo;
    private Thread hiloDibujo;
    private BufferedImage dobleBuffer;
    private Servidor appServidor;



    public Modelo(){
        config = new ConfiguracionDibujo(); 
        getMiSistema().setActivo(true);
        hiloDibujo = new Thread(this);
    }

    public Servidor getAppServidor() {
        if(appServidor == null){
            if(this.ventanaJuego != null){
                appServidor = new Servidor();            
            }
        }
        return appServidor;
    }    
    
    public void iniciar(){
        getVentanaMenu().setVisible(true);
    }
   
    public VistaJuego getVentanaJuego(){
        if(ventanaJuego==null){
            ventanaJuego = new VistaJuego(this);
            ventanaJuego.setTitle("TRIKI");
            ventanaJuego.setResizable(false);
        }
        return ventanaJuego;
    }
    
    public VistaMenu getVentanaMenu(){
        if(ventanaMenu == null){
            ventanaMenu = new VistaMenu(this);
            ventanaMenu.setTitle("Menu TRIKI");
            ventanaMenu.setResizable(false);
        }
        return ventanaMenu;
    }
    
    public Thread getHiloDibujo(){
        return this.hiloDibujo;
    }

    public Triki getMiSistema(){
        if(triki==null){
            triki = new Triki();
        }
        return triki;
    }
    
    public boolean getReiniciar(){
        return reiniciar;
    }
    
    public void setReiniciar(boolean reiniciar){
        this.reiniciar = reiniciar;
    }
    
    public void determinarGanador(){
	char Ganador = getMiSistema().encontrarGanador();
        System.out.println("Ganador -> "+ Ganador);
	if (Ganador != ' ') {
		getVentanaJuego().mostrarMensaje("Jugador " + Ganador + " Ganó!");
                setReiniciar(true);
	}

	if (getMiSistema().esEmpate()) {
		getVentanaJuego().mostrarMensaje("Empate!");
                setReiniciar(true);
	}
    }
    public void mostrarCeldaSeleccionada(int mx, int my){
        boolean dentro = false;
        System.out.println("Entré");
        if(mx > config.getInicioTableroX() && mx < config.getInicioTableroX()+(3*config.getAnchoCelda())){
            if(my > config.getInicioTableroY() && my < config.getInicioTableroY()+(3*config.getAltoCelda())){                
                dentro = true;
            }else{
                dentro = false;
            }
        }else{
            dentro = false;
        }
        
        if(dentro){
            getMiSistema().getCeldaSeleccionada().f = (int)(my - config.getInicioTableroY())/config.getAltoCelda();
            System.out.println(getMiSistema().getCeldaSeleccionada().f);
            getMiSistema().getCeldaSeleccionada().c = (int)(mx - config.getInicioTableroX())/config.getAnchoCelda();
            System.out.println(getMiSistema().getCeldaSeleccionada().c);
        }else{
            getMiSistema().getCeldaSeleccionada().f = -1;
            getMiSistema().getCeldaSeleccionada().c = -1;
        }
        //System.out.println("x: " + mx + ", y: " + my);
            
    }
    
    public void establecerTurnoJugado(){
        int n;
        Punto2D cs = getMiSistema().getCeldaSeleccionada();
    
        if(cs.f != -1 && cs.c != -1){
            if(getMiSistema().hacerMovimiento(cs.f, cs.c)!= true){
                getVentanaJuego().mostrarMensaje("Espacio ya tomado!");
            }
            determinarGanador();
            try {
                
                // Envia el numero de la celda en la conexion para establecer el turno con el conectado

                String strnmbr_fila = String.valueOf(getMiSistema().getCeldaSeleccionada().f);
                String strnmbr_columna = String.valueOf(getMiSistema().getCeldaSeleccionada().c);
                String union_fc = strnmbr_fila + strnmbr_columna;
                System.out.println("Este es el numero a enviar:" + union_fc);

                this.getAppServidor().getDatosSalida().writeUTF(union_fc);
                this.getAppServidor().getDatosSalida().flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            getAppServidor().setYourTurn(false);
        }
    }
    
    private void tick() {

        Punto2D cs = getMiSistema().getCeldaSeleccionada();
        List<Integer> posicion = new ArrayList<Integer>();

        if (!getAppServidor().isYourTurn()) {
            try {
                String espacio = getAppServidor().getDatosEntrada().readUTF();

                System.out.println("El espacio es:" + espacio);

                getMiSistema().hacerMovimiento(Integer.parseInt(String.valueOf(espacio.charAt(0))), Integer.parseInt(String.valueOf(espacio.charAt(1))));

                determinarGanador();
                getAppServidor().setYourTurn(true);

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    @Override
    public void run() {
        Canvas lienzo = getVentanaJuego().getLienzo();
        dobleBuffer = new BufferedImage(lienzo.getWidth(), lienzo.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics lapizCanvas = lienzo.getGraphics();
        Graphics lapiz = dobleBuffer.getGraphics(); 
        
        if(!getAppServidor().conectar()) {
            getAppServidor().initializeServer();
        }

        if (!getAppServidor().isCircle() && !getAppServidor().isAceptado()) {
            try {
                getAppServidor().escucharClientes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
         
        while(getMiSistema().isActivo()){
            this.tick();
            
            lapiz.fillRect(40, 310, 200, 50);
            dibujarTablero(lapiz);
            dibujarValoresTablero(lapiz);
            dibujarTurnos(lapiz);
            
            if(getReiniciar() == true){
                reiniciarJuego(lapiz);
                setReiniciar(false);
            }
            
            lapizCanvas.drawImage(dobleBuffer, 0, 0, lienzo);   
        }
        
    }
    
    public void dibujarTablero(Graphics lapiz){
        int pY, pX;
        for (int y = 1; y <= 2 ; y++) {
            pY = config.getInicioTableroY();
            lapiz.drawLine(y*100, 0, y*100, 300);
  
        }
        // lineas verticales
        for (int x = 1; x <= 2 ; x++) {
            pX = config.getInicioTableroX();
            lapiz.drawLine(0 ,x*100 , 300, x*100 );
            
        }    
    }
    
    public void dibujarTurnos(Graphics lapiz){
        lapiz.setFont(config.getFuenteTexto());
        lapiz.drawString("TURNO DEL JUGADOR "+ getMiSistema().getTurno(), 60, 340);
        if(getMiSistema().getTurno() == 'O'){
                    lapiz.setColor(Color.blue);
                }else{
                    lapiz.setColor(Color.red);
           }  
    }
    
    public void reiniciarJuego(Graphics lapiz){
        for (int Fila = 0; Fila < 3; Fila++) {
			for (int Columna = 0; Columna < 3; Columna++) {
                            for (int f = 0; f < 3; f++) {
                                for (int c = 0; c < 3; c++) {
                                    lapiz.setColor(new Color(5,0,87));
                                    int pX =config.getInicioTableroX()+(c*config.getAnchoCelda());
                                    int pY =config.getInicioTableroY() + (f*config.getAltoCelda());
                                    lapiz.fillRect(pX, pY, 100, 100);
                                    lapiz.drawString(" ", pX, pY);
                                    
                                }
                                
                            }
                            getMiSistema().reiniciarTablero();
                            String Turno = "X";
                            getMiSistema().setTurno(Turno.charAt(0));
			}
		}
    }
    
    private void dibujarValoresTablero(Graphics lapiz) {
        lapiz.setFont(config.getFuenteTablero());
        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                char v = getMiSistema().getTriki(f, c).getInfo();
                int pX = 20 + config.getInicioTableroX()+(c*config.getAnchoCelda());
                int pY = 40 + config.getInicioTableroY() + (f*config.getAltoCelda());
                lapiz.setColor(Color.white);
                lapiz.drawString(v == 0 ? "" : "" + v, pX, pY);
            }
        }
    }
    
    
    
    
}

class ConfiguracionDibujo{
    private int anchoCelda;
    private int altoCelda;
    private Font fuenteTablero = new Font("Arial", Font.PLAIN, 36);
    private Font fuenteTexto = new Font("Arial", Font.PLAIN, 14);
    
    private int inicioTableroX, inicioTableroY;
    private Color colorLinea = Color.BLACK;

    public ConfiguracionDibujo() {
        anchoCelda = altoCelda = 100;
        inicioTableroX = 0;
        inicioTableroY = 0;
    }

    public int getAnchoCelda() {
        return anchoCelda;
    }

    public int getAltoCelda() {
        return altoCelda;
    }

    public int getInicioTableroX() {
        return inicioTableroX;
    }

    public int getInicioTableroY() {
        return inicioTableroY;
    }

    public Color getColorLinea() {
        return colorLinea;
    }

    public Font getFuenteTablero() {
        return fuenteTablero;
    }
    
    public Font getFuenteTexto(){
        return fuenteTexto;
    }
        
}

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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Juan
 */
public class Modelo implements Runnable{
    private Triki triki;
    private Vista ventana;
    private ConfiguracionDibujo config;
    private boolean reiniciar;
    
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
            appServidor = new Servidor();
        }
        return appServidor;
    }


    public void iniciarServidor() throws IOException, ClassNotFoundException {

        getAppServidor().initializeServer();
        getAppServidor().activar(true);
        getAppServidor().escucharClientes();
        getAppServidor().activar(false);
    }
    
    public void iniciar(){
        getVentana().setVisible(true);
        hiloDibujo.start();

    }
    
    public Vista getVentana(){
        if(ventana==null){
            ventana = new Vista(this);
            ventana.setTitle("TRIKI");
            ventana.setResizable(false);
        }
        return ventana;
    }

    public Triki getMiSistema() {

        if(triki==null){
            triki = new Triki();
            System.out.println("Este es la primera instancia: " + triki);
        }
        if (getAppServidor().estadoTriki != null){
            triki = getAppServidor().estadoTriki;
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
		getVentana().mostrarMensaje("Jugador " + Ganador + " Ganó!");
	}

	if (getMiSistema().esEmpate()) {
		getVentana().mostrarMensaje("Empate!");
	}
    }
    public void mostrarCeldaSeleccionada(int mx, int my){
        boolean dentro = false;
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
                getVentana().mostrarMensaje("Espacio ya tomado!");
            }
            determinarGanador();
        }
    }

    @Override
    public void run() {
        Canvas lienzo = getVentana().getLienzo();
        dobleBuffer = new BufferedImage(lienzo.getWidth(), lienzo.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics lapizCanvas = lienzo.getGraphics();
        Graphics lapiz = dobleBuffer.getGraphics();
        while(getMiSistema().isActivo()){
            lapiz.fillRect(40, 310, 200, 50);
            lapiz.setColor(Color.black);
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
                                    lapiz.setColor(new Color(238,238,238));
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

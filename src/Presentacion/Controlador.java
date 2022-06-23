/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;


import Logica.Celda;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

/**
 *
 * @author Juan
 */
public class Controlador implements MouseMotionListener, MouseListener{
    
    private final VistaJuego ventanaJuego;
    private final VistaMenu ventanaMenu;

    private final Modelo modelo;
    
    
    public Controlador(VistaJuego Juego, VistaMenu Menu){
        ventanaJuego = Juego;
        ventanaMenu = Menu;
        modelo= Juego.getMiModelo();
    }


    

    public void actionPerformed(ActionEvent e) {
        
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() instanceof Canvas){
            modelo.mostrarCeldaSeleccionada(e.getX(), e.getY());
            modelo.establecerTurnoJugado();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}

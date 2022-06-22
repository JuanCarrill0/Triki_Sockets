/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Launcher;

import Presentacion.Modelo;

import java.io.IOException;

/**
 *
 * @author Juan
 */
public class Launcher {
    
    private final Modelo miTriki;
    
     public Launcher() throws IOException {
        miTriki = new Modelo();
        miTriki.iniciar();
    }
    
    public static void main(String args[]) throws IOException {
        new Launcher();
    }
    
}

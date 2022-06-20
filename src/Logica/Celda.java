/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.Serializable;

/**
 *
 * @author Juan
 */
public class Celda implements Serializable {
    private char info ;
    private boolean fijo;
    
    public static final char CELDA_VACIA = ' ';

    public Celda() {
        info = ' '; // celda vacia
    }

    
    public char getInfo() {
        return info;
    }

    public void setInfo(char info) {
        this.info = info;
    }

    public boolean isFijo() {
        return fijo;
    }

    public void setFijo(boolean fijo) {
        this.fijo = fijo;
    }
    
    
}

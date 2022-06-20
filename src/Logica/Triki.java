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
public class Triki implements Serializable {
        private Celda tablero[][];
	private int tamaño = 3;
	private char turno;
        private boolean activo;
        
        private Punto2D celdaSeleccionada;
       

	/** Inicializa el triki 3x3*/
        
	public Triki() {
		this.tamaño = tamaño;
                tablero = new Celda[tamaño][tamaño];
                for (int f = 0; f < tamaño; f++) {
                    for (int c = 0; c < tamaño; c++) {
                        tablero[f][c] = new Celda();
                    }
                }
		turno = 'X';
                celdaSeleccionada = new Punto2D();
                
                celdaSeleccionada.f = -1;
                celdaSeleccionada.c =-1;
	}
        
        public Celda getTriki(int f, int c){
            return tablero[f][c];
        }
	public Celda[][] getTablero(){
		return tablero;
	}


	/** Dada una fila y columna seleccionada en la matriz
         *  Rellena el botón con el turno que toca */
        
	public boolean hacerMovimiento(int Fila, int Columna) {               
		if (tablero[Fila][Columna].getInfo() == ' ') {
			tablero[Fila][Columna].setInfo(turno);
			turno = (turno == 'X' ? 'O' : 'X');
			return true;
		}
		return false;
	}

	/** Encuentra un posible ganador verificando
         *  Filas, Columnas y Diagonales en la matriz del tablero
         * @return 
         */
        
        public Punto2D getCeldaSeleccionada() {
            return celdaSeleccionada;
        }   
        
	public char encontrarGanador() {

		// Verifica Horizontales
		char Ganador;
		for (int Fila = 0; Fila < tamaño; Fila++) {
			Ganador = tablero[Fila][0].getInfo();
			for (int Columna = 0; Columna < tamaño; Columna++) {
				if (tablero[Fila][Columna].getInfo() != Ganador) {
					Ganador = ' ';
					break;
				}
			}
			if (Ganador != ' ')
				return Ganador;
		}

		// Verifica Verticales
		for (int Columna = 0; Columna < tamaño; Columna++) {
			Ganador = tablero[0][Columna].getInfo();
			for (int Fila = 0; Fila < tamaño; Fila++) {
				if (tablero[Fila][Columna].getInfo() != Ganador) {
					Ganador = ' ';
					break;
				}
			}
			if (Ganador != ' ')
				return Ganador;
		}

		// Verifica de izquierda a derecha diagonales
		Ganador = tablero[0][0].getInfo();
		for (int Columna = 0; Columna < tamaño; Columna++) {
			if (tablero[Columna][Columna].getInfo() != Ganador) {
				Ganador = ' ';
				break;
			}
		}
		if (Ganador != ' ')
			return Ganador;

		// Verifica de derecha a izquierda diagonales
                
		Ganador = tablero[0][tamaño - 1].getInfo();
		for (int Fila = 0; Fila < tamaño; Fila++) {
			if (tablero[Fila][(tamaño - 1) - Fila].getInfo() != Ganador) {
				Ganador = ' ';
				break;
			}
		}
		if (Ganador != ' ')
			return Ganador;

		return ' ';
	}

	/** Determina si el tablero se encuentra completamente lleno */
        
	public boolean esEmpate() {
		for (int Fila = 0; Fila < tamaño; Fila++) {
			for (int Columna = 0; Columna < tamaño; Columna++) {
				if (tablero[Fila][Columna].getInfo() == ' ')
					return false;
			}
		}

		return true;
	}
        
        /**Reinicia el tablero en la logica*/
        
        public void reiniciarTablero(){
            for (int Fila = 0; Fila < tamaño; Fila++) {
			for (int Columna = 0; Columna < tamaño; Columna++) {
                               tablero[Fila][Columna].setInfo(' '); 
			}
		}   
        }
        
        /** Devuelve el turno */
        
	public char getTurno() {
		return this.turno;
	}
        
        public void setTurno(char Turno){
            this.turno= Turno;
            
        }
        
        
        public boolean isActivo(){
            return activo;
        }
        
        public void setActivo(boolean activo){
            this.activo = activo;
        }
    
}

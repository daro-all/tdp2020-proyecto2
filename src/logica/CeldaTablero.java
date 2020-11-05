package logica;

import java.awt.Component;

/*
 * Una celda se crea una �nica vez, al inicio del juego. Luego se trabaja modificando su estado interno.
 */

/**
 * Almacena todo el contenido relacionado a una celda del tablero de juego:
 * - un n�mero de un d�gito y su imagen (entidad gr�fica) asociada.
 * - sus coordenadas (fila y columna).
 * - si se trata de una celda que contiene una pista.
 * - si se trata de una celda que est� activa (es decir, mostrando el valor almacenado).
 * - el componente que la representa en la GUI.
 * @author Dario A. Leal
 */
public class CeldaTablero {
	
	private int valor;
	private EntidadGraficaCeldaTablero entidad_grafica_celda;
	private int fila, columna;
	private boolean es_una_pista;
	private boolean activa; //false si valor == 0 (celda inactiva), true si valor != 0 (celda activa). 
	private Component componente;
	
	/**
	 * Constructor. Crea la estructura para almacenar toda la informaci�n asociada a una celda del tablero de juego.
	 * @param valor Un d�gito de 0 a 9, donde 0 indica que la celda no mostrar� imagen en el tablero, caso contrario se muestra la imagen correspondiente al par�metro valor recibido.
	 * @param fila Coordenada correspondiente a la fila en la que se encuentra la celda.
	 * @param columna Coordenada correspondiente a la columna en la que se encuentra la celda.
	 */
	public CeldaTablero(int valor, int fila, int columna) {
		this.valor = valor;
		entidad_grafica_celda = new EntidadGraficaCeldaTablero();
		entidad_grafica_celda.setImagen(this.valor);
		this.fila = fila;
		this.columna = columna;
		
		//Si al crear una celda el valor es != 0, la celda almacena una pista, lo que significa que el contenido de la celda
		//no puede ser alterado.
		es_una_pista = this.valor != 0;
		
		//Al momento de la creaci�n, estos estados son equivalentes. Diferencia:
		//Celda que es pista --> est� activa y no puede ser modificada.
		//Celda que no es pista --> su status de activa puede variar, al igual que el contenido.
		activa = es_una_pista;
		
		componente = null;
	}
	
	//Solo se pueden actualizar celdas que no son pista (quien usa este servicio debe controlar eso).
	/**
	 * Actualiza valor, imagen y status de activa para la celda.
	 * La actualizaci�n se da avanzando de forma autom�tica a partir del valor previo almacenado. 
	 */
	public void actualizar() {
		if (valor == 9) {
			valor = 0;
			activa = false;
		}
		else {
			valor++;
			activa = true;
		}
		entidad_grafica_celda.setImagen(valor);
	}
	
	/**
	 * Devuelve el valor almacenado en la celda.
	 * @return El valor almacenado en la celda.
	 */
	public int getValor() {
		return valor;
	}
	
	/**
	 * Devuelve la entidad gr�fica almacenada en la celda.
	 * @return La entidad gr�fica almacenada en la celda.
	 */
	public EntidadGraficaCeldaTablero getEntidadGraficaCelda() {
		return entidad_grafica_celda;
	}

	/**
	 * Devuelve la coordenada correspondiente a la fila en la que se encuentra la celda.
	 * @return La coordenada correspondiente a la fila en la que se encuentra la celda.
	 */
	public int getFila() {
		return fila;
	}
	
	/**
	 * Devuelve la coordenada correspondiente a la columna en la que se encuentra la celda.
	 * @return La coordenada correspondiente a la columna en la que se encuentra la celda.
	 */
	public int getColumna() {
		return columna;
	}
	
	/**
	 * Indica si la celda corresponde a una pista (celda que no puede ser modificada) del tablero.
	 * @return True si la celda corresponde a una pista, false en caso contrario.
	 */
	public boolean es_una_pista() {
		return es_una_pista;
	}

	/**
	 * Indica si la celda est� activa (visible en el tablero de juego).
	 * @return True si la celda est� activa, false en caso contrario.
	 */
	public boolean esta_activa() {
		return activa;
	}
	
	/**
	 * Le indica a la celda el componente que la representa en el tablero de juego.
	 * @param componente Componente gr�fico que representa a la celda en el tablero de juego.
	 */
	public void setComponente(Component componente) {
		this.componente = componente;
	}
	
	/**
	 * Devuelve el componente que representa a la celda en el tablero de juego.
	 * @return El componente gr�fico que representa a la celda en el tablero de juego.
	 */
	public Component getComponente() {
		return componente;
	}
	
}
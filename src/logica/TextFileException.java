package logica;

/**
 * Modela la excepci�n ante un error en la carga del archivo de texto que contiene un juego.
 * @author Dario A. Leal
 */
@SuppressWarnings("serial")
public class TextFileException extends Exception {

	/**
	 * Inicializa la excepci�n indicando el origen del error.
	 * @param msg Especifica informaci�n adicional acerca de la excepci�n.
	 */
	public TextFileException(String msg){
		super(msg);
	}

}
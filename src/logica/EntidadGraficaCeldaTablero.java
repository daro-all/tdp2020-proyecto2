package logica;

import javax.swing.ImageIcon;

/**
 * Estructura que almacena una imagen para una celda del tablero de juego.
 * @author Dario A. Leal
  */
public class EntidadGraficaCeldaTablero {
	
	private ImageIcon imagen;
	private String[] imagenes;
	
	/**
	 * Constructor. Crea la entidad en la que se almacenará una imagen para una celda del tablero.
	 */
	public EntidadGraficaCeldaTablero() {
		imagen = new ImageIcon();
		imagenes = setArregloDeImagenes();
	}
	
	//Crea un arreglo donde empareja cada dígito de 0 a 9 con una imagen que lo representa.
	//Para el caso del 0, se trata de una imagen vacía.
	private String[] setArregloDeImagenes() {
		String[] toReturn = new String[10];
		for (int i = 0; i <= 9; i++)
			toReturn[i] = "/img/game/" + i + ".png";
		return toReturn;
	}

	/**
	 * Se configura la entidad según el parámetro recibido.
	 * Es decir, a la entidad gráfica se le asocia el número en formato imagen correspondiente al valor recibido.
	 * Ej.: Si se recibe valor = 8, se setea esta entidad con la imagen de un número 8.
	 * Si se recibe valor = 0, se setea la imagen con una imagen vacía.
	 * @param valor Es un dígito entre 0 y 9.
	 */
	public void setImagen(int valor) {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagenes[valor]));
		imagen.setImage(imageIcon.getImage());
	}
	
	/**
	 * Devuelve la imagen que almacena esta entidad.
	 * @return la imagen almacenada en esta entidad gráfica.
	 */
	public ImageIcon getImagen() {
		return imagen;
	}
	
}
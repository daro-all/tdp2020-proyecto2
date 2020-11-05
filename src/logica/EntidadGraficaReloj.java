package logica;

import javax.swing.ImageIcon;

/**
 * Estructura que almacena una imagen para un elemento (d�gito o separador) del reloj del juego.
 * @author Dario A. Leal
  */
public class EntidadGraficaReloj {

	private ImageIcon imagen;
	private ImageIcon[] imagenes;
	
	/**
	 * Constructor. Crea la entidad en la que se almacenar� una imagen para un elemento del reloj.
	 */
	public EntidadGraficaReloj() {
		imagen = new ImageIcon();
		imagenes = setImagenesReloj();
	}
	
	//Crea un arreglo donde empareja cada d�gito de 0 a 9 con una imagen que lo representa.
	//Tambi�n hay una imagen para el s�mbolo ":" (el �ltimo componente del arreglo).
	private ImageIcon[] setImagenesReloj() {		
		ImageIcon[] img_array = new ImageIcon[11];
		for (int i = 0; i < 10; i++) {
			img_array[i] = new ImageIcon(getClass().getResource("/img/reloj/r" + i + ".png"));
		}
		img_array[10] = new ImageIcon(getClass().getResource("/img/reloj/rdp.png"));
		return img_array;
	}

	/**
	 * Se configura la entidad seg�n el par�metro recibido.
	 * Es decir, a la entidad gr�fica se le asocia el n�mero en formato imagen correspondiente al valor recibido.
	 * Ej.: Si se recibe valor = 8, se setea esta entidad con la imagen de un n�mero 8.
	 * Si se recibe valor = 10, se setea la imagen con un s�mbolo separador (":" dos puntos).
	 * @param valor Es un n�mero entre 0 y 10.
	 */
	public void setImagen(int valor) {
		imagen.setImage(imagenes[valor].getImage());
	}
	
	/**
	 * Devuelve la imagen que almacena esta entidad.
	 * @return la imagen almacenada en esta entidad gr�fica.
	 */
	public ImageIcon getImagen() {
		return imagen;
	}

}
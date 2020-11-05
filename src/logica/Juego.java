package logica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

/**
 * Modela la lógica interna del juego.
 * @author Dario A. Leal
 */
public class Juego {
	
	private String path;
	private int dimension = 9; //TABLERO CLASICO POR DEFECTO.
	private int cant_minima_pistas = 20; //Debe ser mayor o igual a 17 para garantizar que existe una única solución para el juego.
	private int cant_maxima_pistas = 35;
	private int[][] tablero; //Matriz en la que se vuelca el contenido del archivo de texto que contiene un juego. Servirá para posterior control del resultado.
	private CeldaTablero[][] tablero_para_GUI; //Matriz que contiene un juego válido, apta para interactuar con la GUI. El jugador modifica las celdas durante el juego.

	/**
	 * Genera un juego a partir de un archivo de texto.
	 * @param tablero_numero Número de juego elegido por el usuario.
	 * @throws TextFileException Si hay algún tipo de problema con el archivo de texto desde el que se carga el juego.
	 */
	public Juego(int tablero_numero) throws TextFileException {
		path = "/txt/sudoku" + tablero_numero + ".txt";
		tablero = generarMatriz();
		if (!esTableroValido()) {
			throw new TextFileException("error : text file : \"El archivo no contiene un juego válido.\"");
		} else {
			tablero_para_GUI = generar_tablero_apto_para_GUI();
		}
	}
	
	/* 
	 * Si el archivo tiene más de DIMENSION filas o columnas no importa (ignora el contenido extra).
	 * Lo que importa es que el archivo tenga al menos DIMENSION filas y DIMENSION columnas correctas.
	 * Se retorna una matriz de DIMENSION x DIMENSION dígitos entre 1 y 9.
	 */
	private int[][] generarMatriz() throws TextFileException{
		InputStream ins = Juego.class.getResourceAsStream(path);
		if (ins == null) {
			throw new TextFileException("error : text file : \"No se puede abrir el archivo - Archivo inexistente?\"");
		}
		InputStreamReader insr = new InputStreamReader(ins);
		BufferedReader bfr = new BufferedReader(insr);
		String linea; //Almacena temporalmente cada línea obtenida del archivo.
		Scanner sLine;
		String delimitador = " "; //El delimitador para recuperar números será un espacio simple.
		int numero;
		int[][] matriz = new int[dimension][dimension];
		int fila = 0, col;
		
		try {
			while ( (linea = bfr.readLine()) != null && fila < dimension) {
				sLine = new Scanner(linea);
				sLine.useDelimiter(delimitador);
				col = 0;
				while (sLine.hasNext() && col < dimension) {
					numero = Integer.parseInt( sLine.next() ); //Recupera un número de la línea de texto, lo convierte a int.
					if (numero < 1 || numero > 9) {
						sLine.close();
						bfr.close();
						throw new TextFileException("error : text file : \"Juego con valores fuera de rango.\"");
					}
					else {
						matriz[fila][col] = numero;
						col++;
					}
				}
				sLine.close();
				
				//Al terminar de recorrer una línea controlo haber obtenido la cantidad de números (columnas) correspondientes.
				if (col < dimension) {
					bfr.close();
					throw new TextFileException("error : text file : \"Faltan números para el tablero de juego.\"");
				}
				
				fila++;
			}
			bfr.close();
			
			//Al terminar de recorrer el archivo controlo haber obtenido la cantidad de filas correspondientes.
			if (fila < dimension) {
				throw new TextFileException("error : text file : \"Faltan números para el tablero de juego.\"");
			}
		} catch (IOException ex) {
			throw new TextFileException("error : text file : \"Error al recuperar el tablero de juego.\"");
		}

		return matriz;
	} //generarMatriz
	
	//Verifica que la matriz con el juego obtenido de un archivo de texto contenga un juego válido.
	//La matriz ya tiene DIMENSION x DIMENSION valores, todos en el rango 1..9.
	//Método: se chequea que cada fila tenga todos valores diferentes entre sí. Lo mismo para las columnas.
	//Controla simultáneamente una fila y una columna.
	private boolean esTableroValido() {
		ArrayList<Integer> valores_de_fila = new ArrayList<Integer>(dimension);
		ArrayList<Integer> valores_de_columna = new ArrayList<Integer>(dimension);
		boolean esValido = true;
		int nFil, nCol;
		int i = 0, j;
		while (i < dimension && esValido) {
			j = 0;
			while (j < dimension && esValido) {
				nFil = tablero[i][j]; //Avanza a través de una fila.
				nCol = tablero[j][i]; //Avanza a través de una columna.
				if (valores_de_fila.contains(nFil) || valores_de_columna.contains(nCol)){
					esValido = false;
				}
				else {
					valores_de_fila.add(nFil);
					valores_de_columna.add(nCol);
					j++;
				}
			}
			valores_de_fila.clear();
			valores_de_columna.clear();
			i++;
		}
		return esValido;
	} //esTableroValido
	
	//Genera un tablero con el juego, pero apto para trabajar con él desde la GUI.
	private CeldaTablero[][] generar_tablero_apto_para_GUI(){
		int cant_pistas;
		Random rand = new Random();
		int numero_random;
		boolean mostrar;
		int valor;
		CeldaTablero[][] tablero_para_GUI = new CeldaTablero[dimension][dimension];
		
		do {
			cant_pistas = 0;
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					//Con estas 2 líneas se varía la cantidad de pistas mostradas.
					//Así como está, hay un 40% de chance de que una celda sea pista. El tablero suele mostrar unas ~30.
					numero_random = rand.nextInt(5);
					mostrar = (numero_random == 0 || numero_random == 1) ? true : false;
					
					if (mostrar) {
						valor = tablero[i][j];
						cant_pistas++;
					}
					else {
						valor = 0;
					}
					tablero_para_GUI[i][j] = new CeldaTablero(valor, i, j);
				}
			}
		} while (cant_pistas < cant_minima_pistas || cant_pistas > cant_maxima_pistas);

		return tablero_para_GUI;
	} //generar_tablero_apto_para_GUI
	
	/**
	 * Devuelve la dimensión del tablero de juego.
	 * @return Dimensión del tablero de juego.
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Devuelve la celda del tablero ubicada en las coordenadas recibidas por parámetro.
	 * @param fila Coordenada de la fila de la celda solicitada.
	 * @param columna Coordenada de la columna de la celda solicitada.
	 * @return La celda del tablero ubicada en las coordenadas recibidas por parámetro.
	 */
	public CeldaTablero getCelda(int fila, int columna) {
		return tablero_para_GUI[fila][columna];
	}

	/**
	 * Controla que no hayan valores repetidos con el de la celda recibida por parámetro.
	 * Ese control se hace sobre los elementos de la misma fila, columna y panel del parámetro.
	 * Este control equivale a chequear que no se infrinja ninguna regla del juego.
	 * @param c Celda respecto a la cual se buscan celdas repetidas.
	 * @return Una estructura con celdas de la misma fila, con las de la misma columna, y con las del mismo panel, que tienen el mismo valor que el parámetro.
	 */
	public ArrayList<CeldaTablero> chequearRepetidos(CeldaTablero c) {
		ArrayList<CeldaTablero> repetidos = new ArrayList<CeldaTablero>();
		chequearRepetidosEnFila(c, repetidos);
		chequearRepetidosEnColumna(c, repetidos);
		chequearRepetidosEnPanel(c, repetidos);
		return repetidos;
	}
	
	//Chequeo la fila en la que se encuentra c.
	private void chequearRepetidosEnFila(CeldaTablero c, ArrayList<CeldaTablero> repetidos) {
		CeldaTablero celda;
		int fila_de_c = c.getFila();
		int columna_de_c = c.getColumna();
		
		for (int j = 0; j < dimension; j++) {
			if (j != columna_de_c) {
				celda = tablero_para_GUI[fila_de_c][j];
				if (celda.esta_activa() && celda.getValor() == c.getValor()) { //Sea una pista o no, lo que importa es que la celda esté activa (visible).
					repetidos.add(celda);
				}
			}
		}
	}
	
	//Chequeo la columna en la que se encuentra c.
	private void chequearRepetidosEnColumna(CeldaTablero c, ArrayList<CeldaTablero> repetidos) {
		CeldaTablero celda;
		int fila_de_c = c.getFila();
		int columna_de_c = c.getColumna();
		
		for (int i = 0; i < dimension; i++) {
			if (i != fila_de_c) {
				celda = tablero_para_GUI[i][columna_de_c];
				if (celda.esta_activa() && celda.getValor() == c.getValor()) {
					repetidos.add(celda);
				}
			}
		}
	}
	
	//Chequeo PANEL en que se encuentra c.
	private void chequearRepetidosEnPanel(CeldaTablero c, ArrayList<CeldaTablero> repetidos) {
		CeldaTablero celda;
		//Fila y columna inicial son las coordenadas de la celda ubicada en la esquina superior izquierda del panel en el que se encuentra c.
		int fila_inicial = c.getFila() - c.getFila() % 3;
		int col_inicial = c.getColumna() - c.getColumna() % 3;

		for (int i = fila_inicial; i < fila_inicial + 3; i++) {
			for (int j = col_inicial; j < col_inicial + 3; j++) {
				celda = tablero_para_GUI[i][j];
				if (celda != c) {
					if (celda.esta_activa() && celda.getValor() == c.getValor()) {
						//Controlo que no esté en los repetidos ya.
						//Esto puede darse cuando hay repetición con una celda de la misma fila/columna y que además está en el mismo panel.
						if (!repetidos.contains(celda)) {
							repetidos.add(celda);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Compara el tablero con el que viene jugando el usuario contra el tablero que contiene el juego original.
	 * Hay tres resultados posibles:
	 * - INCOMPLETO: si hay al menos una celda que el usuario dejó sin elegir número.
	 * - DISTINTOS: si el usuario completó el tablero pero no con la solución correcta.
	 * - IGUALES: el usuario resolvió correctamente el juego.
	 * @return Una cadena de texto que contiene el resultado de la comprobación del juego. 
	 */
	public String comprobar_resolucion() {
		boolean iguales = true;
		String resultado = "";
		int i = 0, j;
		
		while (i < dimension && iguales) {
			j = 0;
			while (j < dimension && iguales) {
				if (tablero_para_GUI[i][j].getValor() == 0) {
					iguales = false;
					resultado = "INCOMPLETO";
				}
				else {
					if (tablero_para_GUI[i][j].getValor() != tablero[i][j]) {
						iguales = false;
						resultado = "DISTINTOS";
					}
				}
				j++;
			}
			i++;
		}
		if (iguales) {
			resultado = "IGUALES";
		}
		return resultado;
	} //comprobar_resolucion
	
}
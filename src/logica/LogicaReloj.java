package logica;

/*
 * Almacena los dígitos que componen una hora en un arreglo de 6 elementos. No se incluye el separador ":" en ese arreglo.
 */

/**
 * Modela la lógica interna del reloj del juego.
 * @author Dario A. Leal
 */
public class LogicaReloj {

	/**
	 * Modela un dígito correspondiente al reloj, almacenando su valor y su imagen (entidad gráfica) asociada.
	 * Si el valor almacenado es un número entre 0 y 9, la imagen asociada es una imagen de ese número.
	 * Si valor = 10, la imagen asociada es la del símbolo separador ":"
	 * @author Dario A. Leal
	 */
	public class DigitoReloj {
		private int valor;
		private EntidadGraficaReloj entidad_grafica_digito_reloj;
		
		/**
		 * Constructor. Crea la estructura para almacenar un dígito del reloj y su entidad gráfica asociada.
		 * @param valor Valor del dígito a almacenar.
		 */
		public DigitoReloj(int valor) {
			this.valor = valor;
			entidad_grafica_digito_reloj = new EntidadGraficaReloj();
			entidad_grafica_digito_reloj.setImagen(this.valor);
		}
		
		/**
		 * Actualiza el valor del dígito de reloj según el parámetro recibido.
		 * @param valor Nuevo valor para ser almacenado por el dígito.
		 */
		public void actualizar_digito(int valor) {
			this.valor = valor;
			entidad_grafica_digito_reloj.setImagen(valor);
			/*
			Si dejamos la actualización automática como en CeldaTablero pasaríamos siempre de 9 a 0
			y hay casos en los que eso es un error (las decenas de minutos y segundos), donde hay que
			pasar de 5 a 0, por eso directamente el método recibe el valor hacia el que hay que actualizar.
			*/
		}
		
		/**
		 * Devuelve el valor almacenado por el dígito.
		 * @return El valor almacenado por el dígito.
		 */
		public int getValor() {
			return valor;
		}
		
		/**
		 * Devuelve la entidad gráfica almacenada por el dígito.
		 * @return La entidad gráfica almacenada por el dígito.
		 */
		public EntidadGraficaReloj getEntidadGraficaDigitoReloj() {
			return entidad_grafica_digito_reloj;
		}
	} //clase anidada DigitoReloj
	
	private int hs, min, seg;
	private DigitoReloj[] estado_interno_reloj;
	private DigitoReloj simbolo_dos_puntos;
	
	/**
	 * Constructor. Crea la estructura interna para almacenar la información relacionada al reloj del juego.
	 */
	public LogicaReloj() {
		hs = min = seg = 0;
		estado_interno_reloj = new DigitoReloj[6];
		for (int i = 0; i < 6; i++) {
			estado_interno_reloj[i] = new DigitoReloj(0);
		}
		simbolo_dos_puntos = new DigitoReloj(10);
	}
	
	/**
	 * Actualiza el estado interno del reloj (sus valores y respectivas entidades gráficas asociadas). 
	 */
	public void actualizar_reloj() {
		if (seg < 59) {
			seg++;
		}	
		else {
			if (min < 59) {
				min++;
				seg = 0;
			}
			else {
				hs++;
				min = 0;
				seg = 0;
			}
		}
		
		estado_interno_reloj[0].actualizar_digito(hs / 10);
		estado_interno_reloj[1].actualizar_digito(hs % 10);
		estado_interno_reloj[2].actualizar_digito(min / 10);
		estado_interno_reloj[3].actualizar_digito(min % 10);
		estado_interno_reloj[4].actualizar_digito(seg / 10);
		estado_interno_reloj[5].actualizar_digito(seg % 10);
	}
	
	/**
	 * Devuelve el dígito correspondiente a la decena de las horas.
	 * @return El dígito correspondiente a la decena de las horas.
	 */
	public DigitoReloj getHoraDecena() {
		return estado_interno_reloj[0];
	}
	
	/**
	 * Devuelve el dígito correspondiente a la unidad de las horas.
	 * @return El dígito correspondiente a la unidad de las horas.
	 */
	public DigitoReloj getHoraUnidad() {
		return estado_interno_reloj[1];
	}
	
	/**
	 * Devuelve el dígito correspondiente a la decena de los minutos.
	 * @return El dígito correspondiente a la decena de los minutos.
	 */
	public DigitoReloj getMinutosDecena() {
		return estado_interno_reloj[2];
	}
	
	/**
	 * Devuelve el dígito correspondiente a la unidad de los minutos.
	 * @return El dígito correspondiente a la unidad de los minutos.
	 */
	public DigitoReloj getMinutosUnidad() {
		return estado_interno_reloj[3];
	}
	
	/**
	 * Devuelve el dígito correspondiente a la decena de los segundos.
	 * @return El dígito correspondiente a la decena de los segundos.
	 */
	public DigitoReloj getSegundosDecena() {
		return estado_interno_reloj[4];
	}
	
	/**
	 * Devuelve el dígito correspondiente a la unidad de los segundos.
	 * @return El dígito correspondiente a la unidad de los segundos.
	 */
	public DigitoReloj getSegundosUnidad() {
		return estado_interno_reloj[5];
	}
	
	/**
	 * Devuelve el dígito correspondiente al símbolo separador dos puntos (":").
	 * @return El dígito correspondiente al símbolo separador dos puntos (":").
	 */
	public DigitoReloj getSimboloDosPuntos() {
		return simbolo_dos_puntos;
	}

	/**
	 * Devuelve el valor del atributo que representa las horas del reloj.
	 * @return El valor del atributo que representa las horas del reloj.
	 */
	public int getHoras() {
		return hs;
	}
	
	/**
	 * Devuelve el valor del atributo que representa los minutos del reloj.
	 * @return El valor del atributo que representa los minutos del reloj.
	 */
	public int getMinutos() {
		return min;
	}
	
	/**
	 * Devuelve el valor del atributo que representa los segundos del reloj.
	 * @return El valor del atributo que representa los segundos del reloj.
	 */
	public int getSegundos() {
		return seg;
	}
	
}
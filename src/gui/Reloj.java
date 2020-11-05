package gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import logica.LogicaReloj;

/**
 * Modela la representación gráfica del reloj en la interface del juego.
 * @author Dario A. Leal
 */
@SuppressWarnings("serial")
public class Reloj extends JPanel {
	
	private LogicaReloj logica_reloj;
	private Timer timer;
	//Un label para cada caracter símbolo-numérico que compone la representación gráfica del reloj:
	private JLabel hs_decena, hs_unidad, dp1, min_decena, min_unidad, dp2, seg_decena, seg_unidad;
	//Una imagen para asociar a cada caracter símbolo-numérico de la representación gráfica del reloj:
	private ImageIcon img_hs_decena, img_hs_unidad, img_min_decena, img_min_unidad, img_seg_decena, img_seg_unidad, img_dos_puntos;
	
	/**
	 * Constructor. Crea la lógica interna del reloj, el panel en el que se lo representará gráficamente, y el timer que lo controla. 
	 */
	public Reloj() {
		logica_reloj = new LogicaReloj();

		generar_panel_reloj();
		
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logica_reloj.actualizar_reloj();
				
				img_hs_decena = logica_reloj.getHoraDecena().getEntidadGraficaDigitoReloj().getImagen();
				redimensionar(hs_decena, img_hs_decena);
				hs_decena.setIcon(img_hs_decena);
				
				img_hs_unidad = logica_reloj.getHoraUnidad().getEntidadGraficaDigitoReloj().getImagen();
				redimensionar(hs_unidad, img_hs_unidad);
				hs_unidad.setIcon(img_hs_unidad);
								
				img_min_decena = logica_reloj.getMinutosDecena().getEntidadGraficaDigitoReloj().getImagen();
				redimensionar(min_decena, img_min_decena);
				min_decena.setIcon(img_min_decena);
				
				img_min_unidad = logica_reloj.getMinutosUnidad().getEntidadGraficaDigitoReloj().getImagen();
				redimensionar(min_unidad, img_min_unidad);
				min_unidad.setIcon(img_min_unidad);
				
				img_seg_decena = logica_reloj.getSegundosDecena().getEntidadGraficaDigitoReloj().getImagen();
				redimensionar(seg_decena, img_seg_decena);
				seg_decena.setIcon(img_seg_decena);
				
				img_seg_unidad = logica_reloj.getSegundosUnidad().getEntidadGraficaDigitoReloj().getImagen();
				redimensionar(seg_unidad, img_seg_unidad);
				seg_unidad.setIcon(img_seg_unidad);
			}
		});
	} //Reloj
	
	/*
	 * El panel son 2 columnas, una para un texto que acompaña al reloj, y la otra para otro panel en el que se lo
	 * representa gráficamente.
	 */
	private void generar_panel_reloj() {
		this.setLayout(new GridLayout(0, 2, 0, 0));
		this.setBorder(new EmptyBorder(3, 0, 8, 0)); //Lo uso para establecer márgenes de separación.
		
		JLabel texto_reloj = new JLabel("Tiempo de juego:");
		texto_reloj.setHorizontalAlignment(SwingConstants.RIGHT);
		texto_reloj.setFont(new Font("SansSerif", Font.BOLD, 14));
		texto_reloj.setBorder(new EmptyBorder(0, 0, 0, 15)); //Para que el texto no quede pegado al reloj.
		this.add(texto_reloj);
		
		//Este es el panel que realmente contiene la representación gráfica de cada dígito y separadores del reloj. 
		JPanel panel_numerico = new JPanel();
		panel_numerico.setLayout(new GridLayout(0, 8, 0, 0));
		panel_numerico.setBorder(new EmptyBorder(0, 0, 0, 25)); //Para acercarlo al texto de la leyenda.

		//Preparo la imagen que asignaré como separador de los dígitos:
		img_dos_puntos = logica_reloj.getSimboloDosPuntos().getEntidadGraficaDigitoReloj().getImagen();
		
		//Creo todos los labels y los agrego al panel.
		hs_decena = new JLabel();
		panel_numerico.add(hs_decena);
		
		hs_unidad = new JLabel();
		panel_numerico.add(hs_unidad);

		dp1 = new JLabel();
		redimensionar(dp1, img_dos_puntos);
		dp1.setIcon(img_dos_puntos);
		panel_numerico.add(dp1);
		
		min_decena = new JLabel();
		panel_numerico.add(min_decena);
		
		min_unidad = new JLabel();
		panel_numerico.add(min_unidad);
		
		dp2 = new JLabel();
		dp2.setIcon(img_dos_puntos);
		panel_numerico.add(dp2);
		
		seg_decena = new JLabel();
		panel_numerico.add(seg_decena);
		
		seg_unidad = new JLabel();
		panel_numerico.add(seg_unidad);
		
		for (Component comp : panel_numerico.getComponents()) {
			if (comp instanceof JLabel) {
				((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
			}
		}

		this.add(panel_numerico);
	} //generar_panel_reloj
	
	//Redimensiona (escala) la imagen usando el tamaño del label como referencia.
	//Versión más simplificada que la de GUI_juego.
	private void redimensionar(JLabel label, ImageIcon img) {
		img.setImage( img.getImage().getScaledInstance(label.getWidth()-7, label.getHeight()-7, java.awt.Image.SCALE_SMOOTH) );
		label.repaint();
	}
	
	/**
	 * Inicia el timer que controla el reloj.
	 */
	public void start() {
		timer.start();
	}
	
	/**
	 * Detiene el timer que controla el reloj.
	 */
	public void stop() {
		timer.stop();
	}
	
	/**
	 * Reinicia el timer que controla el reloj.
	 */
	public void restart() {
		timer.restart();
		logica_reloj = new LogicaReloj();
	}

	/**
	 * Devuelve los valores (horas, minutos y segundos) que representan el estado interno del reloj.
	 * @return Un arreglo con los valores (horas, minutos y segundos) del estado interno del reloj.
	 */
	public int[] getValoresReloj() {
		int[] valores = {logica_reloj.getHoras(), logica_reloj.getMinutos(), logica_reloj.getSegundos()};
		return valores;
	}
	
}
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import java.util.ArrayList;
import java.util.Enumeration;

import logica.*;

/*
 * Juegos que van a dar error (no va a generarse el tablero) y la razón:
 * 3: le falta un valor.
 * 4: le falta una fila.
 * 5: tiene un valor fuera de rango.
 * 6: el juego no tiene un formato válido (viola las reglas).
 * 7: el archivo no existe.
 * 
 * ---
 * 
 * El juego inicia mostrando un menú que permite elegir un juego (distribución del tablero). Luego aparece la ventana principal.
 * La interface gráfica consiste en un frame (GUI_juego) que contiene un contenedor principal (JPanel contentPane) dividido
 * en 3 secciones: superior, medio e inferior.
 */

/**
 * Gestiona la creación y administración tanto de la parte gráfica como de la lógica interna del juego
 * @author Dario A. Leal
 */
@SuppressWarnings("serial")
public class GUI_juego extends JFrame {

	private Color color_celda_pista = new Color(125, 206, 250);
	private Color color_celda_normal = new Color(228, 227, 198);
	private Color color_celda_repetida = new Color(250, 108, 80);
	private Color color_celda_en_foco = new Color(119, 255, 92);
	private Juego juego;
	private JPanel panel_tablero;
	
	/**
	 * Servicio que inicia el juego con la muestra de un menú.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int juego_numero = menu_elegir_juego();
					if (juego_numero != 0) { //0 es salir del menú de elección con CANCEL.
						GUI_juego frame = new GUI_juego(juego_numero);
						frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static int menu_elegir_juego() {
		JPanel panel_menu = new JPanel();
		panel_menu.setLayout(new GridLayout(0, 1, 0, 20));
		
		JLabel lbl_texto = new JLabel("Elija un juego para comenzar:", SwingConstants.CENTER);
		lbl_texto.setOpaque(true);
		lbl_texto.setBackground(new Color(249, 231, 159));
		lbl_texto.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		lbl_texto.setFont(new Font("Arial", Font.BOLD, 14));
		panel_menu.add(lbl_texto);
		
		JPanel panel_botones = new JPanel();
		panel_botones.setLayout(new GridLayout(0, 2));
		panel_menu.add(panel_botones);
		
		int cant_juegos = 7; //Esto lo podemos hacer variar.
		int opcion_elegida = 0;
		ButtonGroup botones = new ButtonGroup(); //Esto se usa solamente para luego poder saber qué botón es el seleccionado.
		JRadioButton rb;
		
		for (int i = 1; i <= cant_juegos; i++) {
			rb = new JRadioButton(String.valueOf(i));
			rb.setHorizontalAlignment(SwingConstants.CENTER);
			if (i == 1) { //El juego número 1 es el seleccionado por defecto.
				rb.setSelected(true);
			}
			botones.add(rb);
			panel_botones.add(rb);
		}
		
		int respuesta = JOptionPane.showConfirmDialog(null, panel_menu, "SUDOKU - Elección de juego", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		//Si el jugador elije OK se recupera el número de opción (juego) elegida.
		if (respuesta == JOptionPane.OK_OPTION) {
			for (Enumeration<AbstractButton> buttons = botones.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				if (button.isSelected()) {
					opcion_elegida = Integer.parseInt( button.getText() );
				}
			}
		}

		return opcion_elegida;
	} //menu_elegir_juego
	
	private GUI_juego(int juego_numero) {
		//El objeto sobre el que trabajamos es el JFrame llamado 'frame' que creamos en main.
		setResizable(false);
		setTitle("Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(550, 550);
		setLocationRelativeTo(null);

		//Esto es para que la ventana del juego se cierre apretando la tecla Escape.
		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		Action escapeAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", escapeAction);

		//Contenedor (panel) principal.

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		//Panel superior, para el reloj del juego.
		
		Reloj panel_reloj = new Reloj();
		contentPane.add(panel_reloj, BorderLayout.NORTH);
		panel_reloj.setVisible(false); //Se habilita cuando se elige una opción de juego válida.
		
		//Panel medio, para el tablero del juego.

		setear_panel_tablero();
		contentPane.add(panel_tablero, BorderLayout.CENTER);
		
		//Panel inferior, que a su vez está compuesto de 2 paneles, uno para los botones y otro para un label que en ocasiones brinda información al usuario.
		
		JPanel panel_inferior = new JPanel();
		panel_inferior.setLayout(new BorderLayout(0, 0));
		contentPane.add(panel_inferior, BorderLayout.SOUTH);
		
		//Panel para los botones, y los botones que lo componen.
		
		JPanel panelBotones = new JPanel();
		panel_inferior.add(panelBotones, BorderLayout.NORTH);
		
		JButton btnReiniciar = new JButton("Reiniciar tablero");
		btnReiniciar.setMnemonic('R');
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					juego = new Juego(juego_numero);
					contentPane.remove(panel_tablero);
					setear_panel_tablero();
					contentPane.add(panel_tablero, BorderLayout.CENTER);
					panel_tablero.revalidate();
					generar_GUI_del_tablero(panel_tablero);
					panel_reloj.restart();
				} catch (TextFileException e1) {
					e1.printStackTrace();
				}
			}
		});
		panelBotones.add(btnReiniciar);
		
		JButton btnAbandonar = new JButton("Abandonar y salir");
		btnAbandonar.setMnemonic('A');
		btnAbandonar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panelBotones.add(btnAbandonar);
		
		JButton btnControlar = new JButton("Comprobar resoluci\u00F3n");
		btnControlar.setMnemonic('C');
		btnControlar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_reloj.stop();
				
				String resultado = juego.comprobar_resolucion();
				if (resultado.equals("INCOMPLETO")) {
					JOptionPane.showMessageDialog(null, "Hay celdas sin completar!", "SUDOKU INCOMPLETO", JOptionPane.WARNING_MESSAGE);
					panel_reloj.start(); //Reanuda.
				}
				else {
					if (resultado.equals("DISTINTOS")) {
						JOptionPane.showMessageDialog(null, "La solución no es correcta.", "SUDOKU CON ERRORES", JOptionPane.ERROR_MESSAGE);
						panel_reloj.start(); //Reanuda.
					}
					else { //resultado.equals("IGUALES")
						deshabilitar_tablero(panel_tablero);

						//Juego exitoso, se arma un mensaje que incluirá el tiempo total de juego.
						int[] tiempo_de_juego = panel_reloj.getValoresReloj();
						String msj_solucion_correcta = "La solución es correcta!\n\nTiempo total de juego:\n";
						msj_solucion_correcta += tiempo_de_juego[0] + " horas\n"; 
						msj_solucion_correcta += tiempo_de_juego[1] + " minutos\n";
						msj_solucion_correcta += tiempo_de_juego[2] + " segundos";

						JOptionPane.showMessageDialog(null, msj_solucion_correcta, "SUDOKU RESUELTO", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		panelBotones.add(btnControlar);
		
		//Panel para el label de mensajes.
		
		JPanel panelMensajes = new JPanel();
		panel_inferior.add(panelMensajes, BorderLayout.SOUTH);
		
		JLabel lblMensajes = new JLabel();
		panelMensajes.add(lblMensajes);

		//Creados y agregados a la GUI los componentes principales, se crea una instancia del juego e inicia el reloj:
		
		try {
			juego = new Juego(juego_numero); //Puede generar excepción en relación al archivo de texto desde el cual se crea.
			generar_GUI_del_tablero(panel_tablero);
			panel_reloj.setVisible(true);
			panel_reloj.start();
		} catch (TextFileException e) {
			lblMensajes.setText( e.getMessage() );
			btnReiniciar.setEnabled(false);
			btnAbandonar.setText("SALIR");
			btnAbandonar.setMnemonic('S');
			btnControlar.setEnabled(false);
		}
	} //GUI_juego

	private void setear_panel_tablero() {
		panel_tablero = new JPanel();
		panel_tablero.setLayout(new GridLayout(0, 9, 2, 2));
		panel_tablero.setBackground(Color.BLACK);
		panel_tablero.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
	}
	
	/*
	 * Las celdas que son pista tienen un color de fondo especial y no pueden ser alteradas.
	 * Las celdas que puede modificar el jugador tienen otro color, al igual que la celda que está clickeando.
	 * Cuando hay un conflicto por romper una regla del juego, se marcan con un color especial las celdas involucradas.
	 */
	private void generar_GUI_del_tablero(JPanel panel_tablero) {
		for (int i = 0; i < juego.getDimension(); i++) {
			for (int j = 0; j < juego.getDimension(); j++) {
				CeldaTablero c = juego.getCelda(i, j);
				ImageIcon imgCelda = c.getEntidadGraficaCelda().getImagen();
				JLabel label_celda = new JLabel();
				
				//Para las celdas que están en los bordes internos de los paneles les seteo un borde más grueso.
				if (i == 2 || i == 5) { //Borde inferior para paneles.
					label_celda.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
				}
				if (j == 2 || j == 5) {
					if (i == 2 || i == 5) { //Borde para esquinas inferiores derechas de paneles.
						label_celda.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 3, Color.BLACK));
					}
					else { //Borde derecho para paneles.
						label_celda.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK));
					}
				}
				
				label_celda.setOpaque(true);
				
				label_celda.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						redimensionar(label_celda, imgCelda);
						label_celda.setIcon(imgCelda);
					}
				});
				
				if (c.es_una_pista()) {
					label_celda.setBackground(color_celda_pista);
				}
				else {
					label_celda.setBackground(color_celda_normal);
					label_celda.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							restaurarColores();
							c.actualizar();
							if (c.getValor() != 0) {
								label_celda.setBackground(color_celda_en_foco);
							}
							
							//Esto es para marcar las celdas en conflicto con la actual, si las hay.
							ArrayList<CeldaTablero> repetidos = juego.chequearRepetidos(c);
							if (repetidos.size() > 0) {
								c.getComponente().setBackground(color_celda_repetida);
								for (CeldaTablero celda : repetidos) {
									celda.getComponente().setBackground(color_celda_repetida);
								}
							}
							redimensionar(label_celda, imgCelda);
						}
					});
				}
				
				c.setComponente(label_celda);
				panel_tablero.add(label_celda);
			}
		}
	} //generar_GUI_del_tablero
	
	//Adapta la imagen contenida en img al tamaño deseado tomando como referencia el tamaño de label.
	private void redimensionar(JLabel label, ImageIcon img) {
		Image image = img.getImage();
		if (image != null) {
			Image newimg = image.getScaledInstance(label.getWidth()-3, label.getHeight()-3, java.awt.Image.SCALE_SMOOTH);
			img.setImage(newimg);
			label.repaint();
		}
	}

	//Setea el color de cada celda del tablero a su color original (una celda pierde su color original
	//si es marcada como repetida/en conflicto con la celda que está siendo actualizada).
	private void restaurarColores() {
		CeldaTablero c;
		for (int i = 0; i < juego.getDimension(); i++) {
			for (int j = 0; j < juego.getDimension(); j++) {
				c = juego.getCelda(i, j);
				Color color = c.es_una_pista() ? color_celda_pista : color_celda_normal;
				c.getComponente().setBackground(color);
			}
		}
	}
	
	private void deshabilitar_tablero(JPanel panel) {
		for (Component component : panel.getComponents()) {
			//Si se deshabilita el tablero pero no los oyentes de los label, se puede seguir cambiando el color de las celdas.
			//Esto es para evitar eso.
			if (component instanceof JLabel) {
				java.awt.event.MouseListener[] ml = component.getMouseListeners();
				if (ml.length > 0) { //Si es > 0 solo puede valer 1 (porque a cada label le asociamos un solo mouse listener).
					component.removeMouseListener(ml[0]);
				}
			}
			component.setEnabled(false);
		}
	}
	
}
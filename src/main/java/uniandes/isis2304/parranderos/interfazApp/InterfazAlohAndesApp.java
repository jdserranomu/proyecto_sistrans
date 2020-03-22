package uniandes.isis2304.parranderos.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.AlohAndes;
import uniandes.isis2304.parranderos.negocio.Apartamento;
import uniandes.isis2304.parranderos.negocio.Habitacion;
import uniandes.isis2304.parranderos.negocio.HabitacionHostal;
import uniandes.isis2304.parranderos.negocio.HabitacionHotel;
import uniandes.isis2304.parranderos.negocio.HabitacionVivienda;
import uniandes.isis2304.parranderos.negocio.Horario;
import uniandes.isis2304.parranderos.negocio.Inmueble;
import uniandes.isis2304.parranderos.negocio.OfreceServicio;
import uniandes.isis2304.parranderos.negocio.Operador;
import uniandes.isis2304.parranderos.negocio.PersonaJuridica;
import uniandes.isis2304.parranderos.negocio.PersonaNatural;
import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.ServicioMenaje;
import uniandes.isis2304.parranderos.negocio.Usuario;
import uniandes.isis2304.parranderos.negocio.Vivienda;

@SuppressWarnings("serial")
public class InterfazAlohAndesApp extends JFrame implements ActionListener{
	
	private static Logger log = Logger.getLogger(InterfazAlohAndesApp.class.getName());
	
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigAppAlohAndes.json";
	
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	private JsonObject tableConfig;
	
	private AlohAndes alohAndes;
	
    private JsonObject guiConfig;
    
    private PanelDatos panelDatos;
    
    private JMenuBar menuBar;
    
    public InterfazAlohAndesApp( ){
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        alohAndes = new AlohAndes(tableConfig);
        
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
    /* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try {
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e){
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    private void configurarFrame(  ){
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	if ( guiConfig == null ){
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );
        setTitle( titulo );
		setSize ( ancho, alto);        
    }
    
    
    private void crearMenu(  JsonArray jsonMenu ){    	
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu){
        	JsonObject jom = men.getAsJsonObject(); 
        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	JMenu menu = new JMenu( menuTitle);
        	for (JsonElement op : opciones){
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
    /* ****************************************************************
	 * 			CRUD de Apartamento
	 *****************************************************************/
    
    public void adicionarApartamento ()
	{
    	//int amoblado, double precioMes, long idPersona, String direccion, int capacidad, int disponible, Date fechaReservaFinal
		try {
			int amoblado = Integer.parseInt(JOptionPane.showInputDialog (this, "Amoblado?", "Adicionar apartamento", JOptionPane.QUESTION_MESSAGE));
			double precioMes = Double.parseDouble(JOptionPane.showInputDialog (this, "Precio mes?", "Adicionar apartamento", JOptionPane.QUESTION_MESSAGE));
			long idPersona = Long.parseLong(JOptionPane.showInputDialog (this, "Id Dueño?", "Adicionar apartamento", JOptionPane.QUESTION_MESSAGE));
			String direccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar apartamento", JOptionPane.QUESTION_MESSAGE);
			int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar apartamento", JOptionPane.QUESTION_MESSAGE));
			int disponible = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponible?", "Adicionar apartamento", JOptionPane.QUESTION_MESSAGE));
			Apartamento apto = alohAndes.adicionarApartamento(amoblado, precioMes, idPersona, direccion, capacidad, disponible, null);
			if(apto == null) {
				throw new Exception ("No se pudo crear apartamento");
			}
			else {
				String resultado = "En adicionarApartamento\n\n";
        		resultado += "Apartamento adicionado exitosamente: " + apto;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
			}
			
			
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
        
	}
    
    public void darApartamentos (){
		try {
			List <Apartamento> lista = alohAndes.darApartamentos();

			String resultado = "En darApartamentos";
			resultado +=  "\n" + listarApartamento(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void darApartamentoPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar apartamento por Id", JOptionPane.QUESTION_MESSAGE));
    		Apartamento apto = alohAndes.darApartamentoPorId(id);
    		String resultado = "En darApartamentoPorId\n\n";
    		if(apto!=null) {
    			resultado += "El apartamento es: " + apto;
    		}
    		else {
    			resultado += "El apto con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darApartamentosPorIdPersona(){
		try {
			int idPersona = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar apartamento por Id Persona", JOptionPane.QUESTION_MESSAGE));
			List <Apartamento> lista = alohAndes.darApartamentosPorIdPersona(idPersona);

			String resultado = "En darApartamentosPorIdPersona";
			resultado +=  "\n" + listarApartamento(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    
    
    
    /* ****************************************************************
	 * 			CRUD de Habitacion
	 *****************************************************************/
    
    public void adicionarHabitacion ()
	{
    	//double tamanho, double precioMes, long idPersona, String direccion, int capacidad, int disponible, Date fechaReservaFinal
		try {
			double tamanho = Double.parseDouble(JOptionPane.showInputDialog (this, "Tamaño?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			double precioMes = Double.parseDouble(JOptionPane.showInputDialog (this, "Precio mes?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			long idPersona = Long.parseLong(JOptionPane.showInputDialog (this, "Id Dueño?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			String direccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE);
			int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			int disponible = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponible?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			Habitacion habitacion = alohAndes.adicionarHabitacion(tamanho, precioMes, idPersona, direccion, capacidad, disponible, null);
			if(habitacion == null) {
				throw new Exception ("No se pudo crear habitacion");
			}
			else {
				String resultado = "En adicionarHabitacion\n\n";
        		resultado += "Habitacion adicionada exitosamente: " + habitacion;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
			}
			
			
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
        
	}
    
    public void darHabitaciones(){
		try {
			List <Habitacion> lista = alohAndes.darHabitaciones();

			String resultado = "En darHabitaciones";
			resultado +=  "\n" + listarHabitacion(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void darHabitacionPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar habitacion por Id", JOptionPane.QUESTION_MESSAGE));
    		Habitacion habitacion = alohAndes.darHabitacionPorId(id);
    		String resultado = "En darHabitacionPorId\n\n";
    		if(habitacion!=null) {
    			resultado += "La habitacion es: " + habitacion;
    		}
    		else {
    			resultado += "La habitacion con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darHabitacionesPorIdPersona(){
		try {
			int idPersona = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar habitacion por Id Persona", JOptionPane.QUESTION_MESSAGE));
			List <Habitacion> lista = alohAndes.darHabitacionesPorIdPersona(idPersona);

			String resultado = "En darHabitacionesPorIdPerson";
			resultado +=  "\n" + listarHabitacion(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    
    /* ****************************************************************
	 * 			CRUD de Habitacion Hostal
	 *****************************************************************/
    
    public void adicionarHabitacionHostal ()
	{
    	//int numero, long idHostal, String direccion, int capacidad, int disponible, Date fechaReservaFinal
		try {
			int numero = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero?", "Adicionar habitacion hostal", JOptionPane.QUESTION_MESSAGE));
			long idHostal = Long.parseLong(JOptionPane.showInputDialog (this, "Id Hostal?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			String direccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE);
			int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			int disponible = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponible?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			HabitacionHostal habitacion = alohAndes.adicionarHabitacionHostal(numero, idHostal, direccion, capacidad, disponible, null);
			if(habitacion == null) {
				throw new Exception ("No se pudo crear habitacion");
			}
			else {
				String resultado = "En adicionarHabitacionHostal\n\n";
        		resultado += "Habitacion Hostal adicionada exitosamente: " + habitacion;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
			}
			
			
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
        
	}
    
    public void darHabitacionesHostales(){
		try {
			List <HabitacionHostal> lista = alohAndes.darHabitacionesHostales();

			String resultado = "En darHabitacionesHostales";
			resultado +=  "\n" + listarHabitacionhostal(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void darHabitacionHostalPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar habitacion hostal por Id", JOptionPane.QUESTION_MESSAGE));
    		HabitacionHostal habitacion = alohAndes.darHabitacionHostalPorId(id);
    		String resultado = "En darHabitacionHostalPorId\n\n";
    		if(habitacion!=null) {
    			resultado += "La habitacion hostal es: " + habitacion;
    		}
    		else {
    			resultado += "La habitacion hostal con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darHabitacionesHostal(){
		try {
			int idHostal = Integer.parseInt(JOptionPane.showInputDialog (this, "Id Hostal?", "Buscar habitacion por Id Persona", JOptionPane.QUESTION_MESSAGE));
			List <HabitacionHostal> lista = alohAndes.darHabitacionesHostal(idHostal);

			String resultado = "En darHabitacionesHostal";
			resultado +=  "\n" + listarHabitacionhostal(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    /* ****************************************************************
	 * 			CRUD de Habitacion Hotel
	 *****************************************************************/
    public void adicionarHabitacionHotel ()
	{
    	//long idHotel, int numero, String tipo, double precioNoche, double tamanho, String ubicacion, String direccion, int capacidad, int disponible, Date fechaReservaFinal
		try {
			int numero = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			long idHotel = Long.parseLong(JOptionPane.showInputDialog (this, "Id Hostal?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			String[] choices = {HabitacionHotel.TIPO_ESTANDAR, HabitacionHotel.TIPO_SEMISUITE, HabitacionHotel.TIPO_SUITE};
    		String tipo = (String) JOptionPane.showInputDialog(null, "Elegir tipo habitacion","Adicionar habitacion", JOptionPane.QUESTION_MESSAGE, null,choices, choices[2]);
    		double precioNoche = Double.parseDouble(JOptionPane.showInputDialog (this, "Precio noche?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
    		double tamanho = Double.parseDouble(JOptionPane.showInputDialog (this, "Tamaño?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
    		String ubicacion  = JOptionPane.showInputDialog (this, "Ubicacion?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE);
			String direccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE);
			int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			int disponible = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponible?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			HabitacionHotel habitacion = alohAndes.adicionarHabitacionHotel(idHotel, numero, tipo, precioNoche, tamanho, ubicacion, direccion, capacidad, disponible, null);
			if(habitacion == null) {
				throw new Exception ("No se pudo crear habitacion");
			}
			else {
				String resultado = "En adicionarHabitacionHotel\n\n";
        		resultado += "Habitacion Hotel adicionada exitosamente: " + habitacion;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
			}
			
			
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
        
	}
    
    public void darHabitacionesHoteles(){
		try {
			List <HabitacionHotel> lista = alohAndes.darHabitacionesHoteles();

			String resultado = "En darHabitacionesHoteles";
			resultado +=  "\n" + listarHabitacionHotel(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void darHabitacionHostelPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar habitacion hotel por Id", JOptionPane.QUESTION_MESSAGE));
    		HabitacionHotel habitacion = alohAndes.darHabitacionHotelPorId(id);
    		String resultado = "En darHabitacionHostelPorId\n\n";
    		if(habitacion!=null) {
    			resultado += "La habitacion hotel es: " + habitacion;
    		}
    		else {
    			resultado += "La habitacion hotel con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darHabitacionesHotel(){
		try {
			int idHotel = Integer.parseInt(JOptionPane.showInputDialog (this, "Id Hotel?", "Buscar habitacion por Id Hotel", JOptionPane.QUESTION_MESSAGE));
			List <HabitacionHotel> lista = alohAndes.darHabitacionesHotel(idHotel);

			String resultado = "En darHabitacionesHotel";
			resultado +=  "\n" + listarHabitacionHotel(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    /* ****************************************************************
	 * 			CRUD de Habitacion Vivienda
	 *****************************************************************/
    
    public void adicionarHabitacionVivienda ()
	{
    	// long idVivienda, int numero, double precioSemestre, double precioMes, double precioNoche, String ubicacion, int individual, String direccion, int capacidad, int disponible, Date fechaReservaFinal
    	try {
			int numero = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			long idVivienda = Long.parseLong(JOptionPane.showInputDialog (this, "Id Vivienda?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
    		double precioNoche = Double.parseDouble(JOptionPane.showInputDialog (this, "Precio noche?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
    		double precioMes = Double.parseDouble(JOptionPane.showInputDialog (this, "Precio mes?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
    		double precioSemestre = Double.parseDouble(JOptionPane.showInputDialog (this, "Precio semestre?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
    		String ubicacion  = JOptionPane.showInputDialog (this, "Ubicacion?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE);
    		int individual = Integer.parseInt(JOptionPane.showInputDialog (this, "Individual?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
    		String direccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE);
			int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			int disponible = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponible?", "Adicionar habitacion", JOptionPane.QUESTION_MESSAGE));
			HabitacionVivienda habitacion = alohAndes.adicionarHabitacionVivienda(idVivienda, numero, precioSemestre, precioMes, precioNoche, ubicacion, individual, direccion, capacidad, disponible, null);
			if(habitacion == null) {
				throw new Exception ("No se pudo crear habitacion vivienda");
			}
			else {
				String resultado = "En adicionarHabitacionVivienda\n\n";
        		resultado += "Habitacion Vivienda adicionada exitosamente: " + habitacion;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
			}
			
			
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
        
	}
    
    public void darHabitacionesViviendas(){
		try {
			List <HabitacionVivienda> lista = alohAndes.darHabitacionesViviendas();

			String resultado = "En darHabitacionesViviendas";
			resultado +=  "\n" + listarHabitacionVivienda(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void darHabitacionViviendaPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar habitacion vivienda por Id", JOptionPane.QUESTION_MESSAGE));
    		HabitacionVivienda habitacion = alohAndes.darHabitacionViviendaPorId(id);
    		String resultado = "En darHabitacionViviendaPorId\n\n";
    		if(habitacion!=null) {
    			resultado += "La habitacion vivienda es: " + habitacion;
    		}
    		else {
    			resultado += "La habitacion vivienda con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darHabitacionesVivienda(){
		try {
			int idVivienda = Integer.parseInt(JOptionPane.showInputDialog (this, "Id Vivienda?", "Buscar habitacion por Id Vivienda", JOptionPane.QUESTION_MESSAGE));
			List <HabitacionVivienda> lista = alohAndes.darHabitacionesVivienda(idVivienda);

			String resultado = "En darHabitacionesVivienda";
			resultado +=  "\n" + listarHabitacionVivienda(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    /* ****************************************************************
	 * 			CRUD de Horario
	 *****************************************************************/
    
    public void adicionarHorario()
	{
    	try {
    		//long idHostal, String dia, int horaAbre, int horaCierra
    		int horaAbre = Integer.parseInt(JOptionPane.showInputDialog (this, "Hora abre?", "Adicionar horario", JOptionPane.QUESTION_MESSAGE));
    		int horaCierra = Integer.parseInt(JOptionPane.showInputDialog (this, "Hora cierra?", "Adicionar horario", JOptionPane.QUESTION_MESSAGE));
			long idHostal = Long.parseLong(JOptionPane.showInputDialog (this, "Id Hostal?", "Adicionar horario", JOptionPane.QUESTION_MESSAGE));
			String[] choices = {Horario.DIA_LUNES, Horario.DIA_MARTES, Horario.DIA_MIERCOLES, Horario.DIA_JUEVES, Horario.DIA_VIERNES, Horario.DIA_SABADO,
					Horario.DIA_DOMINGO };
    		String dia = (String) JOptionPane.showInputDialog(null, "Elegir dia","Adicionar horario", JOptionPane.QUESTION_MESSAGE, null,choices, choices[0]);
    		Horario horario = alohAndes.adicionarHorario(idHostal, dia, horaAbre, horaCierra);
    		if(horario == null) {
				throw new Exception ("No se pudo crear horario");
			}
			else {
				String resultado = "En adicionarHorario\n\n";
        		resultado += "Horario adicionado exitosamente: " + horario;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
			}
    		
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void eliminarHorarioPorIdHostalYDia() {
		try {
			//long idHostal, String dia
			long idHostal = Long.parseLong(JOptionPane.showInputDialog (this, "Id Hostal?", "Adicionar horario", JOptionPane.QUESTION_MESSAGE));
			String[] choices = {Horario.DIA_LUNES, Horario.DIA_MARTES, Horario.DIA_MIERCOLES, Horario.DIA_JUEVES, Horario.DIA_VIERNES, Horario.DIA_SABADO,
					Horario.DIA_DOMINGO };
    		String dia = (String) JOptionPane.showInputDialog(null, "Elegir dia","Adicionar horario", JOptionPane.QUESTION_MESSAGE, null,choices, choices[0]);
    		long tbEliminados = alohAndes.eliminarHorarioPorIdHostalYDia(idHostal, dia);
			String resultado = "En eliminarHorarioPorIdHostalYDia\n\n";
			resultado += tbEliminados + " Horarios eliminados\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void darHorarioPorIdHostalYDia ()
	{
		try {
    		//long idHostal, String dia
			long idHostal = Long.parseLong(JOptionPane.showInputDialog (this, "Id Hostal?", "Adicionar horario", JOptionPane.QUESTION_MESSAGE));
			String[] choices = {Horario.DIA_LUNES, Horario.DIA_MARTES, Horario.DIA_MIERCOLES, Horario.DIA_JUEVES, Horario.DIA_VIERNES, Horario.DIA_SABADO,
					Horario.DIA_DOMINGO };
    		String dia = (String) JOptionPane.showInputDialog(null, "Elegir dia","Adicionar horario", JOptionPane.QUESTION_MESSAGE, null,choices, choices[0]);
    		Horario horario = alohAndes.darHorarioPorIdHostalYDia(idHostal, dia);
    		String resultado = "En buscar horario por id hostal y dia\n\n";
    		if (horario != null)
			{
    			resultado += "El horario es: " + horario;
			}
			else
			{
    			resultado += "Un horario: " + dia + " " + idHostal + " NO EXISTE\n";    				
			}
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void darHorarios ()
	{
		try {
			List <Horario> lista = alohAndes.darHorarios();

			String resultado = "En darHorario";
			resultado +=  "\n" + listarHorario (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void darHorariosPorIdHostal()
	{
		try {
			long idHostal = Long.parseLong(JOptionPane.showInputDialog (this, "Id Hostal?", "Adicionar horario", JOptionPane.QUESTION_MESSAGE));
			List <Horario> lista = alohAndes.darHorariosPorIdHostal((int)idHostal);
			String resultado = "En darHorario";
			resultado +=  "\n" + listarHorario (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    /* ****************************************************************
	 * 			CRUD de Inmueble
	 *****************************************************************/
    
	public void eliminarInmueblePorId() {
		try {
			//long idInmueble
			long idInmueble = Long.parseLong(JOptionPane.showInputDialog (this, "Id Inmueble?", "Borrar Inmueble por Id", JOptionPane.QUESTION_MESSAGE));
			long tbEliminados = alohAndes.eliminarInmueblePorId(idInmueble);

			String resultado = "En eliminar Inmueble\n\n";
			resultado += tbEliminados + " Inmuebles eliminados\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void darInmueblePorId()
	{
		try {
			
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void darInmuebles ()
	{
		try {
			List <Inmueble> lista = alohAndes.darInmuebles();
			String resultado = "En darInmuebles";
			resultado +=  "\n" + listarInmueble (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void darInmueblesPorMayorCapacidad ()
	{
		try {
			int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Listar inmubles por capacidad mayor", JOptionPane.QUESTION_MESSAGE));
			List <Inmueble> lista = alohAndes.darInmueblesPorMayorCapacidad(capacidad);
			String resultado = "En darInmueblesPorMayorCapacidad";
			resultado +=  "\n" + listarInmueble (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void darInmueblesPorTipo ()
	{
		try {
			String[] choices = {Inmueble.TIPO_APARTAMENTO, Inmueble.TIPO_HABITACION, Inmueble.TIPO_HABITACIONHOSTAL, Inmueble.TIPO_HABITACIONHOTEL, 
					Inmueble.TIPO_HABITACIONVIVIENDA, Inmueble.TIPO_VIVIENDA};
    		String tipo = (String) JOptionPane.showInputDialog(null, "Elegir tipo","Listar inmueble por tipo", JOptionPane.QUESTION_MESSAGE, null,choices, choices[0]);
			List <Inmueble> lista = alohAndes.darInmueblesPorTipo(tipo);
			String resultado = "En darInmueblesPorMayorCapacidad";
			resultado +=  "\n" + listarInmueble (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void darInmueblesPorDisponibilidad()
	{
		try {
			int disponibilidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponibilidad?", "Listar inmuebles por disponibilidad", JOptionPane.QUESTION_MESSAGE));
			List <Inmueble> lista = alohAndes.darInmueblesPorDisponibilidad(disponibilidad);
			String resultado = "En darInmueblesPorMayorCapacidad";
			resultado +=  "\n" + listarInmueble (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
    /* ****************************************************************
	 * 			CRUD de OfreceServicio
	 *****************************************************************/
    
    /* ****************************************************************
	 * 			CRUD de Operador
	 *****************************************************************/
    
    /* ****************************************************************
	 * 			CRUD de Persona Juridica
	 *****************************************************************/
    
    /* ****************************************************************
	 * 			CRUD de Persona Natural
	 *****************************************************************/
    
    /* ****************************************************************
	 * 			CRUD de Reserva
	 *****************************************************************/
    public void adicionarReserva( )
    {
    	try 
    	{
    		//Date fechaInicio, Date fechaFin, double valorTotal, Date fechaCancelacion, int pagado, 
			//double descuento, int capacidad, int estado, long idOperador, long idUsuario, long idInmueble
			
			String fechaIni= JOptionPane.showInputDialog (this, "Fecha (YYYY-MM-DD)?", "Adicionar fecha Inicio", JOptionPane.QUESTION_MESSAGE); 
			Date fechaInicio=Date.valueOf(fechaIni);
			
			String fechafi= JOptionPane.showInputDialog (this, "Fecha (YYYY-MM-DD)?", "Adicionar fecha fin", JOptionPane.QUESTION_MESSAGE); 
			Date fechaFin=Date.valueOf(fechafi);
			
			int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "capacidad?", "Adicionar capacidad", JOptionPane.QUESTION_MESSAGE));
    		long idUsuario = Long.parseLong(JOptionPane.showInputDialog (this, "id?", "Adicionar id del usuario", JOptionPane.QUESTION_MESSAGE));
    		long idInmueble = Long.parseLong(JOptionPane.showInputDialog (this, "inmueble?", "Adicionar id del inmueble", JOptionPane.QUESTION_MESSAGE));
		
    		Inmueble inmu=this.darInmueblePor
    		
    		String[] choices = {ServicioMenaje.TIPO_MENAJE, ServicioMenaje.TIPO_SERVICIO};
    		String tipo = (String) JOptionPane.showInputDialog(null, "Selecciones el tipo","Seleccione el tipo", JOptionPane.QUESTION_MESSAGE, null,choices, choices[1]);
    	
    		if (nombre!=null && tipo!=null)
    		{
        		ServicioMenaje sm = alohAndes.adicionarServicioMenaje(nombre, tipo);
        		if (sm == null)
        		{
        			throw new Exception ("No se pudo crear un servicio/menaje con nombre: " + nombre);
        		}
        		String resultado = "En adicionarServicioMenaje\n\n";
        		resultado += "Servicio Menaje adicionado exitosamente: " + sm;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * 			CRUD de Servicio Menaje
	 *****************************************************************/
    public void adicionarServicioMenaje( )
    {
    	try 
    	{
    		String nombre = JOptionPane.showInputDialog (this, "Nombre?", "Adicionar nombre del servicio/menaje", JOptionPane.QUESTION_MESSAGE);
    		
    		String[] choices = {ServicioMenaje.TIPO_MENAJE, ServicioMenaje.TIPO_SERVICIO};
    		String tipo = (String) JOptionPane.showInputDialog(null, "Selecciones el tipo","Seleccione el tipo", JOptionPane.QUESTION_MESSAGE, null,choices, choices[1]);
    	
    		if (nombre!=null && tipo!=null)
    		{
        		ServicioMenaje sm = alohAndes.adicionarServicioMenaje(nombre, tipo);
        		if (sm == null)
        		{
        			throw new Exception ("No se pudo crear un servicio/menaje con nombre: " + nombre);
        		}
        		String resultado = "En adicionarServicioMenaje\n\n";
        		resultado += "Servicio Menaje adicionado exitosamente: " + sm;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void eliminarServicioMenajePorNombre() {
    	try {
    		String nombre= JOptionPane.showInputDialog (this, "nombre?", "Buscar servicio/menaje por nombre", JOptionPane.QUESTION_MESSAGE);
    		long us = alohAndes.eliminarServicioMenajePorNombre(nombre);
    		String resultado = "En buscar servicio/menaje por Id\n\n";
    		if(us!=-1) {
    			resultado += "fue eliminado el servicio/menaje con nombre: " + us;
    		}
    		else {
    			resultado += "El serivicio/menaje con nombre :"+nombre+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void darServicioMenajePorNombre() {
    	try {
    		String nombre=JOptionPane.showInputDialog (this, "Id?", "Buscar servicio menaje por nombre", JOptionPane.QUESTION_MESSAGE);
    		ServicioMenaje us = alohAndes.darServicioMenajePorNombre(nombre);
    		String resultado = "En buscar Servicio/Menaje por nombre\n\n";
    		if(us!=null) {
    			resultado += "El Servicio/menaje es: " + us;
    		}
    		else {
    			resultado += "El servicio/menaje con nombre :"+nombre+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darServiciosMenajesPorTipo() {
    	try {
    		String[] choices = {ServicioMenaje.TIPO_MENAJE, ServicioMenaje.TIPO_SERVICIO};
    		String tipo = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo","Selecciones el tipo", JOptionPane.QUESTION_MESSAGE, null,choices, choices[1]);
    	
    		List <ServicioMenaje> lista = alohAndes.darServiciosMenajesPorTipo(tipo);
			
			String resultado = "En listaServicioMenaje";
			resultado +=  "\n" + listarServicioMenaje(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * 			CRUD de Usuario
	 *****************************************************************/
    
    public void adicionarUsuario( )
    {
    	try 
    	{
    		String nombre = JOptionPane.showInputDialog (this, "Nombre?", "Adicionar nombre del usuario", JOptionPane.QUESTION_MESSAGE);
    		String email = JOptionPane.showInputDialog (this, "Email?", "Adicionar email del usuario", JOptionPane.QUESTION_MESSAGE);
    		String telefono = JOptionPane.showInputDialog (this, "Telefono?", "Adicionar telefono de usuario", JOptionPane.QUESTION_MESSAGE);
    		String[] choices = {Usuario.TIPO_ESTUDIANTE, Usuario.TIPO_PROFESOR, Usuario.TIPO_PROFESORINVITADO, Usuario.TIPO_EMPLEADO,
    				Usuario.TIPO_EGRESADO, Usuario.TIPO_PADRAESTUDIANTE, Usuario.TIPO_INVITADO};
    		String tipo = (String) JOptionPane.showInputDialog(null, "Choose now...","The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null,choices, choices[1]);
    	
    		if (nombre!=null && email!=null && telefono!=null && tipo!=null)
    		{
        		Usuario usuario = alohAndes.adicionarUsuario(nombre, email, telefono, tipo);
        		if (usuario == null)
        		{
        			throw new Exception ("No se pudo crear un usuario con nombre: " + nombre);
        		}
        		String resultado = "En adicionarUsuario\n\n";
        		resultado += "Usuario adicionado exitosamente: " + usuario;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darUsuarios (){
		try {
			List <Usuario> lista = alohAndes.darUsuarios();

			String resultado = "En listaUsuarios";
			resultado +=  "\n" + listarUsuario(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    public void eliminarUsuarioPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar usuarios por Id", JOptionPane.QUESTION_MESSAGE));
    		long us = alohAndes.eliminarUsuario(id);
    		String resultado = "En buscar Usuario por Id\n\n";
    		if(us!=-1) {
    			resultado += "fue eliminado cel usuario con id: " + us;
    		}
    		else {
    			resultado += "El usuario con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void darUsuarioPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar usuarios por Id", JOptionPane.QUESTION_MESSAGE));
    		Usuario us = alohAndes.darUsuarioPorId(id);
    		String resultado = "En buscar Usuario por Id\n\n";
    		if(us!=null) {
    			resultado += "El usuario es es: " + us;
    		}
    		else {
    			resultado += "El usuario con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darUsuarioPorEmail(){
		try {
			String mail = JOptionPane.showInputDialog (this, "Email?", "Buscar usuario por email", JOptionPane.QUESTION_MESSAGE);
			Usuario bus = alohAndes.darUsuarioPorEmail(mail);

			String resultado = "En buscar Usuario por mail\n\n";
    		if(bus!=null) {
    			resultado += "El usuario es es: " + bus;
    		}
    		else {
    			resultado += "El usuario con email :"+mail+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void darUsuariosPorTipo (){
    	
		try {
			
			String[] choices = {Usuario.TIPO_EGRESADO, Usuario.TIPO_EMPLEADO, Usuario.TIPO_ESTUDIANTE, Usuario.TIPO_INVITADO, Usuario.TIPO_PADRAESTUDIANTE,
					Usuario.TIPO_PROFESOR, Usuario.TIPO_PROFESORINVITADO};
    		String tipo = (String) JOptionPane.showInputDialog(null, "Selecciones el tipo","Selecciones el tipo", JOptionPane.QUESTION_MESSAGE, null,choices, choices[1]);
			List <Usuario> lista = alohAndes.darUsuariosPorTipo(tipo);
			
			String resultado = "En listaUsuarios";
			resultado +=  "\n" + listarUsuario(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    
    /* ****************************************************************
	 * 			CRUD de Vivienda
	 *****************************************************************/
    public void adicionarVivienda( )
    {
    	try 
    	{
    		int numeroHabitaciones = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero Habitaciones?", "Adicionar Numero de Habitaciones", JOptionPane.QUESTION_MESSAGE));
    		double costoNoche = Double.parseDouble(JOptionPane.showInputDialog (this, "Costo Noche?", "Adicionar Costo por noche", JOptionPane.QUESTION_MESSAGE));
    		long idPersona = Long.parseLong(JOptionPane.showInputDialog (this, "id del dueño?", "Adicionar id del dueño", JOptionPane.QUESTION_MESSAGE));
    		String direccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar direccion", JOptionPane.QUESTION_MESSAGE);
    		int capacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar capacidad", JOptionPane.QUESTION_MESSAGE));
    		int disponible = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponible?", "Adicionar Disponible", JOptionPane.QUESTION_MESSAGE));
    		
    		Vivienda vivienda=alohAndes.adicionarVivienda(numeroHabitaciones, costoNoche, 0, idPersona, direccion, capacidad, disponible, null);
    		if(vivienda == null) {
				throw new Exception ("No se pudo crear la vivienda");
			}
			else {
				String resultado = "En adicionarVivienda\n\n";
        		resultado += "Vivienda adicionada exitosamente: " + vivienda;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
			}
		} 
    	catch (Exception e) 
    	{
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
 
    public void darViviendas (){
		try {
			List <Vivienda> lista = alohAndes.darViviendas();

			String resultado = "En listaViviendas";
			resultado +=  "\n" + listarVivenda(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void darViviendaPorId() {
    	try {
    		int id = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar vivienda por Id", JOptionPane.QUESTION_MESSAGE));
    		Vivienda viv = alohAndes.darViviendaPorId(id);
    		String resultado = "En buscar Vivienda por Id\n\n";
    		if(viv!=null) {
    			resultado += "La vivienda es: " + viv;
    		}
    		else {
    			resultado += "La vivienda con id :"+id+" no existe";
    		}
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void darViviendaPorIdPersona(){
		try {
			int idPersona = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Buscar vivienda por Id Persona", JOptionPane.QUESTION_MESSAGE));
			List <Vivienda> lista = alohAndes.darViviendaPorIdPersona(idPersona);

			String resultado = "En listarVivienda";
			resultado +=  "\n" + listarVivenda(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
	
    /* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/

	public void mostrarLogParranderos (){
		mostrarArchivo ("parranderos.log");
	}
	
	public void mostrarLogDatanuecleus (){
		mostrarArchivo ("datanucleus.log");
	}
	
	public void limpiarLogParranderos (){
		boolean resp = limpiarArchivo ("parranderos.log");
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}

	public void limpiarLogDatanucleus (){
		boolean resp = limpiarArchivo ("datanucleus.log");
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";
		panelDatos.actualizarInterfaz(resultado);
	}

	public void limpiarBD (){
		try {
			long eliminados [] = alohAndes.limpiarAlohAndes();
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Apartamentos eliminados\n";
			resultado += eliminados [1] + " Habitaciones eliminados\n";
			resultado += eliminados [2] + " Hab. Hostal eliminados\n";
			resultado += eliminados [3] + " Hab. Hotel eliminados\n";
			resultado += eliminados [4] + " Hab. Vivienda eliminados\n";
			resultado += eliminados [5] + " Horario eliminados\n";
			resultado += eliminados [6] + " Inmueble eliminados\n";
			resultado += eliminados [7] + " Ofrece Servicio eliminados\n";
			resultado += eliminados [8] + " Operador eliminados\n";
			resultado += eliminados [9] + " Per. Juridica eliminados\n";
			resultado += eliminados [10] + " Per. Natural eliminados\n";
			resultado += eliminados [11] + " Reserva eliminados\n";
			resultado += eliminados [12] + " Servicio/Menaje eliminados\n";
			resultado += eliminados [13] + " Usuario eliminados\n";
			resultado += eliminados [14] + " Vivienda eliminados\n";
			resultado += "\nLimpieza terminada";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) {
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void mostrarPresentacionGeneral (){
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}

	public void mostrarModeloConceptual (){
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}

	public void mostrarEsquemaBD (){
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}

	public void mostrarScriptBD (){
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}

	public void mostrarArqRef (){
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	public void mostrarJavadoc (){
		mostrarArchivo ("doc/index.html");
	}
    /* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/

    private String listarApartamento(List<Apartamento> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarHabitacion(List<Habitacion> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarHabitacionhostal(List<HabitacionHostal> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarHabitacionHotel(List<HabitacionHotel> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarHabitacionVivienda(List<HabitacionVivienda> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarHorario(List<Horario> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarInmueble(List<Inmueble> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarOfreceServicio(List<OfreceServicio> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

    private String listarOperador(List<Operador> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarPersonaJuridica(List<PersonaJuridica> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarPersonaNatural(List<PersonaNatural> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarReserva(List<Reserva> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarServicioMenaje(List<ServicioMenaje> lista) {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarUsuario(List<Usuario> lista) {
    	String resp = "Los usuarios existentes son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    private String listarVivenda(List<Vivienda> lista) {
    	String resp = "Los tipos de vivienda son:\n";
    	int i = 1;
        for (Object tb : lista){
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	private String generarMensajeError(Exception e){
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	private boolean limpiarArchivo(String nombreArchivo) {
		BufferedWriter bw;
		try{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) {
			return false;
		}
	}

	private void mostrarArchivo (String nombreArchivo){
		try{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
	
	public void actionPerformed(ActionEvent e) {
		String evento = e.getActionCommand( );		
        try 
        {
			Method req = InterfazAlohAndesApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception exception) 
        {
			exception.printStackTrace();
		} 
	}
	
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
	
	public static void main( String[] args ){
        try{
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazAlohAndesApp interfaz = new InterfazAlohAndesApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e ){
            e.printStackTrace( );
        }
    }
	

}
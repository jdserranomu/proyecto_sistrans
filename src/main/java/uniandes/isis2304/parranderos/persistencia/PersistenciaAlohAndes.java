package uniandes.isis2304.parranderos.persistencia;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
import uniandes.isis2304.parranderos.negocio.ReqConsulta1;
import uniandes.isis2304.parranderos.negocio.ReqConsulta2;
import uniandes.isis2304.parranderos.negocio.ReqConsulta3;
import uniandes.isis2304.parranderos.negocio.ReqConsulta4;
import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.ServicioMenaje;
import uniandes.isis2304.parranderos.negocio.Usuario;
import uniandes.isis2304.parranderos.negocio.Vivienda;

public class PersistenciaAlohAndes {

	private static Logger log = Logger.getLogger(PersistenciaAlohAndes.class.getName());

	public final static String SQL = "javax.jdo.query.SQL";

	private static PersistenciaAlohAndes instance;

	private PersistenceManagerFactory pmf;

	private List<String> tablas;

	private SQLUtil sqlUtil;

	private SQLApartamento sqlApartamento;

	private SQLHabitacion sqlHabitacion;

	private SQLHabitacionHostal sqlHabitacionHostal;

	private SQLHabitacionHotel sqlHabitacionHotel;

	private SQLHabitacionVivienda sqlHabitacionVivienda;

	private SQLHorario sqlHorario;

	private SQLInmueble sqlInmueble;

	private SQLOfreceServicio sqlOfreceServicio;

	private SQLOperador sqlOperador;

	private SQLPersonaJuridica sqlPersonaJuridica;

	private SQLPersonaNatural sqlPersonaNatural;

	private SQLReserva sqlReserva;

	private SQLServicioMenaje sqlServicioMenaje;

	private SQLUsuario sqlUsuario;

	private SQLVivienda sqlVivienda;

	private PersistenciaAlohAndes() {
		pmf = JDOHelper.getPersistenceManagerFactory("AlohAndes");
		crearClasesSQL();
		tablas = new LinkedList<String>();
		tablas.add("INMUEBLE_SEQUENCE");
		tablas.add("OPERADOR_SEQUENCE");
		tablas.add("RESERVA_SEQUENCE");
		tablas.add("USUARIO_SEQUENCE");
		tablas.add("APARTAMENTO");
		tablas.add("HABITACION");
		tablas.add("HABITACIONHOSTAL");
		tablas.add("HABITACIONHOTEL");
		tablas.add("HABITACIONVIVIENDA");
		tablas.add("HORARIO");
		tablas.add("INMUEBLE");
		tablas.add("OFRECESERVICIO");
		tablas.add("OPERADOR");
		tablas.add("PERSONAJURIDICA");
		tablas.add("PERSONANATURAL");
		tablas.add("RESERVA");
		tablas.add("SERVICIOMENAJE");
		tablas.add("USUARIO");
		tablas.add("VIVIENDA");
	}

	private PersistenciaAlohAndes(JsonObject tableConfig) {
		crearClasesSQL();
		tablas = leerNombresTablas(tableConfig);

		String unidadPersistencia = tableConfig.get("unidadPersistencia").getAsString();

		log.trace("Accediendo unidad de persistencia: " + unidadPersistencia);

		pmf = JDOHelper.getPersistenceManagerFactory(unidadPersistencia);

	}

	private List<String> leerNombresTablas(JsonObject tableConfig) {
		JsonArray nombres = tableConfig.getAsJsonArray("tablas");
		List<String> resp = new LinkedList<String>();
		for (JsonElement nom : nombres) {
			resp.add(nom.getAsString());
		}
		return resp;
	}

	public static PersistenciaAlohAndes getInstance() {
		if (instance == null) {
			instance = new PersistenciaAlohAndes();
		}
		return instance;
	}

	public static PersistenciaAlohAndes getInstance(JsonObject tableConfig) {
		if (instance == null) {
			instance = new PersistenciaAlohAndes(tableConfig);
		}
		return instance;
	}

	public void cerrarUnidadPersistencia() {
		pmf.close();
		instance = null;
	}

	private void crearClasesSQL() {
		sqlApartamento = new SQLApartamento(this);
		sqlHabitacion = new SQLHabitacion(this);
		sqlHabitacionHostal = new SQLHabitacionHostal(this);
		sqlHabitacionHotel = new SQLHabitacionHotel(this);
		sqlHabitacionVivienda = new SQLHabitacionVivienda(this);
		sqlHorario = new SQLHorario(this);
		sqlInmueble = new SQLInmueble(this);
		sqlOfreceServicio = new SQLOfreceServicio(this);
		sqlOperador = new SQLOperador(this);
		sqlPersonaJuridica = new SQLPersonaJuridica(this);
		sqlPersonaNatural = new SQLPersonaNatural(this);
		sqlReserva = new SQLReserva(this);
		sqlServicioMenaje = new SQLServicioMenaje(this);
		sqlUsuario = new SQLUsuario(this);
		sqlVivienda = new SQLVivienda(this);
		sqlUtil = new SQLUtil(this);
	}

	public String darSeqInmueble() {
		return tablas.get(0);
	}

	public String darSeqOperador() {
		return tablas.get(1);
	}

	public String darSeqReserva() {
		return tablas.get(2);
	}

	public String darSeqUsuario() {
		return tablas.get(3);
	}


	public String darTablaApartamento() {
		return tablas.get(4);
	}

	public String darTablaHabitacion() {
		return tablas.get(5);
	}

	public String darTablaHabitacionHostal() {
		return tablas.get(6);
	}

	public String darTablaHabitacionHotel() {
		return tablas.get(7);
	}

	public String darTablaHabitacionVivienda() {
		return tablas.get(8);
	}

	public String darTablaHorario() {
		return tablas.get(9);
	}

	public String darTablaInmueble() {
		return tablas.get(10);
	}

	public String darTablaOfreceServicio() {
		return tablas.get(11);
	}

	public String darTablaOperador() {
		return tablas.get(12);
	}

	public String darTablaPersonaJuridica() {
		return tablas.get(13);
	}

	public String darTablaPersonaNatural() {
		return tablas.get(14);
	}

	public String darTablaReserva() {
		return tablas.get(15);
	}

	public String darTablaServicioMenaje() {
		return tablas.get(16);
	}

	public String darTablaUsuario() {
		return tablas.get(17);
	}

	public String darTablaVivienda() {
		return tablas.get(18);
	}

	private long nextvalInmueble() {
		long resp = sqlUtil.nextvalInmueble(pmf.getPersistenceManager());
		log.trace("Generando secuencia: " + resp);
		return resp;
	}

	private long nextvalOperador() {
		long resp = sqlUtil.nextvalOperador(pmf.getPersistenceManager());
		log.trace("Generando secuencia: " + resp);
		return resp;
	}

	private long nextvalReserva() {
		long resp = sqlUtil.nextvalReserva(pmf.getPersistenceManager());
		log.trace("Generando secuencia: " + resp);
		return resp;
	}

	private long nextvalUsuario() {
		long resp = sqlUtil.nextvalUsuario(pmf.getPersistenceManager());
		log.trace("Generando secuencia: " + resp);
		return resp;
	}



	private String darDetalleException(Exception e) {
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException")) {
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions()[0].getMessage();
		}
		return resp;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los APARTAMENTOS
	 *****************************************************************/

	public Apartamento adicionarApartamento(int amoblado, double precioMes, long idPersona, String direccion,
			int capacidad, int disponible, Date fechaReservaFinal) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idInmueble = nextvalInmueble();
			long tuplaInmuble = sqlInmueble.adicionarInmueble(pm, idInmueble, direccion, Inmueble.TIPO_APARTAMENTO,
					capacidad, disponible, fechaReservaFinal);
			long tuplasInsertadas = sqlApartamento.adicionarApartamento(pm, idInmueble, amoblado, precioMes, idPersona);
			tx.commit();
			log.trace("Inserción de Apartamento: " + idInmueble + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuples inmueble " + tuplaInmuble);
			return new Apartamento(idInmueble, amoblado, precioMes, idPersona);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Apartamento> darApartamentos() {
		return sqlApartamento.darApartamentos(pmf.getPersistenceManager());
	}

	public Apartamento darApartamentoPorId(long idApartamento) {
		return sqlApartamento.darApartamentoPorId(pmf.getPersistenceManager(), idApartamento);
	}

	public List<Apartamento> darApartamentosPorIdPersona(long idPersona) {
		return sqlApartamento.darApartamentosPorIdPersona(pmf.getPersistenceManager(), idPersona);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACION
	 *****************************************************************/

	public Habitacion adicionarHabitacion(double tamanho, double precioMes, long idPersona, String direccion,
			int capacidad, int disponible, Date fechaReservaFinal) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idInmueble = nextvalInmueble();
			long tuplaInmuble = sqlInmueble.adicionarInmueble(pm, idInmueble, direccion, Inmueble.TIPO_HABITACION,
					capacidad, disponible, fechaReservaFinal);
			long tuplasInsertadas = sqlHabitacion.adicionarHabitacion(pm, idInmueble, tamanho, precioMes, idPersona);
			tx.commit();
			log.trace("Inserción de Habitacion: " + idInmueble + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuples inmueble " + tuplaInmuble);
			return new Habitacion(idInmueble, tamanho, precioMes, idPersona);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Habitacion> darHabitaciones() {
		return sqlHabitacion.darHabitaciones(pmf.getPersistenceManager());
	}

	public Habitacion darHabitacionPorId(long idHabitacion) {
		return sqlHabitacion.darHabitacionPorId(pmf.getPersistenceManager(), idHabitacion);
	}

	public List<Habitacion> darHabitacionesPorIdPersona(long idPersona) {
		return sqlHabitacion.darHabitacionesPorIdPersona(pmf.getPersistenceManager(), idPersona);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACION HOSTAL
	 *****************************************************************/

	public HabitacionHostal adicionarHabitacionHostal(int numero, long idHostal, String direccion, int capacidad,
			int disponible, Date fechaReservaFinal) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idInmueble = nextvalInmueble();
			long tuplaInmuble = sqlInmueble.adicionarInmueble(pm, idInmueble, direccion, Inmueble.TIPO_HABITACIONHOSTAL,
					capacidad, disponible, fechaReservaFinal);
			long tuplasInsertadas = sqlHabitacionHostal.adicionarHabitacionHostal(pm, idInmueble, numero, idHostal);
			tx.commit();
			log.trace("Inserción de Habitacion Hostal: " + idInmueble + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuples inmueble " + tuplaInmuble);
			return new HabitacionHostal(idInmueble, idHostal, numero);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<HabitacionHostal> darHabitacionesHostales() {
		return sqlHabitacionHostal.darHabitacionesHostales(pmf.getPersistenceManager());
	}

	public HabitacionHostal darHabitacionHostalPorId(long idHabitacionHostal) {
		return sqlHabitacionHostal.darHabitacionHostalPorId(pmf.getPersistenceManager(), idHabitacionHostal);
	}

	public List<HabitacionHostal> darHabitacionesHostal(long idHostal) {
		return sqlHabitacionHostal.darHabitacionesHostal(pmf.getPersistenceManager(), idHostal);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACION HOTEL
	 *****************************************************************/

	public HabitacionHotel adicionarHabitacionHotel(long idHotel, int numero, String tipo, double precioNoche,
			double tamanho, String ubicacion, String direccion, int capacidad, int disponible, Date fechaReservaFinal) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idInmueble = nextvalInmueble();
			long tuplaInmuble = sqlInmueble.adicionarInmueble(pm, idInmueble, direccion, Inmueble.TIPO_HABITACIONHOTEL,
					capacidad, disponible, fechaReservaFinal);
			long tuplasInsertadas = sqlHabitacionHotel.adicionarHabitacionHotel(pm, idInmueble, idHotel, numero, tipo,
					precioNoche, tamanho, ubicacion);
			tx.commit();
			log.trace("Inserción de Habitacion Hotel: " + idInmueble + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuples inmueble " + tuplaInmuble);
			return new HabitacionHotel(idInmueble, idHotel, numero, tipo, precioNoche, tamanho, ubicacion);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<HabitacionHotel> darHabitacionesHoteles() {
		return sqlHabitacionHotel.darHabitacionesHoteles(pmf.getPersistenceManager());
	}

	public HabitacionHotel darHabitacionHotelPorId(long idHabitacionHotel) {
		return sqlHabitacionHotel.darHabitacionHotelPorId(pmf.getPersistenceManager(), idHabitacionHotel);
	}

	public List<HabitacionHotel> darHabitacionesHotel(long idHotel) {
		return sqlHabitacionHotel.darHabitacionesHotel(pmf.getPersistenceManager(), idHotel);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACION VIVIENDA
	 *****************************************************************/

	public HabitacionVivienda adicionarHabitacionVivienda(long idVivienda, int numero, double precioSemestre,
			double precioMes, double precioNoche, String ubicacion, int individual, String direccion, int capacidad,
			int disponible, Date fechaReservaFinal) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idInmueble = nextvalInmueble();
			long tuplaInmuble = sqlInmueble.adicionarInmueble(pm, idInmueble, direccion,
					Inmueble.TIPO_HABITACIONVIVIENDA, capacidad, disponible, fechaReservaFinal);
			long tuplasInsertadas = sqlHabitacionVivienda.adicionarHabitacionVivienda(pm, idInmueble, idVivienda,
					numero, precioSemestre, precioMes, precioNoche, ubicacion, individual);
			tx.commit();
			log.trace("Inserción de Habitacion Vivienda: " + idInmueble + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuples inmueble " + tuplaInmuble);
			return new HabitacionVivienda(idInmueble, idVivienda, numero, precioSemestre, precioMes, precioNoche,
					ubicacion, individual);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<HabitacionVivienda> darHabitacionesViviendas() {
		return sqlHabitacionVivienda.darHabitacionesViviendas(pmf.getPersistenceManager());
	}

	public HabitacionVivienda darHabitacionViviendaPorId(long idHabitacionVivienda) {
		return sqlHabitacionVivienda.darHabitacionViviendaPorId(pmf.getPersistenceManager(), idHabitacionVivienda);
	}

	public List<HabitacionVivienda> darHabitacionesVivienda(long idVivienda) {
		return sqlHabitacionVivienda.darHabitacionesVivienda(pmf.getPersistenceManager(), idVivienda);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HORARIO
	 *****************************************************************/

	public Horario adicionarHorario(long idHostal, String dia, int horaAbre, int horaCierra) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long tuplasInsertadas = sqlHorario.adicionarHorario(pm, idHostal, dia, horaAbre, horaCierra);
			tx.commit();
			log.trace("Inserción de Horario: " + idHostal + " " + dia + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Horario(idHostal, dia, horaAbre, horaCierra);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarHorarioPorIdHostalYDia(long idHostal, String dia) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlHorario.eliminarHorarioPorIdHostalYDia(pm, idHostal, dia);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public Horario darHorarioPorIdHostalYDia(long idHostal, String dia) {
		return sqlHorario.darHorarioPorIdHostalYDia(pmf.getPersistenceManager(), idHostal, dia);
	}

	public List<Horario> darHorariosPorIdHostal(long idHostal) {
		return sqlHorario.darHorariosPorIdHostal(pmf.getPersistenceManager(), idHostal);
	}

	public List<Horario> darHorarios() {
		return sqlHorario.darHorarios(pmf.getPersistenceManager());
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los Inmueble
	 *****************************************************************/
	public long eliminarInmueblePorId(long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlInmueble.eliminarInmueblePorId(pm, idInmueble);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long retirarOfertaInmueblePorId(long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlInmueble.retirarOfertaInmueblePorId(pm, idInmueble);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long habilitarOfertaInmueblePorId(long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlInmueble.habilitarOfertaInmueblePorId(pm, idInmueble);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public Inmueble darInmueblePorId(long idInmueble) {
		return sqlInmueble.darInmueblePorId(pmf.getPersistenceManager(), idInmueble);
	}

	public List<Inmueble> darInmuebles() {
		return sqlInmueble.darInmuebles(pmf.getPersistenceManager());
	}

	public List<Inmueble> darInmueblesPorMayorCapacidad(int capacidad) {
		return sqlInmueble.darInmueblesPorMayorCapacidad(pmf.getPersistenceManager(), capacidad);
	}

	public List<Inmueble> darInmueblesPorTipo(String tipo) {
		return sqlInmueble.darInmueblesPorTipo(pmf.getPersistenceManager(), tipo);
	}

	public List<Inmueble> darInmueblesPorDisponibilidad(int disponibilidad) {
		return sqlInmueble.darInmueblesPorDisponibilidad(pmf.getPersistenceManager(), disponibilidad);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los OfreceServicio
	 *****************************************************************/

	public OfreceServicio adicionarOfreceServicio(String idServicioMenaje, long idInmueble, Double costo,
			Integer cantidad) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long tuplasInsertadas = sqlOfreceServicio.adicionarOfreceServicio(pm, idServicioMenaje, idInmueble, costo,
					cantidad);
			tx.commit();
			log.trace("Inserción de Ofrece Servicio: " + idServicioMenaje + " " + idInmueble + ": " + tuplasInsertadas
					+ " tuplas insertadas");
			return new OfreceServicio(idServicioMenaje, idInmueble, costo, cantidad);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarOfreceServicio(String idServicioMenaje, long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlOfreceServicio.eliminarOfreceServicio(pm, idServicioMenaje, idInmueble);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public OfreceServicio darOfreceServicio(String idServicioMenaje, long idInmueble) {
		return sqlOfreceServicio.darOfreceServicio(pmf.getPersistenceManager(), idServicioMenaje, idInmueble);
	}

	public List<OfreceServicio> darOfrecenServicios() {
		return sqlOfreceServicio.darOfrecenServicios(pmf.getPersistenceManager());
	}

	public List<OfreceServicio> darOfreceServicios(long idInmueble) {
		return sqlOfreceServicio.darOfreceServicios(pmf.getPersistenceManager(), idInmueble);
	}

	public List<OfreceServicio> darOfrecenServicio(String idServicioMenaje) {
		return sqlOfreceServicio.darOfrecenServicio(pmf.getPersistenceManager(), idServicioMenaje);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los Operador
	 *****************************************************************/

	public long eliminarOperadorPorId(long idOperador) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlOperador.eliminarOperadorPorId(pm, idOperador);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public Operador darOperadorPorId(long idOperador) {
		return sqlOperador.darOperadorPorId(pmf.getPersistenceManager(), idOperador);
	}

	public List<Operador> darOperadores() {
		return sqlOperador.darOperadores(pmf.getPersistenceManager());
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Personas Juridicas
	 *****************************************************************/

	public PersonaJuridica adicionarPersonaJuridica(long idSuperIntendenciaTurismo, long idCamaraComercio,
			Integer categoria, Double precioNoche, String tipo, String nombre, String email, String telefono) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idOperador = nextvalOperador();
			long tuplaOperador = sqlOperador.adicionarOperador(pm, idOperador, nombre, email, telefono);
			long tuplasInsertadas = sqlPersonaJuridica.adicionarPersonaJuridica(pm, idOperador,
					idSuperIntendenciaTurismo, idCamaraComercio, categoria, precioNoche, tipo);
			tx.commit();
			log.trace("Inserción de Persona Juridica: " + idOperador + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuplas operador " + tuplaOperador);
			return new PersonaJuridica(idOperador, idSuperIntendenciaTurismo, idCamaraComercio, categoria, precioNoche,
					tipo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<PersonaJuridica> darPersonasJuridicas() {
		return sqlPersonaJuridica.darPersonasJuridicas(pmf.getPersistenceManager());
	}

	public PersonaJuridica darPersonaJuridicaPorId(long idPersonaJuridica) {
		return sqlPersonaJuridica.darPersonaJuridicaPorId(pmf.getPersistenceManager(), idPersonaJuridica);
	}

	public PersonaJuridica darPersonaJuridicaPorIdSuperIntendenciaTurismo(long idSuperIntendenciaTurismo) {
		return sqlPersonaJuridica.darPersonaJuridicaPorIdSuperIntendenciaTurismo(pmf.getPersistenceManager(),
				idSuperIntendenciaTurismo);
	}

	public PersonaJuridica darPersonaJuridicaPorIdCamaraComercio(long idCamaraComercio) {
		return sqlPersonaJuridica.darPersonaJuridicaPorIdCamaraComercio(pmf.getPersistenceManager(), idCamaraComercio);
	}

	public List<PersonaJuridica> darPersonaJuridicasPorTipo(String tipo) {
		return sqlPersonaJuridica.darPersonaJuridicaPorTipo(pmf.getPersistenceManager(), tipo);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Personas Naturales
	 *****************************************************************/

	public PersonaNatural adicionarPersonaNatural(String tipo, String nombre, String email, String telefono) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idOperador = nextvalOperador();
			long tuplaOperador = sqlOperador.adicionarOperador(pm, idOperador, nombre, email, telefono);
			long tuplasInsertadas = sqlPersonaNatural.adicionarPersonaNatural(pm, idOperador, tipo);
			tx.commit();
			log.trace("Inserción de Persona Natural: " + idOperador + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuplas operador " + tuplaOperador);
			return new PersonaNatural(idOperador, tipo);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<PersonaNatural> darPersonasNaturales() {
		return sqlPersonaNatural.darPersonasNaturales(pmf.getPersistenceManager());
	}

	public PersonaNatural darPersonaNaturalPorId(long idPersonaNatural) {
		return sqlPersonaNatural.darPersonaNaturalPorId(pmf.getPersistenceManager(), idPersonaNatural);
	}

	public List<PersonaNatural> darPersonasNaturalesPorTipo(String tipo) {
		return sqlPersonaNatural.darPersonasNaturalesPorTipo(pmf.getPersistenceManager(), tipo);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Reserva
	 *****************************************************************/

	public Reserva adicionarReserva(Date fechaInicio, Date fechaFin, int capacidad, long idUsuario, long idInmueble) throws Exception  {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idReserva = nextvalReserva();
			long diffDays = ChronoUnit.DAYS.between(fechaInicio.toInstant(), fechaFin.toInstant());
			Inmueble inm = darInmueblePorId(idInmueble);
			double costo=calcularCostoReserva(diffDays, inm.getTipo(), inm);
			Date fechaCancelar=darFechaDeCancelacion(inm.getTipo(), fechaInicio, diffDays);
			int pagado=0;
			double descuento=0;
			long idDueno= darDuenoInmueble(inm.getId(), inm.getTipo());
			int estado = Reserva.ESTADO_SOLICITADO;
			

			
			
			if (inm.getCapacidad()<capacidad){
				throw new Exception("La capacidad ingresada supera la capacidad del inmueble");
			}
			
			if (inm.getDisponible()==0) {
				throw new Exception("El inmueble no esta disponible para reservas");
			}
			String tipo= inm.getTipo();
			if (tipo.equals(Inmueble.TIPO_HABITACION) && diffDays<30 ) {
				throw new Exception("Una habitacion tiene una reserva minima de un mes");
			}
			
			Usuario us= darUsuarioPorId(idUsuario);
			if (us.getTipo().equals(Usuario.TIPO_INVITADO) && !tipo.equals(Inmueble.TIPO_VIVIENDA)) {
				throw new Exception("El usuario de tipo invitado solo puede arrendar vivienda");
			}
			
			if (inm.getTipo().equals(Inmueble.TIPO_APARTAMENTO) && diffDays<30) {
				throw new Exception ("El apartamento tiene reserva minima de un mes");
			}
			
			if (tipo.equals(Inmueble.TIPO_VIVIENDA)) {
				Vivienda viv= darViviendaPorId(idInmueble);
				if (viv.getDiasUtilizado()+diffDays>30 ) {
					throw new Exception("Con las fechas dadas la vivienda seria utilizada mas de 30 dias en el año");
				}
			}
			if (tipo.equals(Inmueble.TIPO_HABITACIONVIVIENDA) && !(us.getTipo().equals(Usuario.TIPO_ESTUDIANTE) || us.getTipo().equals(Usuario.TIPO_PROFESOR) || us.getTipo().equals(Usuario.TIPO_EMPLEADO)  || us.getTipo().equals(Usuario.TIPO_PROFESORINVITADO) )) {
				throw new Exception("La habitacion vivienda solo puede ser usada por estudiantes, profesores, empleados");
			}
			
			List<Reserva> reservasUs= darReservasNoCanceladasEnFechasPorIdUsuario(us.getId(), fechaInicio, fechaFin);
			
			if (reservasUs != null && reservasUs.size() != 0 ) {
				throw new Exception("El usuario ya tiene reservas para esas fechas");
			}
			
			List<Reserva> reservasIn = darReservasNoCanceladasEnFechasParaInmueble(fechaInicio, fechaFin, idInmueble);
			
			if (reservasIn != null && reservasIn.size() != 0 ) {
				throw new Exception("El inmueble ya se encuentra reservado en esas fechas");
			}
			
			long tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva, fechaInicio, fechaFin, costo,
					fechaCancelar, pagado, descuento, capacidad, estado, idDueno, idUsuario, idInmueble);
			
			Reserva re= new Reserva(idReserva,fechaInicio, fechaFin, costo,
					fechaCancelar, pagado, descuento, capacidad, estado, idDueno, idUsuario, idInmueble);
			if (re!=null) {
		    	if (inm.getTipo().equals(Inmueble.TIPO_VIVIENDA)) {
						Vivienda viv= darViviendaPorId(inm.getId());
						int diasNuevo= viv.getDiasUtilizado()+ (int)diffDays;
						actualizarViviendaDiasUtilizado(diasNuevo,inm.getId());
					}
		        }
			tx.commit();
			log.trace("Inserción de Reserva: " + idReserva + ": " + tuplasInsertadas + " tuplas insertadas");
			
			
			return re;
		} catch (Exception e) {
			log.error("Exception esta aqui : " + e.getMessage() + "\n" + darDetalleException(e));
			throw e;
			//return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long cancelarReservaPorId(long id) throws Exception {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try {
			tx.begin();
			
			Reserva reserva = darReservaPorId(id);
			Date hoyDate = new Date();
			if( reserva == null) // Verifica que existe la reserva
				throw new Exception("No existe reserva");
			else if (reserva.getEstado()==Reserva.ESTADO_CANCELADO) // Verifica si ya fue cancelada
				throw new Exception("La reserva ya fue cancelada");
			else if (reserva.getFechaFin().compareTo(hoyDate)<0) // Verifica si ya finalizo la reserva
				throw new Exception("La reserva ya finalizo");
			double nuevoPrecio = calcularCostoCancelacion(reserva.getValorTotal(), reserva.getFechaCancelacion(), reserva.getFechaInicio());
			reserva.setEstado(Reserva.ESTADO_CANCELADO);
			reserva.setValorTotal(nuevoPrecio);
			
			
			long resp = sqlReserva.actualizarReservaPorId(pm, id, reserva);;
			tx.commit();
			log.trace("Actualizacion de reserva: "+id);
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			throw e;
			//return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarReservaPorId(long idReserva) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlReserva.eliminarReservaPorId(pm, idReserva);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public Reserva darReservaPorId(long idReserva) {
		return sqlReserva.darReservaPorId(pmf.getPersistenceManager(), idReserva);
	}

	public List<Reserva> darReservas() {
		return sqlReserva.darReservas(pmf.getPersistenceManager());
	}


	public List<Reserva> darReservasPorIdUsuario(long idUsuario) {
		return sqlReserva.darReservaPorIdUsuario(pmf.getPersistenceManager(), idUsuario);
	}

	public List<Reserva> darReservasNoCanceladasEnFechasPorIdUsuario(long idUsuario, Date fechaStart, Date fechaEnd) {
		return sqlReserva.darReservasNoCanceladasEnFechasPorIdUsuario(pmf.getPersistenceManager(), idUsuario,
				fechaStart, fechaEnd);
	}

	public List<Reserva> darReservasDespuesDeFechaPorIdInmueble(Date fecha, long idInmueble) {
		return sqlReserva.darReservasDespuesDeFechaPorIdInmueble(pmf.getPersistenceManager(), fecha, idInmueble);
	}

	public List<Reserva> darReservasEnFechasParaInmueble(Date fechaStart, Date fechaEnd, long idInmueble) {
		return sqlReserva.darReservasEnFechasParaInmueble(pmf.getPersistenceManager(), fechaStart, fechaEnd,
				idInmueble);
	}

	public List<Reserva> darReservasNoCanceladasEnFechasParaInmueble(Date fechaStart, Date fechaEnd, long idInmueble) {
		return sqlReserva.darReservasNoCanceladasEnFechasParaInmueble(pmf.getPersistenceManager(), fechaStart, fechaEnd,
				idInmueble);
	}

	public double calcularCostoReserva(long diffDays, String tipoIn, Inmueble in) {
		double precio=-1;
		if (tipoIn.equals(Inmueble.TIPO_HABITACION)) {
			//Habitacion tiene reserva minima de un mes
			double tiempo= Math.ceil(diffDays/30);
			Habitacion hab= darHabitacionPorId(in.getId());
			precio= hab.getPrecioMes()*tiempo;
		}else if (tipoIn.equals(Inmueble.TIPO_VIVIENDA)) {
			Vivienda viv= darViviendaPorId(in.getId());
			precio= viv.getCostoNoche()*diffDays;

		}else if(tipoIn.equals(Inmueble.TIPO_APARTAMENTO)) {
			Apartamento apto=darApartamentoPorId(in.getId());
			double tiempo= Math.ceil(diffDays/30);
			precio= apto.getPrecioMes()*tiempo;

		}else if (tipoIn.equals(Inmueble.TIPO_HABITACIONHOTEL)) {
			HabitacionHotel hab= darHabitacionHotelPorId(in.getId());
			precio=hab.getPrecioNoche()*diffDays;

		}else if(tipoIn.equals(Inmueble.TIPO_HABITACIONVIVIENDA)) {
			HabitacionVivienda hab= darHabitacionViviendaPorId(in.getId());

			if (diffDays<=30) {
				precio= hab.getPrecioNoche()*diffDays;
			}else if (diffDays<182.5) {
				double tiempo= Math.ceil(diffDays/30);
				precio=hab.getPrecioMes()*tiempo;
			}else {
				double tiempo= Math.ceil(diffDays/182.5);
				precio= hab.getPrecioSemestre()*tiempo;
			}
		}else if (tipoIn.equals(Inmueble.TIPO_HABITACIONHOSTAL)) {
			long dueno=darDuenoInmueble(in.getId(), tipoIn);
			PersonaJuridica per=darPersonaJuridicaPorId(dueno);
			precio=per.getPrecioNoche()*diffDays;
		}
		return precio;

	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Personas Servicio Menaje
	 *****************************************************************/

	public ServicioMenaje adicionarServicioMenaje(String nombre, String tipo) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long tuplasInsertadas = sqlServicioMenaje.adicionarServicioMenaje(pm, nombre, tipo);
			tx.commit();
			log.trace("Inserción de Servicio Menaje: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
			return new ServicioMenaje(nombre, tipo);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarServicioMenajePorNombre(String nombre) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlServicioMenaje.eliminarServicioMenajePorNombre(pm, nombre);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public ServicioMenaje darServicioMenajePorNombre(String nombre) {
		return sqlServicioMenaje.darServicioMenajePorNombre(pmf.getPersistenceManager(), nombre);
	}

	public List<ServicioMenaje> darServiciosMenajesPorTipo(String tipo) {
		return sqlServicioMenaje.darServiciosMenajesPorTipo(pmf.getPersistenceManager(), tipo);
	}

	public List<ServicioMenaje> darServiciosMenajes() {
		return sqlServicioMenaje.darServiciosMenajes(pmf.getPersistenceManager());
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Personas Usuarios
	 *****************************************************************/
	public Usuario adicionarUsuario(String nombre, String email, String telefono, String tipo) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idUsuario = nextvalUsuario();
			long tuplasInsertadas = sqlUsuario.adicionarUsuario(pm, idUsuario, nombre, email, telefono, tipo);
			tx.commit();
			log.trace("Inserción de Usuario: " + idUsuario + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Usuario(idUsuario, nombre, email, telefono, tipo);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarUsuarioPorId(long idUsuario) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlUsuario.eliminarUsuarioPorId(pmf.getPersistenceManager(), idUsuario);
			tx.commit();
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public Usuario darUsuarioPorId(long idUsuario) {
		return sqlUsuario.darUsuarioPorId(pmf.getPersistenceManager(), idUsuario);
	}

	public Usuario darUsuarioPorEmail(String email) {
		return sqlUsuario.darUsuarioPorEmail(pmf.getPersistenceManager(), email);
	}

	public List<Usuario> darUsuarios() {
		return sqlUsuario.darUsuarios(pmf.getPersistenceManager());
	}

	public List<Usuario> darUsuariosPorTipo(String tipo) {
		return sqlUsuario.darUsuariosPorTipo(pmf.getPersistenceManager(), tipo);
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Viviendas
	 *****************************************************************/

	public Vivienda adicionarVivienda(int numeroHabitaciones, double costoNoche, int diasUtilizado, long idPersona,
			String direccion, int capacidad, int disponible, Date fechaReservaFinal) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idInmueble = nextvalInmueble();
			long tuplaInmuble = sqlInmueble.adicionarInmueble(pm, idInmueble, direccion, Inmueble.TIPO_VIVIENDA,
					capacidad, disponible, fechaReservaFinal);
			long tuplasInsertadas = sqlVivienda.adicionarVivienda(pm, idInmueble, numeroHabitaciones, costoNoche,
					diasUtilizado, idPersona);
			tx.commit();
			log.trace("Inserción de Vivienda: " + idInmueble + ": " + tuplasInsertadas + " tuplas insertadas"
					+ " tuples inmueble " + tuplaInmuble);
			return new Vivienda(idInmueble, numeroHabitaciones, costoNoche, diasUtilizado, idPersona);
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Vivienda> darViviendas() {
		return sqlVivienda.darViviendas(pmf.getPersistenceManager());
	}

	public Vivienda darViviendaPorId(long idVivienda) {
		return sqlVivienda.darViviendaPorId(pmf.getPersistenceManager(), idVivienda);
	}

	public List<Vivienda> darViviendasPorIdPersona(long idPersona) {
		return sqlVivienda.darViviendasPorIdPersona(pmf.getPersistenceManager(), idPersona);
	}

	public long actualizarViviendaDiasUtilizado(int diasUtilizado, long idVivienda) {
		return sqlVivienda.actualizarViviendaDiasUtilizado(pmf.getPersistenceManager(), idVivienda, diasUtilizado);
	}

	/*
	 * **************************************************************** Métodos para
	 * requerimientos de consulta
	 *****************************************************************/

	public List<ReqConsulta1> RFC1() {
		return sqlUtil.RFC1(pmf.getPersistenceManager());
	}

	public List<ReqConsulta2> RFC2() {
		return sqlUtil.RFC2(pmf.getPersistenceManager());
	}

	public List<ReqConsulta3> RFC3() {
		return sqlUtil.RFC3(pmf.getPersistenceManager());
	}

	public List<ReqConsulta4> RFC4(Date X, Date Y, List<String> servicios) {
		return sqlUtil.RFC4(pmf.getPersistenceManager(), X, Y, servicios);
	}

	
	public List<Inmueble> RF7(Date X, Date Y, List<String> servicios, String tipoInmueble, int cantidad, int capacidadPor,long  idUsuario) throws Exception {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			List<ReqConsulta4> lista=sqlUtil.RFC4(pmf.getPersistenceManager(), X, Y, servicios);
			List<Inmueble> tipoCorrecto= new ArrayList<Inmueble>();
			for (int i=0; i<lista.size();i++) {
				long idActual= lista.get(i).getIdInmueble();
				Inmueble in= darInmueblePorId(idActual);
				if (in.getTipo().equals(tipoInmueble) && in.getCapacidad()>=capacidadPor) {
					tipoCorrecto.add(in);
				}
			}
			List<Inmueble> reservadas= new ArrayList<Inmueble>();
			if (tipoCorrecto.size()<cantidad) {
				throw new Exception ("No hay suficientes "+ tipoInmueble+" para cubrir la demanda");
			}else {
				
				for (int i=0; i<cantidad;i++) {
					Inmueble actual= tipoCorrecto.get(i);
					adicionarReserva(X, Y, capacidadPor, idUsuario, actual.getId());
					reservadas.add(actual);
				}

			}
			tx.commit();
			
			log.trace("Creacion de "+cantidad+" reservas " );
			return reservadas;
		}catch(Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			throw e;
			//return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}





	public long darDuenoInmueble( long id, String tipo){

		if (tipo.equals(Inmueble.TIPO_APARTAMENTO)) {
			Apartamento inm= darApartamentoPorId(id);
			return inm.getIdPersona();
		}else if (tipo.equals(Inmueble.TIPO_HABITACION)) {
			Habitacion inm= darHabitacionPorId(id);
			return inm.getIdPersona();
		}else if (tipo.equals(Inmueble.TIPO_HABITACIONHOSTAL)) {
			HabitacionHostal inm= darHabitacionHostalPorId(id);
			return inm.getIdHostal();
		}else if (tipo.equals(Inmueble.TIPO_HABITACIONHOTEL)) {
			HabitacionHotel inm= darHabitacionHotelPorId(id);
			return inm.getIdHotel();

		}else if (tipo.equals(Inmueble.TIPO_HABITACIONVIVIENDA)) {
			HabitacionVivienda inm= darHabitacionViviendaPorId(id);
			return inm.getIdVivienda();

		}else if (tipo.equals(Inmueble.TIPO_VIVIENDA)) {
			Vivienda inm= darViviendaPorId(id);
			return inm.getIdPersona();
		}
		return -1;
	}


	public Date darFechaDeCancelacion(String tipo, Date fechaIni, long diffDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaIni);
		if (diffDays<=0) {
			return null;
		}
		else if (tipo.equals(Inmueble.TIPO_VIVIENDA) || tipo.equals(Inmueble.TIPO_HABITACIONHOTEL) || tipo.equals(Inmueble.TIPO_HABITACIONHOSTAL )){
			calendar.add(Calendar.DATE, -3);
			Date out = calendar.getTime();
			return out;
		}else if (tipo.equals(Inmueble.TIPO_HABITACION) || tipo.equals(Inmueble.TIPO_APARTAMENTO) ) {
			calendar.add(Calendar.DATE, -7);
			Date out = calendar.getTime();
			return out;
		}else if (tipo.equals(Inmueble.TIPO_HABITACIONVIVIENDA)) {
			if (diffDays<30) {
				calendar.add(Calendar.DATE, -3);
				Date out = calendar.getTime();
				return out;
			}else {
				calendar.add(Calendar.DATE, -7);
				Date out = calendar.getTime();
				return out;
			}
		}else {
			return null;
		}

	}

	public double calcularCostoCancelacion(double totalOriginal, Date fechaCancelacion, Date fechaInicio) {
		Date date = new Date();  
		if (date.compareTo(fechaCancelacion)<0) {
			return totalOriginal*0.1;
		}else if (date.compareTo(fechaInicio)<0 ) {
			return totalOriginal*0.3;
		}else {
			return totalOriginal*0.5;
		}

	}


	public long[] limpiarAlohAndes() {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long[] resp = sqlUtil.limpiarAlohAndes(pm);
			tx.commit();
			log.info("Borrada la base de datos");
			return resp;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return new long[] { -1, -1, -1, -1, -1, -1, -1 };
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}

	}
}

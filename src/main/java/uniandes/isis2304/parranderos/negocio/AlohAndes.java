/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.negocio;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.google.gson.JsonObject;

import uniandes.isis2304.parranderos.persistencia.PersistenciaAlohAndes;

/**
 * Clase principal del negocio Sarisface todos los requerimientos funcionales
 * del negocio
 *
 * @author Germán Bravo
 */
public class AlohAndes {
	/*
	 * **************************************************************** Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(AlohAndes.class.getName());

	/*
	 * **************************************************************** Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaAlohAndes pp;

	/*
	 * **************************************************************** Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public AlohAndes() {

		pp = PersistenciaAlohAndes.getInstance();
	}

	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * 
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad
	 *                    de persistencia
	 */
	public AlohAndes(JsonObject tableConfig) {
		pp = PersistenciaAlohAndes.getInstance(tableConfig);
	}

	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia() {
		pp.cerrarUnidadPersistencia();
	}

	public String aTexto(int var) {
		if (var == 1) {
			return "Si";
		} else {
			return "No";
		}
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los TIPOS DE BEBIDA
	 *****************************************************************/

	public Apartamento adicionarApartamento(int amoblado, double precioMes, long idPersona, String direccion,
			int capacidad, int disponible, Date fechaReservaFinal) throws Exception {

		log.info("Adicionando Apartamento en: " + direccion + " con capacidad: " + capacidad + ", precio mes: "
				+ precioMes + ", amoblado: " + aTexto(amoblado) + ", disponible: " + aTexto(disponible) + " y dueño: "
				+ idPersona);
		Apartamento apto = pp.adicionarApartamento(amoblado, precioMes, idPersona, direccion, capacidad, disponible,
				fechaReservaFinal);
		log.info("Adicionando Apartamento: " + apto);
		return apto;
	}

	public List<Apartamento> darApartamentos() {
		log.info("Consultando Apartamentos");
		List<Apartamento> aptos = pp.darApartamentos();
		log.info("Consultando Apartamentos: " + aptos.size() + " existentes");
		return aptos;
	}

	public Apartamento darApartamentoPorId(long id) {
		log.info("Consultando Apartamento");
		Apartamento apto = pp.darApartamentoPorId(id);
		log.info("Consultando Apartamento: " + id);
		return apto;
	}

	public List<Apartamento> darApartamentosPorIdPersona(int idPersona) {
		log.info("Consultando Apartamentos");
		List<Apartamento> aptos = pp.darApartamentosPorIdPersona(idPersona);
		log.info("Consultando " + aptos.size() + " Apartamentos de: " + idPersona);
		return aptos;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACION
	 *****************************************************************/
	public Habitacion adicionarHabitacion(double tamanho, double precioMes, long idPersona, String direccion,
			int capacidad, int disponible, Date fechaReservaFinal) throws Exception {

		log.info("Adicionando Habitacion en: " + direccion + " con capacidad: " + capacidad + ", precio mes: "
				+ precioMes + ", tamaño: " + tamanho + ", disponible: " + aTexto(disponible) + " y dueño: "
				+ idPersona);
		Habitacion habitacion = pp.adicionarHabitacion(tamanho, precioMes, idPersona, direccion, capacidad, disponible,
				fechaReservaFinal);
		log.info("Adicionando Habitacion: " + habitacion);
		return habitacion;
	}

	public List<Habitacion> darHabitaciones() {
		log.info("Consultando Habitaciones");
		List<Habitacion> hab = pp.darHabitaciones();
		log.info("Consultando Habitaciones : " + hab.size() + " existentes");
		return hab;
	}

	public Habitacion darHabitacionPorId(long id) {
		log.info("Consultando Habitacion");
		Habitacion hab = pp.darHabitacionPorId(id);
		log.info("Consultando Habitacion: " + id);
		return hab;
	}

	public List<Habitacion> darHabitacionesPorIdPersona(int idPersona) {
		log.info("Consultando Habitaciones");
		List<Habitacion> habs = pp.darHabitacionesPorIdPersona(idPersona);
		log.info("Consultando " + habs.size() + "Habitaciones de: " + idPersona);
		return habs;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACIONES HOSTAL
	 *****************************************************************/
	public HabitacionHostal adicionarHabitacionHostal(int numero, long idHostal, String direccion, int capacidad,
			int disponible, Date fechaReservaFinal) throws Exception {
		log.info("Adicionando Habitacion Hostal en: " + direccion + " con capacidad: " + capacidad + ", numero: "
				+ numero + ", disponible: " + aTexto(disponible) + " y del hostal: " + idHostal);
		HabitacionHostal habitacion = pp.adicionarHabitacionHostal(numero, idHostal, direccion, capacidad, disponible,
				fechaReservaFinal);
		log.info("Adicionando Habitacion: " + habitacion);
		return habitacion;
	}

	public List<HabitacionHostal> darHabitacionesHostales() {
		log.info("Consultando Habitaciones Hostal");
		List<HabitacionHostal> hab = pp.darHabitacionesHostales();
		log.info("Consultando Habitaciones : " + hab.size() + " existentes");
		return hab;
	}

	public HabitacionHostal darHabitacionHostalPorId(long id) {
		log.info("Consultando Habitacion Hostal");
		HabitacionHostal hab = pp.darHabitacionHostalPorId(id);
		log.info("Consultando Habitacion: " + id);
		return hab;
	}

	public List<HabitacionHostal> darHabitacionesHostal(int idHostal) {
		log.info("Consultando Habitaciones Hostal");
		List<HabitacionHostal> habs = pp.darHabitacionesHostal(idHostal);
		log.info("Consultando " + habs.size() + "Habitaciones de: " + idHostal);
		return habs;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACION HOTEL
	 *****************************************************************/
	public HabitacionHotel adicionarHabitacionHotel(long idHotel, int numero, String tipo, double precioNoche,
			double tamanho, String ubicacion, String direccion, int capacidad, int disponible, Date fechaReservaFinal)
			throws Exception {
		log.info("Adicionando Habitacion Hotel en: " + direccion + " con capacidad: " + capacidad + ", numero: "
				+ numero + ", disponible: " + aTexto(disponible) + ", tipo: " + tipo + ", tamanho: " + tamanho
				+ " y del hotel: " + idHotel);
		HabitacionHotel habitacion = pp.adicionarHabitacionHotel(idHotel, numero, tipo, precioNoche, tamanho, ubicacion,
				direccion, capacidad, disponible, fechaReservaFinal);
		log.info("Adicionando Habitacion Hotel: " + habitacion);
		return habitacion;
	}

	public List<HabitacionHotel> darHabitacionesHoteles() {
		log.info("Consultando Habitaciones Hoteles");
		List<HabitacionHotel> hab = pp.darHabitacionesHoteles();
		log.info("Consultando Habitaciones : " + hab.size() + " existentes");
		return hab;
	}

	public HabitacionHotel darHabitacionHotelPorId(long id) {
		log.info("Consultando Habitacion Hotel");
		HabitacionHotel hab = pp.darHabitacionHotelPorId(id);
		log.info("Consultando Habitacion: " + id);
		return hab;
	}

	public List<HabitacionHotel> darHabitacionesHotel(int idHotel) {
		log.info("Consultando Habitaciones Hotel");
		List<HabitacionHotel> habs = pp.darHabitacionesHotel(idHotel);
		log.info("Consultando " + habs.size() + "Habitaciones de: " + idHotel);
		return habs;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HABITACION VIVIENDA
	 *****************************************************************/
	public HabitacionVivienda adicionarHabitacionVivienda(long idVivienda, int numero, double precioSemestre,
			double precioMes, double precioNoche, String ubicacion, int individual, String direccion, int capacidad,
			int disponible, Date fechaReservaFinal) throws Exception {
		log.info("Adicionando Habitacion Vivienda en: " + direccion + " con capacidad: " + capacidad + ", numero: "
				+ numero + ", disponible: " + aTexto(disponible) + ", precio Mes: " + precioMes + ", precio semestre: "
				+ precioSemestre + ", precio noche: " + precioNoche + ", individual: " + aTexto(individual)
				+ " y de la vivienda: " + idVivienda);
		HabitacionVivienda habitacion = pp.adicionarHabitacionVivienda(idVivienda, numero, precioSemestre, precioMes,
				precioNoche, ubicacion, individual, direccion, capacidad, disponible, fechaReservaFinal);
		log.info("Adicionando Habitacion Vivienda: " + habitacion);
		return habitacion;
	}

	public List<HabitacionVivienda> darHabitacionesViviendas() {
		log.info("Consultando Habitaciones Vivienda");
		List<HabitacionVivienda> hab = pp.darHabitacionesViviendas();
		log.info("Consultando Habitaciones Vivienda : " + hab.size() + " existentes");
		return hab;
	}

	public HabitacionVivienda darHabitacionViviendaPorId(long id) {
		log.info("Consultando Habitacion Vivienda");
		HabitacionVivienda hab = pp.darHabitacionViviendaPorId(id);
		log.info("Consultando Habitacion Vivienda: " + id);
		return hab;
	}

	public List<HabitacionVivienda> darHabitacionesVivienda(long idVivienda) {
		log.info("Consultando Habitaciones Vivienda");
		List<HabitacionVivienda> habs = pp.darHabitacionesVivienda(idVivienda);
		log.info("Consultando " + habs.size() + "Habitaciones de: " + idVivienda);
		return habs;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los HORARIO
	 *****************************************************************/
	public Horario adicionarHorario(long idHostal, String dia, int horaAbre, int horaCierra) throws Exception {
		log.info("Adicionando Horario en el dia: " + dia + " con hora abre: " + horaAbre + ", horaCierra: " + horaCierra
				+ " y del hostal: " + idHostal);
		Horario horario = pp.adicionarHorario(idHostal, dia, horaAbre, horaCierra);
		log.info("Adicionando Horario: " + horario);
		return horario;
	}

	public long eliminarHorarioPorIdHostalYDia(long idHostal, String dia) {
		log.info("Borrando el Horario del dia: " + dia + " del hostal: " + idHostal);
		long horario = pp.eliminarHorarioPorIdHostalYDia(idHostal, dia);
		log.info("eliminando el horario");
		return horario;
	}

	public Horario darHorarioPorIdHostalYDia(long idHostal, String dia) {
		log.info("Consultando Horario");
		Horario ho = pp.darHorarioPorIdHostalYDia(idHostal, dia);
		log.info("Consultando Horario del dia: " + dia + " del hostal: " + idHostal);
		return ho;
	}

	public List<Horario> darHorarios() {
		log.info("Consultando Horarios");
		List<Horario> ho = pp.darHorarios();
		log.info("Consultando Horarios : " + ho.size() + " existentes");
		return ho;
	}

	public List<Horario> darHorariosPorIdHostal(long idHostal) {
		log.info("Consultando Horarios");
		List<Horario> ho = pp.darHorariosPorIdHostal(idHostal);
		log.info("Consultando " + ho.size() + "Horarios de: " + idHostal);
		return ho;
	}

	/******************************************************************
	 * Métodos para manejar los Inmueble
	 *****************************************************************/
	public long eliminarInmueblePorId(long idInmueble) {
		log.info("Eliminando el Inmueble: " + idInmueble);
		long inm = pp.eliminarInmueblePorId(idInmueble);
		log.info("eliminando el inmueble");
		return inm;
	}

	public Inmueble darInmueblePorId(long idInmueble) {
		log.info("Consultando Inmueble");
		Inmueble inm = pp.darInmueblePorId(idInmueble);
		log.info("Consultando Imnueble: " + idInmueble);
		return inm;
	}

	public List<Inmueble> darInmuebles() {
		log.info("Consultando Inmuebles");
		List<Inmueble> inm = pp.darInmuebles();
		log.info("Consultando Inmuebles: " + inm.size() + " existentes");
		return inm;
	}

	public List<Inmueble> darInmueblesPorMayorCapacidad(int capacidad) {
		log.info("Consultando Inmuebles por capacidad");
		List<Inmueble> inm = pp.darInmueblesPorMayorCapacidad(capacidad);
		log.info("Consultando Inmuebles: " + inm.size() + " existentes");
		return inm;
	}

	public List<Inmueble> darInmueblesPorTipo(String tipo) {
		log.info("Consultando Inmuebles por tipo");
		List<Inmueble> inm = pp.darInmueblesPorTipo(tipo);
		log.info("Consultando Inmuebles: " + inm.size() + " existentes");
		return inm;
	}

	public List<Inmueble> darInmueblesPorDisponibilidad(int disponibilidad) {
		log.info("Consultando Inmuebles por disponibilidad");
		List<Inmueble> inm = pp.darInmueblesPorDisponibilidad(disponibilidad);
		log.info("Consultando Inmuebles: " + inm.size() + " existentes");
		return inm;
	}

	public long retirarOfertaInmueblePorId(long idInmueble) throws Exception {
		log.info("Retirando oferta inmueble: " + idInmueble);
		long inm = pp.retirarOfertaInmueblePorId(idInmueble);
		log.info("Oferta retirada");
		return inm;
	}
	public List<ReqFun9> deshabilitarOfertaAlojamiento(long idInmueble) throws Exception{
		log.info("deshabilitando inmueble: " + idInmueble);
		List<ReqFun9> inm = pp.deshabilitarOfertaAlojamiento(idInmueble);
		log.info("Oferta deshabilitada");
		return inm;
	}
	public long habilitadrOfertaDeInmueble(long idInmueble) {
		log.info("habilitando inmueble: " + idInmueble);
		long inm = pp.habilitarOfertaInmueblePorId(idInmueble);
		log.info("Oferta habilitada");
		return inm;
	}

	
	/****************************************************************** 
	 * Métodos para manejar los OfreceServicio
	 *****************************************************************/
	public OfreceServicio adicionarOfreceServicio(String idServicioMenaje, long idInmueble, Double costo,
			Integer cantidad) throws Exception {
		log.info("Adicionando  servicio/menaje: " + idServicioMenaje + " al inmueble: " + idInmueble + ", con costo: "
				+ costo + " y cantidad: " + cantidad);
		OfreceServicio os = pp.adicionarOfreceServicio(idServicioMenaje, idInmueble, costo, cantidad);
		log.info("Adicionando ofrece servicio: " + os);
		return os;
	}

	public long eliminarOfreceServicio(String idServicioMenaje, long idInmueble) {
		log.info("Eliminando el servicio: " + idServicioMenaje + " ofrecido por: " + idInmueble);
		long os = pp.eliminarOfreceServicio(idServicioMenaje, idInmueble);
		log.info("eliminando el ofrece servicio");
		return os;
	}

	public OfreceServicio darOfreceServicio(String idServicioMenaje, long idInmueble) {
		log.info("Consultando servicio/menaje:" + idServicioMenaje + " ofrecido por el inmueble:" + idInmueble);
		OfreceServicio os = pp.darOfreceServicio(idServicioMenaje, idInmueble);
		log.info("Consultando ofrece servicio: " + os);
		return os;
	}

	public List<OfreceServicio> darOfrecenServicios() {
		log.info("Consultando Servicios ofrecidos");
		List<OfreceServicio> os = pp.darOfrecenServicios();
		log.info("Consultando servicios ofrecidos: " + os.size() + " existentes");
		return os;
	}

	public List<OfreceServicio> darOfreceServicios(long idInmueble) {
		log.info("Consultando Servicios ofrecidos por inmueble: " + idInmueble);
		List<OfreceServicio> os = pp.darOfreceServicios(idInmueble);
		log.info("Consultando servicios ofrecidos: " + os.size() + " existentes");
		return os;
	}

	public List<OfreceServicio> darOfrecenServicio(String idServicioMenaje) {
		log.info("Consultando inmueble que ofrecen servicio: " + idServicioMenaje);
		List<OfreceServicio> os = pp.darOfrecenServicio(idServicioMenaje);
		log.info("Consultando servicios ofrecidos: " + os.size() + " existentes");
		return os;
	}

	/****************************************************************** 
	 * Métodos para manejar los Operador
	 *****************************************************************/
	public long eliminarOperadorPorId(long idOperador) {
		log.info("Eliminando el operador: " + idOperador);
		long op = pp.eliminarOperadorPorId(idOperador);
		log.info("eliminando el operador" + op);
		return op;
	}

	public List<Operador> darOperadores() {
		log.info("Consultando Operadores");
		List<Operador> hab = pp.darOperadores();
		log.info("Consultando Operadores: " + hab.size() + " existentes");
		return hab;
	}

	public Operador darOperadorPorId(long id) {
		log.info("Consultando operador con id:" + id);
		Operador op = pp.darOperadorPorId(id);
		log.info("Consultando Operador: " + op);
		return op;
	}

	/****************************************************************** 
	 * Métodos para manejar las Personas Juridicas
	 *****************************************************************/
	public PersonaJuridica adicionarPersonaJuridica(long idSuperIntendenciaTurismo, long idCamaraComercio,
			Integer categoria, Double precioNoche, String tipo, String nombre, String email, String telefono)
			throws Exception {
		log.info("Adicionando Persona Juridica con id de superIntendencia" + idSuperIntendenciaTurismo
				+ ", id camara comercio: " + idCamaraComercio + " y tipo: " + tipo);
		verificarConsistenciaPersonaJuridica(tipo, categoria, precioNoche);
		PersonaJuridica pj = pp.adicionarPersonaJuridica(idSuperIntendenciaTurismo, idCamaraComercio, categoria,
				precioNoche, tipo, nombre, email, telefono);
		log.info("Adicionando persona juridica: " + pj);
		return pj;
	}

	public List<PersonaJuridica> darPersonasJuridicas() {
		log.info("Consultando Personas Juridicas");
		List<PersonaJuridica> pj = pp.darPersonasJuridicas();
		log.info("Consultando Personas Juridicas: " + pj.size() + " existentes");
		return pj;
	}

	public PersonaJuridica darPersonaJuridicaPorId(long id) {
		log.info("Consultando Persona Juridica con id:" + id);
		PersonaJuridica pj = pp.darPersonaJuridicaPorId(id);
		log.info("Consultando Persona Juridica: " + pj);
		return pj;
	}

	public PersonaJuridica darPersonaJuridicaPorIdSuperIntendenciaTurismo(long idSuperIntendenciaTurismo) {
		log.info("Consultando Persona Juridica con id de superintendencia:" + idSuperIntendenciaTurismo);
		PersonaJuridica pj = pp.darPersonaJuridicaPorIdSuperIntendenciaTurismo(idSuperIntendenciaTurismo);
		log.info("Consultando Persona Juridica: " + pj);
		return pj;
	}

	public PersonaJuridica darPersonaJuridicaPorIdCamaraComercio(long idCamaraComercio) {
		log.info("Consultando Persona Juridica con id de camara de comercio:" + idCamaraComercio);
		PersonaJuridica pj = pp.darPersonaJuridicaPorIdCamaraComercio(idCamaraComercio);
		log.info("Consultando Persona Juridica: " + pj);
		return pj;
	}

	public List<PersonaJuridica> darPersonaJuridicaPorTipo(String tipo) {
		log.info("Consultando Persona Juridica con tipo:" + tipo);
		List<PersonaJuridica> pj = pp.darPersonaJuridicasPorTipo(tipo);
		log.info("Consultando Persona Juridica: " + pj);
		return pj;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Personas Naturales
	 *****************************************************************/
	public PersonaNatural adicionarPersonaNatural(String tipo, String nombre, String email, String telefono) {

		log.info("Adicionando Persona Natural con nombre" + nombre + ", email: " + email + ", telefono:" + telefono
				+ " y tipo: " + tipo);
		PersonaNatural pn = pp.adicionarPersonaNatural(tipo, nombre, email, telefono);
		log.info("Adicionando persona natural: " + pn);
		return pn;
	}

	public List<PersonaNatural> darPersonasNaturales() {
		log.info("Consultando Personas Naturales");
		List<PersonaNatural> pn = pp.darPersonasNaturales();
		log.info("Consultando Personas Natural: " + pn.size() + " existentes");
		return pn;
	}

	public PersonaNatural darPersonaNaturalPorId(long id) {
		log.info("Consultando Persona Natural con id:" + id);
		PersonaNatural pn = pp.darPersonaNaturalPorId(id);
		log.info("Consultando Persona Natural: " + pn);
		return pn;
	}

	public List<PersonaNatural> darPersonaNaturalPorTipo(String tipo) {
		log.info("Consultando Personas Naturales con tipo:" + tipo);
		List<PersonaNatural> pn = pp.darPersonasNaturalesPorTipo(tipo);
		log.info("Consultando Persona Natural: " + pn);
		return pn;
	}

	/***************************************************************** 
	 * Métodos para manejar las Reserva
	 *****************************************************************/
	
	public List<Reserva> adicionarReservaColectiva(Date fechaInicio, Date fechaFin, List<String>servicios, String tipo, int cantidad, int capacidad, long idUsuario) throws Exception{
		log.info("Adicionando reserva colectiva con fecha inicio" + fechaInicio + ", fecha fin: " + fechaFin + ", usuario:"
				+ idUsuario + " tipo inmueble: " + tipo + " cantidad: "+cantidad);
		List<Reserva> reservas = pp.adicionarReservaColectiva(fechaInicio, fechaFin, servicios, tipo, cantidad, capacidad, idUsuario);
		log.info("Reservas adicionadas");
		return reservas;
	}

	public Reserva adicionarReserva(Date fechaInicio, Date fechaFin, int capacidad, long idUsuario, long idInmueble)
			throws Exception {
		log.info("Adicionando reserva con fecha inicio" + fechaInicio + ", fecha fin: " + fechaFin + ", usuario:"
				+ idUsuario + " y inmueble: " + idInmueble);
		Reserva re = pp.adicionarReserva(fechaInicio, fechaFin, capacidad, idUsuario, idInmueble);
		log.info("Adicionando reserva: " + re);
		return re;
	}

	public long eliminarReservaporId(long idReserva) {

		log.info("Eliminando la reserva: " + idReserva);
		long op = pp.eliminarReservaPorId(idReserva);
		log.info("eliminando el reserva" + op);
		return op;
	}

	public Reserva darReservaPorId(long id) {
		log.info("Consultando Reserva con id:" + id);
		Reserva re = pp.darReservaPorId(id);
		log.info("Consultando Reserva: " + re);
		return re;
	}

	public List<Reserva> darReservas() {
		log.info("Consultando Reservas");
		List<Reserva> pn = pp.darReservas();
		log.info("Consultando Reservas: " + pn.size() + " existentes");
		return pn;
	}

	public List<Reserva> darReservasEnFechasParaInmueble(Date fechaStart, Date fechaEnd, long idInmueble) {
		log.info("Consultando Reservas en fechas" + fechaStart + ", fecha fin " + fechaEnd + " del inmueble: "
				+ idInmueble);
		List<Reserva> pn = pp.darReservasEnFechasParaInmueble(fechaStart, fechaEnd, idInmueble);
		log.info("Consultando Reservas: " + pn.size() + " existentes");
		return pn;
	}

	public List<Reserva> darReservasDeUsuario(long pId) {
		List<Reserva> li = darReservas();
		List<Reserva> ans = darReservas();
		for (int i = 0; i < li.size(); i++) {
			if (li.get(i).getIdUsuario() == pId) {
				ans.add(li.get(i));
			}
		}
		return ans;
	}

	public List<Reserva> darReservasporIdUsuario(long idUsuario) {
		log.info("Consultando Reservas de usuario " + idUsuario);
		List<Reserva> pn = pp.darReservasPorIdUsuario(idUsuario);
		log.info("Consultando Reservas por id usuario: " + pn.size() + " existentes");
		return pn;
	}

	public long cancelarReservaPorId(long id) throws Exception {
		log.info("Cancelando reserva con id:" + id);
		long resp = pp.cancelarReservaPorId(id);
		log.info("Reserva cancelada:" + resp);
		return resp;

	}
	
	public List<Long> cancelarReservaColectivaPorId(long idReservaColectiva) throws Exception{
		log.info("Cancelando reserva colectiva con id:" + idReservaColectiva);
		List<Long> resp = pp.cancelarReservaColectivaPorId(idReservaColectiva);
		log.info("Reserva colectiva cancelada:" + resp.size());
		return resp;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Servicio Menaje
	 *****************************************************************/
	public ServicioMenaje adicionarServicioMenaje(String nombre, String tipo) {

		log.info("Adicionando Servicio o Menaje" + nombre + " y con tipo: " + tipo);

		ServicioMenaje sm = pp.adicionarServicioMenaje(nombre, tipo);
		log.info("Adicionando Servicio o Menaje: " + sm);
		return sm;
	}

	public long eliminarServicioMenajePorNombre(String nombre) {
		log.info("Eliminando el servicio o menaje: " + nombre);
		long op = pp.eliminarServicioMenajePorNombre(nombre);
		log.info("eliminando el servicio o menaje" + op);
		return op;
	}

	public ServicioMenaje darServicioMenajePorNombre(String nombre) {
		log.info("Consultando Servicio o Menaje con nombre:" + nombre);
		ServicioMenaje re = pp.darServicioMenajePorNombre(nombre);
		log.info("Consultando Servicio o Menaje: " + re);
		return re;
	}

	public List<ServicioMenaje> darServiciosMenajesPorTipo(String tipo) {
		log.info("Consultando Servicios o Menajes por tipo:" + tipo);
		List<ServicioMenaje> re = pp.darServiciosMenajesPorTipo(tipo);
		log.info("Consultando Servicios o Menajes: " + re);
		return re;
	}

	public List<ServicioMenaje> darServiciosMenajes() {
		log.info("Consultando Servicios o Menajes");
		List<ServicioMenaje> re = pp.darServiciosMenajes();
		log.info("Consultando Servicios o Menajes: " + re);
		return re;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar los Usuarios
	 *****************************************************************/
	public Usuario adicionarUsuario(String nombre, String email, String telefono, String tipo) {
		log.info("Adicionando usuario" + nombre + ", email: " + email + ", telefono: " + telefono + " y tipo: " + tipo);
		Usuario sm = pp.adicionarUsuario(nombre, email, telefono, tipo);
		log.info("Adicionando Usuario: " + sm);
		return sm;
	}

	public long eliminarUsuario(long idUsuario) {
		log.info("Eliminando Usuario: " + idUsuario);
		long op = pp.eliminarUsuarioPorId(idUsuario);
		log.info("eliminando usuario" + op);
		return op;
	}

	public Usuario darUsuarioPorId(long idUsuario) {
		log.info("Consultando Usuario : " + idUsuario);
		Usuario us = pp.darUsuarioPorId(idUsuario);
		log.info("Consultando Usuario: " + us);
		return us;
	}

	public Usuario darUsuarioPorEmail(String email) {
		log.info("Consultando Usuario por email : " + email);
		Usuario us = pp.darUsuarioPorEmail(email);
		log.info("Consultando Usuario por email: " + us);
		return us;
	}

	public List<Usuario> darUsuarios() {
		log.info("Consultando usuarios");
		List<Usuario> re = pp.darUsuarios();
		log.info("Consultando Usuarios: " + re);
		return re;
	}

	public List<Usuario> darUsuariosPorTipo(String tipo) {
		log.info("Consultando usuarios por tipo");
		List<Usuario> re = pp.darUsuariosPorTipo(tipo);
		log.info("Consultando Usuarios por tipo: " + re);
		return re;
	}

	/*
	 * **************************************************************** Métodos para
	 * manejar las Viviendas
	 *****************************************************************/
	public Vivienda adicionarVivienda(int numeroHabitaciones, double costoNoche, int diasUtilizado, long idPersona,
			String direccion, int capacidad, int disponible, Date fechaReservaFinal) {
		log.info("Adicionando Vivienda en: " + direccion + " con capacidad: " + capacidad + ", costo noche: "
				+ costoNoche + ", numero habitaciones: " + numeroHabitaciones + ", disponible: " + aTexto(disponible)
				+ " y dueño: " + idPersona);
		Vivienda viv = pp.adicionarVivienda(numeroHabitaciones, costoNoche, diasUtilizado, idPersona, direccion,
				capacidad, disponible, fechaReservaFinal);
		log.info("Adicionando Vivienda: " + viv);
		return viv;
	}

	public List<Vivienda> darViviendas() {
		log.info("Consultando Viviendas");
		List<Vivienda> re = pp.darViviendas();
		log.info("Consultando Vivienda: " + re);
		return re;
	}

	public Vivienda darViviendaPorId(long idVivienda) {
		log.info("Consultando Vivienda : " + idVivienda);
		Vivienda us = pp.darViviendaPorId(idVivienda);
		log.info("Consultando Vivienda: " + us);
		return us;
	}

	public List<Vivienda> darViviendaPorIdPersona(long idPersona) {
		log.info("Consultando Viviendas de la persona con id : " + idPersona);
		List<Vivienda> us = pp.darViviendasPorIdPersona(idPersona);
		log.info("Consultando Viviendas: " + us);
		return us;
	}

	public long actualizarViviendaDiasUtilizado(int diasUtilizado, long idVivienda) {
		log.info("Actualizando dias utilizado de vivienda a: " + diasUtilizado);
		long ans = pp.actualizarViviendaDiasUtilizado(diasUtilizado, idVivienda);
		log.info("actualizando vivienda");
		return ans;
	}

	/*
	 * **************************************************************** Métodos para
	 * requerimientos de consulta
	 *****************************************************************/

	public List<ReqConsulta1> RFC1() {
		log.info("Consultado requerimiento de consulta 1");
		List<ReqConsulta1> listaConsulta = pp.RFC1();
		log.info("Consultando RFC1: " + listaConsulta);
		return listaConsulta;
	}

	public List<ReqConsulta2> RFC2() {
		log.info("Consultado requerimiento de consulta 2");
		List<ReqConsulta2> listaConsulta = pp.RFC2();
		log.info("Consultando RFC2: " + listaConsulta);
		return listaConsulta;
	}

	public List<ReqConsulta3> RFC3() {
		log.info("Consultado requerimiento de consulta 3");
		List<ReqConsulta3> listaConsulta = pp.RFC3();
		log.info("Consultando RFC3: " + listaConsulta);
		return listaConsulta;
	}

	public List<ReqConsulta4> RFC4(Date X, Date Y, List<String> servicios) {
		log.info("Consultado requerimiento de consulta 4");
		List<ReqConsulta4> listaConsulta = pp.RFC4(X, Y, servicios);
		log.info("Consultando RFC4: " + listaConsulta);
		return listaConsulta;
	}
	
	public List<ReqConsulta5> RFC5() {
		log.info("Consultado requerimiento de consulta 5");
		List<ReqConsulta5> listaConsulta = pp.RFC5();
		log.info("Consultando RFC5: " + listaConsulta);
		return listaConsulta;
	}
	
	public ReqConsulta6 RFC6(long idUsuario) {
		log.info("Consultado requerimiento de consulta 6");
		ReqConsulta6 listaConsulta = pp.RFC6(idUsuario);
		log.info("Consultando RFC6: " + listaConsulta);
		return listaConsulta;
	}
	
	public List<Usuario> RFC8(long idInmuble){
		log.info("Consultado requerimiento de consulta 8");
		List<Usuario> listaConsulta = pp.RFC8(idInmuble);
		log.info("Consultando RFC8: " + listaConsulta);
		return listaConsulta;
	}
	
	public List<Inmueble> RFC9() throws Exception{
		log.info("Consultado requerimiento de consulta 9");
		List<Inmueble> listaConsulta = pp.RFC9();
		log.info("Consultando RFC9: " + listaConsulta);
		return listaConsulta;
	}
	
	public List<Usuario> RFC11(long inmuebleId, Date inicio, Date fin, boolean ordId, boolean ordNombre, boolean ordEmail, long idUsuario){
		log.info("Consultando requerimiento de consulta 11");
		List<Usuario> lista= pp.RFC11(inmuebleId, inicio, fin, ordId, ordNombre, ordEmail, idUsuario);
		log.info("Consultando RFC11"+ lista.size());
		return lista;
		
	}
	
	public List<Usuario> RFC10(long inmuebleId, Date inicio, Date fin, List<String> carac){
		log.info("Consultando requerimiento de consulta 10");
		List<Usuario> lista= pp.RFC10(inmuebleId, inicio, fin,carac);
		log.info("Consultando RFC10"+ lista.size());
		return lista;
		
	}
	public List<Usuario> RFC10Usuario(long inmuebleId, Date inicio, Date fin, long idUsuario,List<String> carac){
		log.info("Consultando requerimiento de consulta 10");
		List<Usuario> lista= pp.RFC10Usuario(inmuebleId, inicio, fin, idUsuario, carac);
		log.info("Consultando RFC10"+ lista.size());
		return lista;
		
	}
	
	public List<ReqConsulta12Inmueble> RFC12InmuebleMayor(){
		log.info("Consultando requerimiento de consulta 12 inmueble mayor");
		List<ReqConsulta12Inmueble> lista= pp.RFC12InmuebleMayor();
		log.info("Consultando RFC12 inmueble mayor"+ lista);
		return lista;
	}
	
	public List<ReqConsulta12Inmueble> RFC12InmuebleMenor(){
		log.info("Consultando requerimiento de consulta 12 inmueble menor");
		List<ReqConsulta12Inmueble> lista= pp.RFC12InmuebleMenor();
		log.info("Consultando RFC12 inmueble menor"+ lista);
		return lista;
	}
	
	public List<ReqConsulta12Operador> RFC12OperadorMayor(){
		log.info("Consultando requerimiento de consulta 12 operador mayor");
		List<ReqConsulta12Operador> lista= pp.RFC12OperadorMayor();
		log.info("Consultando RFC12 operador mayor"+ lista);
		return lista;
	}
	
	public List<ReqConsulta12Operador> RFC12OperadorMenor(){
		log.info("Consultando requerimiento de consulta 12 operador menor");
		List<ReqConsulta12Operador> lista= pp.RFC12OperadorMenor();
		log.info("Consultando RFC12 operador menor"+ lista);
		return lista;
	}
	
	public List<Long> RFC13(){
		log.info("Consultando requerimiento de consulta 13");
		List<Long> lista= pp.RFC13();
		log.info("Consultando RFC13 operador menor"+ lista.size());
		return lista;
	}
	
	
	public List<ReqConsulta7> mayorDemanda(String tipo) {
		log.info("Consultado requerimiento de consulta 7 mayor demanda");
		List<ReqConsulta7> listaConsulta = pp.mayorDemanda(tipo);
		
		log.info("Consultando RFC7: " + listaConsulta);
		return listaConsulta;
	}
	
	public List<ReqConsulta7> mayorIngresos(String tipo) {
		log.info("Consultado requerimiento de consulta 7 mayor ingresos");
		List<ReqConsulta7> listaConsulta = pp.mayorIngresos(tipo);
		log.info("Consultando RFC7: " + listaConsulta);
		return listaConsulta;
	}
	
	public List<ReqConsulta7> menorOcupacion(String tipo) {
		log.info("Consultado requerimiento de consulta 7 menor ocupacion");
		List<ReqConsulta7> listaConsulta = pp.menorOcupacion(tipo);
		log.info("Consultando RFC7: " + listaConsulta);
		return listaConsulta;
	}
	


	

	
	/****************************************************************** 
	 * Métodos para administración
	 *****************************************************************/

	private void verificarConsistenciaPersonaJuridica(String tipo, Integer categoria, Double precioNoche)
			throws Exception {
		if (tipo.equals(PersonaJuridica.TIPO_HOSTAL)) {
			if (precioNoche != null || categoria == null)
				throw new Exception("No cumple requisitos de hostal");
		} else if (tipo.equals(PersonaJuridica.TIPO_HOTEL)) {
			if (precioNoche == null || categoria != null)
				throw new Exception("No cumple requisitos de hotel");
		} else if (tipo.equals(PersonaJuridica.TIPO_VIVIENDAUNIVERSITARIA)) {
			if (precioNoche != null || categoria != null)
				throw new Exception("No cumple requisitos de vivienda universitaria");
		}
	}


	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de
	 * Parranderos
	 * 
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en
	 *         las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA, TIPOBEBIDA, BEBEDOR y
	 *         BAR, respectivamente
	 */
	public long[] limpiarAlohAndes() {
		log.info("Limpiando la BD de AlohAndes");
		long[] borrrados = pp.limpiarAlohAndes();
		log.info("Limpiando la BD de AlohAndes: Listo!");
		return borrrados;
	}
}

DROP TABLE "OPERADOR" CASCADE CONSTRAINTS;
DROP TABLE "USUARIO" CASCADE CONSTRAINTS;
DROP TABLE "PERSONA_JURIDICA" CASCADE CONSTRAINTS;
DROP TABLE "HORARIO" CASCADE CONSTRAINTS;
DROP TABLE "INMUEBLE" CASCADE CONSTRAINTS;
DROP TABLE "HABITACION_HOTEL" CASCADE CONSTRAINTS;
DROP TABLE "HABITACION_VIVIENDA" CASCADE CONSTRAINTS;
DROP TABLE "HABITACION_HOSTAL" CASCADE CONSTRAINTS;
DROP TABLE "SERVICIO_MENAJE" CASCADE CONSTRAINTS;

-- Eliminar el contenido de todas las tablas de la base de datos
-- El orden es importante. Por qu�?
DELETE FROM OPERADOR;
DELETE FROM USUARIO;
DELETE FROM PERSONA_JURIDICA;
DELETE FROM HORARIO;
DELETE FROM INMUEBLE;
DELETE FROM HABITACION_HOTEL;
DELETE FROM HABITACION_VIVIENDA;
DELETE FROM HABITACION_HOSTAL;
DELETE FROM SERVICIO_MENAJE;

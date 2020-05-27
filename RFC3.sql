SELECT INMUEBLE.ID AS IDINMUEBLE, AVG(RESERVA.CAPACIDAD/INMUEBLE.CAPACIDAD) AS TASAOCUPACION
FROM INMUEBLE
LEFT OUTER JOIN RESERVA ON RESERVA.IDINMUEBLE=INMUEBLE.ID
GROUP BY INMUEBLE.ID;
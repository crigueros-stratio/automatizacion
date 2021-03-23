package com.stratio.comfandi.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ingesta", schema = "public")
@Data
public class ParametrizacionIngesta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre_tabla;
	private Integer estado;
	private Integer periodicidad;
	private Date fecha_inicio_ingesta;
	private Date fecha_fin_ingesta;
	private String id_flujo;
	private String consulta_origen;
	private String consulta_destino;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre_tabla() {
		return nombre_tabla;
	}

	public void setNombre_tabla(String nombre_tabla) {
		this.nombre_tabla = nombre_tabla;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Integer getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(Integer periodicidad) {
		this.periodicidad = periodicidad;
	}

	public Date getFecha_inicio_ingesta() {
		return fecha_inicio_ingesta;
	}

	public void setFecha_inicio_ingesta(Date fecha_inicio_ingesta) {
		this.fecha_inicio_ingesta = fecha_inicio_ingesta;
	}

	public Date getFecha_fin_ingesta() {
		return fecha_fin_ingesta;
	}

	public void setFecha_fin_ingesta(Date fecha_fin_ingesta) {
		this.fecha_fin_ingesta = fecha_fin_ingesta;
	}

	public String getId_flujo() {
		return id_flujo;
	}

	public void setId_flujo(String id_flujo) {
		this.id_flujo = id_flujo;
	}

	public String getConsulta_origen() {
		return consulta_origen;
	}

	public void setConsulta_origen(String consulta_origen) {
		this.consulta_origen = consulta_origen;
	}

	public String getConsulta_destino() {
		return consulta_destino;
	}

	public void setConsulta_destino(String consulta_destino) {
		this.consulta_destino = consulta_destino;
	}

}
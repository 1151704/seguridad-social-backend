package ufps.web.professionacare.backend.container;

import java.io.Serializable;
import java.util.Date;

public class CuentaCobroGenerar implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fecha;
	
	private int id_asesor;

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getId_asesor() {
		return id_asesor;
	}

	public void setId_asesor(int id_asesor) {
		this.id_asesor = id_asesor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

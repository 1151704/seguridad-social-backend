package ufps.web.professionacare.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ufps.web.professionacare.backend.enums.EstadoOrden;

@Entity
@Table(name="sspt_cuenta_cobro")
public class SsptCuentaCobro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	private SsptUsuario asesor;
	
	@Column(nullable=false)
	private float total;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "estado_orden", nullable = true)
	private EstadoOrden estadoOrden;
	
	@Column(name = "fecha_orden", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date createAt;

	@PrePersist
	public void prePersist() {
		
		this.createAt=new Date();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public EstadoOrden getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(EstadoOrden estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public SsptUsuario getAsesor() {
		return asesor;
	}

	public void setAsesor(SsptUsuario asesor) {
		this.asesor = asesor;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	
	

}

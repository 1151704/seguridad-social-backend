package ufps.web.professionacare.backend.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ufps.web.professionacare.backend.model.SsptCuentaCobro;

public interface SsptCuentaCobroRepository extends CrudRepository<SsptCuentaCobro, Long > {

@Query( value="SELECT COUNT(precio) FROM sspt_orden_servicio" + 
" INNER JOIN sspt_cliente ON sspt_cliente.id = sspt_orden_servicio.id_cliente"+
" INNER JOIN sspt_usuario ON sspt_usuario.id = sspt_cliente.id_asesor" +
	" WHERE sspt_cliente.id_asesor= :id AND DATE(sspt_orden_servicio.fecha_pago) >= :fechaInicio AND DATE(sspt_orden_servicio.fecha_pago) <= :fechaFinal", nativeQuery=true)
	public int findByAsesor(Integer id, Date fechaInicio, Date fechaFinal);
}

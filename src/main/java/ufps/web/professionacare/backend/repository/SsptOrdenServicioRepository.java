package ufps.web.professionacare.backend.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ufps.web.professionacare.backend.model.SsptCliente;
import ufps.web.professionacare.backend.model.SsptOrdenServicio;

public interface SsptOrdenServicioRepository extends CrudRepository<SsptOrdenServicio, Integer>{

	public SsptOrdenServicio findLastByCliente(SsptCliente cliente);
	
	@Query(value = "SELECT sol.* FROM sspt_orden_servicio sol "
			+ "where sol.fecha_orden >= :fechaInicio and sol.fecha_orden <= :fechaFinal "
			+ "ORDER BY case when sol.estado_orden = 0 then 0 else 1 end, "
			+ "sol.fecha_orden desc", nativeQuery=true)
	public List<SsptOrdenServicio> findByFechaBetweenOnly(Date fechaInicio, Date fechaFinal);
	
	@Query(value = "SELECT sol.* FROM sspt_orden_servicio sol "
			+ "inner join sspt_cliente cli join sol.id_cliente = cli.id "
			+ "where sol.fecha_orden >= :fechaInicio and sol.fecha_orden <= :fechaFinal "
			+ "and cli.id_asesor = :idAsesor "
			+ "ORDER BY case when sol.estado_orden = 0 then 0 else 1 end, "
			+ "sol.fecha_orden desc", nativeQuery=true)
	public List<SsptOrdenServicio> findByFechaBetweenAsesor(Date fechaInicio, Date fechaFinal, Integer idAsesor);
	
	@Query(value = "SELECT sol.* FROM sspt_orden_servicio sol "
			+ "where sol.fecha_orden >= :fechaInicio and sol.fecha_orden <= :fechaFinal "
			+ "and sol.estado_orden = :estado "
			+ "ORDER BY case when sol.estado_orden = 0 then 0 else 1 end, "
			+ "sol.fecha_orden desc", nativeQuery=true)
	public List<SsptOrdenServicio> findByFechaBetweenAndEstadoOnly(Date fechaInicio, Date fechaFinal, Integer estado);
	
	@Query(value = "SELECT sol.* FROM sspt_orden_servicio sol "
			+ "inner join sspt_cliente cli join sol.id_cliente = cli.id "
			+ "where sol.fecha_orden >= :fechaInicio and sol.fecha_orden <= :fechaFinal "
			+ "and sol.estado_orden = :estado and cli.id_asesor = :idAsesor "
			+ "ORDER BY case when sol.estado_orden = 0 then 0 else 1 end, "
			+ "sol.fecha_orden desc", nativeQuery=true)
	public List<SsptOrdenServicio> findByFechaBetweenAndEstadoAsesor(Date fechaInicio, Date fechaFinal, Integer estado, Integer idAsesor);
	

	
}

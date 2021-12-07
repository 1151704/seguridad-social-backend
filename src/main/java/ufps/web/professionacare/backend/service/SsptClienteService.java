package ufps.web.professionacare.backend.service;

import java.util.Date;
import java.util.List;

import ufps.web.professionacare.backend.container.ClienteEntrada;
import ufps.web.professionacare.backend.model.SsptCliente;

public interface SsptClienteService {
	
	public SsptCliente getPorId(int id);
	
	public List<SsptCliente> get();
	
	public List<SsptCliente> listarTodos(Integer idAsesor);
	
	public SsptCliente guardar(SsptCliente cliente);
	
	public SsptCliente buscarPorCorreo(String correo);
	
	public SsptCliente getPorCedula(String cedula);
	
	public List<SsptCliente> filtradoReporte(String estado, Date fechaInicio, Date fechaFinal, Integer idAsesor);

}

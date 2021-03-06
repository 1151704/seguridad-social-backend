package ufps.web.professionacare.backend.service;

import java.util.Date;
import java.util.List;

import ufps.web.professionacare.backend.model.SsptCliente;
import ufps.web.professionacare.backend.model.SsptOrdenServicio;

public interface SsptOrdenServicioService {

	public SsptOrdenServicio getById(int id);

	public SsptOrdenServicio getByCliente(SsptCliente cliente);

	public List<SsptOrdenServicio> getAll();

	public SsptOrdenServicio save(SsptOrdenServicio entrada);

	public List<SsptOrdenServicio> filtradoReporte(String estado, Date fechaInicio, Date fechaFinal, Integer idAsesor);

}

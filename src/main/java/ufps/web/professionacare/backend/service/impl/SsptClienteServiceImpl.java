package ufps.web.professionacare.backend.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufps.web.professionacare.backend.container.ClienteEntrada;
import ufps.web.professionacare.backend.enums.EstadoCliente;
import ufps.web.professionacare.backend.model.SsptCliente;
import ufps.web.professionacare.backend.repository.SsptClienteRepository;
import ufps.web.professionacare.backend.service.SsptClienteService;

@Service
@Transactional
public class SsptClienteServiceImpl implements SsptClienteService {

	@Autowired
	private SsptClienteRepository c;

	@Override
	public SsptCliente getPorId(int id) {

		return c.findById(id).orElse(null);
	}

	@Override
	public List<SsptCliente> get() {

		return (List<SsptCliente>) c.findAll();
	}

	@Override
	public List<SsptCliente> listarTodos(Integer idAsesor) {
		if (idAsesor != null) {
			return c.findByAsesor(idAsesor);
		}
		return (List<SsptCliente>) c.findAll();
	}

	@Override
	public SsptCliente guardar(SsptCliente cliente) {

		return c.save(cliente);
	}

	@Override
	public SsptCliente getPorCedula(String cedula) {

		return c.findByIdentificacion(cedula);
	}

	@Override
	public SsptCliente buscarPorCorreo(String correo) {
		return c.findByCorreo(correo);
	}

	@Override
	public List<SsptCliente> filtradoReporte(String estado, Date fechaInicio, Date fechaFinal, Integer idAsesor) {
		Integer tipo = -1;
		try {
			tipo = EstadoCliente.valueOf(estado).ordinal();
		} catch (Exception e) {
		}
		if (tipo != -1) {
			if (idAsesor != null) {
				return c.findByFechaBetweenAndEstadoAsesor(fechaInicio, fechaFinal, tipo, idAsesor);
			} else {
				return c.findByFechaBetweenAndEstadoOnly(fechaInicio, fechaFinal, tipo);				
			}
		} else {
			if (idAsesor != null) {
				return c.findByFechaBetweenAsesor(fechaInicio, fechaFinal, idAsesor);
			}else {
				return c.findByFechaBetweenOnly(fechaInicio, fechaFinal);
			}
		}
	}


}

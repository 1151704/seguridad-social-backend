package ufps.web.professionacare.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufps.web.professionacare.backend.model.SsptMunicipio;
import ufps.web.professionacare.backend.service.impl.SsptMunicipioServiceImpl;

@RestController
@RequestMapping("/api/municipio/")
public class MunicipioController {

	@Autowired
	SsptMunicipioServiceImpl service;

	@GetMapping("getPorDepartamento/{id}")
	public List<SsptMunicipio> getPorDepartamentoId(@PathVariable int id) {

		return service.getPorDepartamento(id);

	}

	@GetMapping("getPorId/{id}")
	public SsptMunicipio getPorId(@PathVariable int id) {

		return service.getPorId(id);

	}
}

package ufps.web.professionacare.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufps.web.professionacare.backend.model.SsptActividadEconomica;
import ufps.web.professionacare.backend.service.SsptActividadEconomicaService;
import ufps.web.professionacare.backend.util.ValidationException;

@RestController
@RequestMapping("/api/actividades/")
public class ActividadEconomicaController {
	
	@Autowired
	private SsptActividadEconomicaService service;
	
	@GetMapping("todos")
	public ResponseEntity<List<SsptActividadEconomica>> getDepartamentos() {

		List<SsptActividadEconomica> api = new ArrayList<>();

		try {
			api = service.listarTodos();
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}

}

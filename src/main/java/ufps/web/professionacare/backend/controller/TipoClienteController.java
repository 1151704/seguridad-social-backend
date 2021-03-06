package ufps.web.professionacare.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import ufps.web.professionacare.backend.container.TiposClienteApi;
import ufps.web.professionacare.backend.service.impl.SsptTipoClienteServiceImpl;
import ufps.web.professionacare.backend.util.ValidationException;

@RestController
@RequestMapping("/api/tipoDeClientes/")
public class TipoClienteController {

	@Autowired
	private SsptTipoClienteServiceImpl service;

	@GetMapping("todos")
	public ResponseEntity<TiposClienteApi> getList() {

		TiposClienteApi api = new TiposClienteApi();

		try {
			api.setTiposCliente(service.get());
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}
	
	
}

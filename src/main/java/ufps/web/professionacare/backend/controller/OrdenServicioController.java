package ufps.web.professionacare.backend.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufps.web.professionacare.backend.container.OrdenEntrada;
import ufps.web.professionacare.backend.container.OrdenesApi;
import ufps.web.professionacare.backend.enums.EstadoCliente;
import ufps.web.professionacare.backend.enums.EstadoOrden;
import ufps.web.professionacare.backend.model.SsptCliente;
import ufps.web.professionacare.backend.model.SsptOrdenServicio;
import ufps.web.professionacare.backend.service.SsptClienteService;
import ufps.web.professionacare.backend.service.SsptOrdenServicioService;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenServicioController {

	@Autowired
	private SsptOrdenServicioService service;

	@Autowired
	private SsptClienteService clienteService;

	@PostMapping(value = "/save")
	public SsptOrdenServicio save(@RequestBody OrdenEntrada entrada) {

		SsptOrdenServicio orden = new SsptOrdenServicio();

		SsptCliente cliente = clienteService.getPorId(entrada.getIdCliente());
		if (cliente != null) {
			orden.setCliente(cliente);
			orden.setPrecio(cliente.getPlan().getPrecio());
			orden.prePersist();
			return service.save(orden);
		} else {
			return null;
		}
	}

	@GetMapping("/getId/{id}")
	public SsptOrdenServicio getById(@PathVariable int id) {

		return service.getById(id);
	}

	@GetMapping("/all")
	public OrdenesApi getAll() {
		OrdenesApi api = new OrdenesApi();
		api.setOrdenes(service.getAll());

		return api;
	}

	@GetMapping("/generarTodos")
	public boolean generarOrdenes() {
		System.out.print("se ejecutó");
		List<SsptCliente> clientes = clienteService.get();

		EstadoCliente estado = EstadoCliente.AFILIADO;
		try {
			for (SsptCliente cliente : clientes) {
				if (cliente.getEstadoCliente().equals(estado) && cliente.getPlan() != null) {
					SsptOrdenServicio orden = new SsptOrdenServicio();
					orden.setCliente(cliente);
					orden.setPrecio(cliente.getPlan().getPrecio());
					orden.prePersist();
					service.save(orden);
				}

			}
			return true;

		} catch (Exception e) {
			System.out.print(e);
			return false;
		}

	}

	@GetMapping("/generarOrden/{cedula}")
	public boolean generarOrdenPorId(@PathVariable String cedula) {
		System.out.print("se ejecutó");
		SsptCliente cliente = clienteService.getPorCedula(cedula);
		EstadoCliente estado = EstadoCliente.AFILIADO;

		try {

			if (cliente.getEstadoCliente().equals(estado) && cliente.getPlan() != null) {
				SsptOrdenServicio orden = new SsptOrdenServicio();
				orden.setCliente(cliente);
				orden.setPrecio(cliente.getPlan().getPrecio());
				orden.prePersist();
				service.save(orden);
				return true;
			}

		} catch (Exception e) {
			System.out.print(e);
		}

		return false;
	}

	@GetMapping("/pagar/{id}")
	public boolean pagar(@PathVariable int id) {
		try {
			SsptOrdenServicio orden = service.getById(id);
			orden.setFechaPago(new Date());
			orden.setEstadoOrden(EstadoOrden.PAGADA);
			service.save(orden);
			return true;
		} catch (Exception e) {
			System.err.print(e);
			return false;
		}

	}

}

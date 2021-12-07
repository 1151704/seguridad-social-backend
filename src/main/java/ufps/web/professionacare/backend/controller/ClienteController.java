package ufps.web.professionacare.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ufps.web.professionacare.backend.container.ClienteEntrada;
import ufps.web.professionacare.backend.container.ClientesApi;
import ufps.web.professionacare.backend.container.ConsultaOrdenApi;
import ufps.web.professionacare.backend.enums.EstadoCliente;
import ufps.web.professionacare.backend.model.SsptCliente;
import ufps.web.professionacare.backend.model.SsptUsuario;
import ufps.web.professionacare.backend.service.SsptClienteService;
import ufps.web.professionacare.backend.service.SsptOrdenServicioService;
import ufps.web.professionacare.backend.service.SsptUsuarioService;

@RestController
@RequestMapping("/api/clientes/")
public class ClienteController {

	@Autowired
	private SsptClienteService service;


	@Autowired
	private SsptUsuarioService usuarioService;
	
	


	@GetMapping("todos")
	public ClientesApi getAll() {
		ClientesApi api = new ClientesApi();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SsptUsuario usuarioRta = usuarioService.buscarPorUsername(authentication.getName());
		Integer idAsesor = null;
		if (usuarioRta.getRol().getRol().equals("ROLE_USER")) {
			idAsesor = usuarioRta.getId();
		}
		api.setClientes(service.listarTodos(idAsesor));
		return api;
	}

	@PostMapping("crear")
	public SsptCliente crearCliente(@RequestBody SsptCliente cliente) {
		return service.guardar(cliente);
	}
	
	@GetMapping("porId/{id}")
	public SsptCliente getPorId(@PathVariable int id) {
		return service.getPorId(id);
	}

	@GetMapping("porCedula/{cedula}")
	public ConsultaOrdenApi getPorCedula(@PathVariable String cedula) {
		ConsultaOrdenApi api = new ConsultaOrdenApi();

		api.setCliente(service.getPorCedula(cedula));
		

		api.setCliente(service.getPorCedula(cedula));

		//api.setOrden(ordenesService.getByCliente(api.getCliente()));
		return api;
	}

	@PostMapping("cambiarEstado/{id}/{estado}")
	public SsptCliente cambiarEstado(@PathVariable int id, @PathVariable String estado) {
		SsptCliente cli = this.getPorId(id);
		cli.setEstadoCliente(EstadoCliente.valueOf(estado));
		return service.guardar(cli);
	}
	
	
	//Falta implementar
	@PostMapping("guardar")
	public SsptCliente guardar(@RequestBody ClienteEntrada c) {
		SsptCliente cliente= new SsptCliente();
		cliente.setApellido1(c.getApellido1());
		cliente.setApellido2(c.getApellido2());
		return cliente;
		
	}


//	@Autowired
//	private SsptTipoIdentificacionService tipoIdentificacionService;
//
//	@Autowired
//	private SsptTipoClienteService tipoClienteService;
//
//	@Autowired
//	private SsptMunicipioService municipioser;
//	@PostMapping(value = "save")
//	public SsptCliente save(@RequestBody ClienteEntrada entrada) {
//		// ClienteApi api = new ClienteApi();
//
//		SsptTipoIdentificacion tipoId = tipoIdentificacionService.buscarPorId(entrada.getIdTipoIdentificacion());
//		SsptTipoCliente tipoCliente = tipoClienteService.buscarPorId(entrada.getIdTipoCliente());
//		SsptMunicipio municipio = municipioser.getPorId(entrada.getIdMunicipio());
//
//		if (tipoId == null || tipoCliente == null) {
//			return null;
//		}
//
//		SsptCliente client = new SsptCliente();
//
//		client.setNombre1(entrada.getNombre1());
//		client.setNombre2(entrada.getNombre2());
//		client.setApellido1(entrada.getApellido1());
//		client.setApellido2(entrada.getApellido2());
//		client.setCorreo(entrada.getCorreo());
//		client.setTelefono(entrada.getTelefono());
//		client.setDireccion(entrada.getDireccion());
//		client.setIdentificacion(entrada.getIdentificacion());
//
//		client.setTipoIdentificacion(tipoId);
//		client.setTipoCliente(tipoCliente);
//		client.setMunicipio(municipio);
//
//		return service.guardar(client);
//	}

//	@GetMapping("porEstado")
//	public SsptCliente GetPorEstado(@RequestBody EstadoCliente e) {
//		return service.GetPorEstado(e);
//	}

}

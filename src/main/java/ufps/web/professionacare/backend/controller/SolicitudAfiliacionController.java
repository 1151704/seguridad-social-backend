package ufps.web.professionacare.backend.controller;

import ufps.web.professionacare.backend.service.EmailService;
import ufps.web.professionacare.backend.service.SsptClienteService;
import ufps.web.professionacare.backend.service.SsptEmpresaService;
import ufps.web.professionacare.backend.service.SsptFileService;
import ufps.web.professionacare.backend.service.SsptMunicipioService;
import ufps.web.professionacare.backend.service.SsptPlanService;
import ufps.web.professionacare.backend.service.SsptSolicitudAfiliacionService;
import ufps.web.professionacare.backend.service.SsptTipoClienteService;
import ufps.web.professionacare.backend.service.SsptTipoIdentificacionService;
import ufps.web.professionacare.backend.service.SsptUsuarioService;
import ufps.web.professionacare.backend.util.ValidationException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufps.web.professionacare.backend.container.SolicitudApi;
import ufps.web.professionacare.backend.container.SolicitudEntradaApi;
import ufps.web.professionacare.backend.container.SolicitudRespuestaEntradaApi;
import ufps.web.professionacare.backend.container.SolicitudesApi;
import ufps.web.professionacare.backend.container.SolicitudesBusquedaEntradaApi;
import ufps.web.professionacare.backend.enums.EstadoCliente;
import ufps.web.professionacare.backend.enums.EstadoSolicitudAfiliacion;
import ufps.web.professionacare.backend.model.*;

@RestController
@RequestMapping("/api/solicitudes/")
public class SolicitudAfiliacionController {

	@Autowired
	public SsptSolicitudAfiliacionService service;
	
	@Autowired
	public SsptClienteService clienteService;

	@Autowired
	public SsptEmpresaService empresaService;
	
	@Autowired
	public SsptUsuarioService usuarioService;
	
	@Autowired
	public SsptPlanService planService;
	
	@Autowired
	private SsptTipoIdentificacionService tipoIdentificacionService;
	
	@Autowired
	private SsptTipoClienteService tipoClienteService;
		
	@Autowired 
	private SsptMunicipioService municipioser;

	@Autowired
	private SsptFileService fileService;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("findAll")
	public List<SsptSolicitudAfiliacion> index(){
		
		return service.Get();
	}
	
	@GetMapping("todos")
	public ResponseEntity<SolicitudesApi> getList() {

		SolicitudesApi api = new SolicitudesApi();

		try {
			api.setSolicitudes(service.Get());
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}
	
	@PostMapping("busqueda")
	public ResponseEntity<SolicitudesApi> getByBusqueda(@RequestBody SolicitudesBusquedaEntradaApi entrada) {

		SolicitudesApi api = new SolicitudesApi();

		Date fecha = null;
		try {
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse(entrada.getFecha());
		} catch (Exception e) {
		}

		try {
			
			api.setSolicitudes(service.busqueda(entrada.getBusqueda().toUpperCase(), entrada.getEstado(), fecha, entrada.getPorFecha()));
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<SolicitudApi> getById(@PathVariable Integer id) {

		SolicitudApi api = new SolicitudApi();

		try {
			api.setSolicitud(service.GetPorId(id));;
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = { "multipart/form-data" })
	public SsptSolicitudAfiliacion guardar(@ModelAttribute SolicitudEntradaApi entrada) {
			
		// Verificar si ya existe
		SsptCliente cliente = clienteService.GetPorCedula(entrada.getIdentificacion());

		SsptTipoIdentificacion tipoId = tipoIdentificacionService.buscarPorId(entrada.getIdTipoIdentificacion());
		SsptTipoCliente tipoCliente = tipoClienteService.buscarPorId(entrada.getIdTipoCliente());
		SsptMunicipio municipio = municipioser.getPorId(entrada.getIdMunicipio());
		
		SsptPlan tipoPlan = planService.buscarPorId(entrada.getIdTipoPlan());

		if (cliente == null) {
			cliente = new SsptCliente();

			cliente.setNombre1(entrada.getNombre1());
			cliente.setNombre2(entrada.getNombre2());
			cliente.setApellido1(entrada.getApellido1());
			cliente.setApellido2(entrada.getApellido2());
			cliente.setIdentificacion(entrada.getIdentificacion());
			cliente.setTipoIdentificacion(tipoId);	
			cliente.setTipoCliente(tipoCliente);	

			cliente.setCorreo(entrada.getCorreo());
			cliente.setTelefono(entrada.getTelefono());
			cliente.setDireccion(entrada.getDireccion());
			cliente.setMunicipio(municipio);
			
			clienteService.guardar(cliente);
			
		}
		
		// Registrar solicitud		
		SsptSolicitudAfiliacion solicitud = new SsptSolicitudAfiliacion();
		
		solicitud.setObservaciones(entrada.getObservaciones());
		solicitud.setSsptCliente(cliente);
		solicitud.setSsptPlan(tipoPlan);		
		
		// Registrar soportes
		Set<SsptSoporteAfiliacion> soportes = new HashSet<>();
		if (entrada.getFile() != null) {
			SsptFile mppFile =fileService.save(entrada.getFile());
			if (mppFile != null) {
				SsptSoporteAfiliacion soporteCedula = new SsptSoporteAfiliacion();
				
				soporteCedula.setDescripcion("Soporte de Cédula");
				soporteCedula.setFile(mppFile);
				soporteCedula.setSolicitud(solicitud);
				
				soportes.add(soporteCedula);
			}		
		}
		solicitud.setSoportes(soportes);		
		
		return service.guardar(solicitud);
	}
	
	@PostMapping("responder/{id}")
	public SsptSolicitudAfiliacion responder(@PathVariable int id, @RequestBody SolicitudRespuestaEntradaApi entrada) {
		
		SsptSolicitudAfiliacion soli = service.GetPorId(id);

		soli.setEstadoSolicitud(EstadoSolicitudAfiliacion.valueOf(entrada.getRespuesta()));
		soli.setRespuesta(entrada.getObservacion());
		soli.setFechaRespuesta(new Date());
		
		SsptEmpresa empresa = empresaService.getEmpresaActual();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String cuerpoMensaje = "<h1>Respuesta de Solicitud de Afiliación</h1>\r\n"
				+ "<p>Cordial Saludo <b>%s</b></p>\r\n"
				+ "<br>\r\n"
				+ "<p>Le informamos que se ha dado respuesta a su solicitud con número de identificación <b>%s</b>. La respuesta a su solicitud es: <b><i>%s</i></b>, a continuación encontrará el detalle de su respuesta.</p>\r\n"
				+ "<br>\r\n"
				+ "<p><span><b>Observaciones: </b></span>%s</p>\r\n"
				+ "<p><span><b>Fecha de respuesta: </b></span>%s</p>\r\n"
				+ "<br>\r\n"
				+ "<hr>\r\n"
				+ "<footer>\r\n"
				+ "    <p><span><b>%s</b></span></p>\r\n"
				+ "    <p>Dirección: <span><b>%s</b></span></p>\r\n"
				+ "    <p>Teléfonos: <span><b>%s</b></span></p>\r\n"
				+ "    <p>Correo: <span><b>%s</b></span></p>\r\n"
				+ "</footer>\r\n"
				+ "<br>\r\n"
				+ "<hr>\r\n"
				+ "<p><i>***!!! FAVOR NO RESPONDER A ESTE CORREO, ES SOLO DE GESTIÓN AUTOMÁTICA Y NO SE MONITOREA ¡¡¡***.</i></p>\r\n"
				+ "<hr>";
		
		cuerpoMensaje = String.format(cuerpoMensaje, 
				soli.getSsptCliente().getNombreCompleto(), 
				soli.getSsptCliente().getIdentificacion(),
				soli.getEstadoSolicitud().getNombre(),
				soli.getRespuesta(),
				format.format(soli.getFechaRespuesta()),
				empresa.getNombre(),
				empresa.getDireccion(),
				empresa.getTelefono(),
				empresa.getEmail());
		
		emailService.sendMessageWithAttachment("PROFESSIONAL CARE - RESPUESTA DE AFILIACIÓN", cuerpoMensaje, 
				soli.getSsptCliente().getCorreo());
		
		return service.guardar(soli);
	}
	
	@PostMapping("cambiarEstado/{id}/{estado}")
	public SsptSolicitudAfiliacion cambiarEstado(@PathVariable int id , @PathVariable String estado) {
		
		SsptSolicitudAfiliacion soli = service.GetPorId(id);
		
		soli.setEstadoSolicitud(EstadoSolicitudAfiliacion.valueOf(estado));
		SsptCliente cli = soli.getSsptCliente();
		
		
		if(estado.equals("APROBADA")) {
			
			cli.setEstadoCliente(EstadoCliente.valueOf("AFILIADO"));
		}
		else {
			cli.setEstadoCliente(EstadoCliente.valueOf("NEGADO"));
		}
		
		return service.guardar(soli);
	}
	
	
}

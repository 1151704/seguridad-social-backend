package ufps.web.professionacare.backend.controller;

import ufps.web.professionacare.backend.service.EmailService;
import ufps.web.professionacare.backend.service.SsptActividadEconomicaService;
import ufps.web.professionacare.backend.service.SsptClienteService;
import ufps.web.professionacare.backend.service.SsptEmpresaService;
import ufps.web.professionacare.backend.service.SsptFileService;
import ufps.web.professionacare.backend.service.SsptMunicipioService;
import ufps.web.professionacare.backend.service.SsptPlanService;
import ufps.web.professionacare.backend.service.SsptSolicitudAfiliacionService;
import ufps.web.professionacare.backend.service.SsptTipoClienteService;
import ufps.web.professionacare.backend.service.SsptTipoIdentificacionService;
import ufps.web.professionacare.backend.service.SsptUsuarioService;
import ufps.web.professionacare.backend.util.FormateadorService;
import ufps.web.professionacare.backend.util.ValidationException;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public FormateadorService formatService;

	@Autowired
	public SsptClienteService clienteService;

	@Autowired
	public SsptEmpresaService empresaService;

	@Autowired
	public SsptActividadEconomicaService actividadService;

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
	public List<SsptSolicitudAfiliacion> index() {

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

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SsptUsuario usuarioRta = usuarioService.buscarPorUsername(authentication.getName());
		Integer idAsesor = null;
		SolicitudesApi api = new SolicitudesApi();
		if (usuarioRta.getRol().getRol().equals("ROLE_USER")) {
			idAsesor = usuarioRta.getId();
		}
		Date fecha = null;
		try {
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse(entrada.getFecha());
		} catch (Exception e) {
		}
		try {
			api.setSolicitudes(service.busqueda(entrada.getBusqueda().toUpperCase(), entrada.getEstado(), fecha,
					entrada.getPorFecha(), idAsesor));
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<SolicitudApi> getById(@PathVariable Integer id) {

		SolicitudApi api = new SolicitudApi();

		try {
			api.setSolicitud(service.GetPorId(id));
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}

	@PostMapping(value = "save", consumes = { "multipart/form-data" })
	public ResponseEntity<SsptSolicitudAfiliacion> afiliar(@ModelAttribute SolicitudEntradaApi entrada) {
		SsptSolicitudAfiliacion solicitud = new SsptSolicitudAfiliacion();
		try {

			// Verificar si ya existe
			SsptCliente cliente = clienteService.getPorCedula(entrada.getIdentificacion());

			SsptTipoIdentificacion tipoId = tipoIdentificacionService.buscarPorId(entrada.getIdTipoIdentificacion());
			SsptTipoCliente tipoCliente = tipoClienteService.buscarPorId(entrada.getIdTipoCliente());
			SsptMunicipio municipio = municipioser.getPorId(entrada.getIdMunicipio());
			SsptActividadEconomica actividad = actividadService.buscarPorId(entrada.getIdActividad());
			SsptUsuario asesor = null;

			if (entrada.getIdAsesor() != null) {
				asesor = usuarioService.buscarPorId(entrada.getIdAsesor());
				if (asesor == null) {
					throw new Exception("El código del asesor es invalido");
				}
			}

			SsptPlan tipoPlan = planService.buscarPorId(entrada.getIdTipoPlan());

			if (tipoPlan == null) {
				throw new Exception("Debe seleccionar el plan");
			}
			if (entrada.getFile() == null) {
				throw new Exception("Debe ingresar un soporte");
			}

			if (cliente == null) {

				if (!formatService.verificarEmail(entrada.getCorreo())) {
					throw new Exception("Debe ingresar el correo electronico valido.");
				}

				cliente = clienteService.buscarPorCorreo(entrada.getCorreo());
				if (cliente != null) {
					throw new Exception("El correo ya se encuentra en uso, por favor ingrese otro.");
				}
				if (actividad == null) {
					throw new Exception("Debe seleccionar la actividad economica.");
				}
				if (tipoId == null) {
					throw new Exception("Debe seleccionar el tipo de identificación.");
				}
				if (municipio == null) {
					throw new Exception("Debe seleccionar el municipio de residencia.");
				}
				if (!formatService.verificarDato(entrada.getNombre1())) {
					throw new Exception("Debe ingresar el primer nombre.");
				}
				if (!formatService.verificarDato(entrada.getApellido1())) {
					throw new Exception("Debe ingresar el primer apellido.");
				}
				if (!formatService.verificarDato(entrada.getIdentificacion())
						&& entrada.getIdentificacion().length() >= 7) {
					throw new Exception("El documento de identidad debe ser mayor o igual a 7 digitos.");
				}

				Date fechaNacimiento = formatService.getFecha(entrada.getFechaNacimiento());

				if (fechaNacimiento == null) {
					throw new Exception("La fecha de Nacimiento es invalida: " + entrada.getFechaNacimiento() + ".");
				}
				Date fechaExpedicion = formatService.getFecha(entrada.getFechaExpedicion());

				if (fechaExpedicion == null) {
					throw new Exception("La fecha de Expedición es invalida: " + entrada.getFechaExpedicion() + ".");
				}

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
				cliente.setProfesion(entrada.getProfesion());
				cliente.setDv(entrada.getDv());
				cliente.setIbc(entrada.getIbc());
				cliente.setLugarExpedicion(entrada.getLugarExpedicion());
				cliente.setFechaExpedicion(fechaExpedicion);
				cliente.setFechaNacimiento(fechaNacimiento);
				cliente.setDireccion(entrada.getDireccion());
				cliente.setActividad(actividad);
				cliente.setMunicipio(municipio);

				clienteService.guardar(cliente);

			}
			solicitud.setObservaciones(entrada.getObservaciones());
			solicitud.setSsptCliente(cliente);
			solicitud.setSsptPlan(tipoPlan);
			if (asesor != null) {
				solicitud.setSsptUsuario(asesor);
			} else {
				if (cliente.getAsesor() != null) {
					asesor = cliente.getAsesor();
					solicitud.setSsptUsuario(asesor);
				}
			}
			if (solicitud.getSsptUsuario() == null) {
				asesor = usuarioService.asesorDisponible();
			}
			solicitud.setSsptUsuario(asesor);
			if (cliente.getAsesor() == null) {
				cliente.setAsesor(asesor);
				cliente = clienteService.guardar(cliente);
			}
			// Registrar soportes
			Set<SsptSoporteAfiliacion> soportes = new HashSet<>();
			SsptFile mppFile = fileService.save(entrada.getFile());
			if (mppFile != null) {
				SsptSoporteAfiliacion soporteCedula = new SsptSoporteAfiliacion();

				soporteCedula.setDescripcion("DNI");
				soporteCedula.setFile(mppFile);
				soporteCedula.setSolicitud(solicitud);

				soportes.add(soporteCedula);
			}
			solicitud.setSoportes(soportes);

			solicitud = service.guardar(solicitud);

			SsptEmpresa empresa = empresaService.getEmpresaActual();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String cuerpoMensaje = "<h1>Registro de Solicitud de Afiliación</h1>\r\n"
					+ "<p>Cordial Saludo <b>%s</b></p>\r\n" + "<br>\r\n"
					+ "<p>Le informamos que hemos recibido su solicitud con número de identificación <b>%s</b>. La respuesta a su solicitud se le dará por este medio, a continuación encontrará el detalle de su solicitud.</p>\r\n"
					+ "<br>\r\n" + "<p><span><b>Observaciones: </b></span>%s</p>\r\n"
					+ "<p><span><b>Fecha de envio: </b></span>%s</p>\r\n" + "<br>\r\n" + "<hr>\r\n" + "<footer>\r\n"
					+ "    <p><span><b>%s</b></span></p>\r\n" + "    <p>Dirección: <span><b>%s</b></span></p>\r\n"
					+ "    <p>Teléfonos: <span><b>%s</b></span></p>\r\n"
					+ "    <p>Correo: <span><b>%s</b></span></p>\r\n" + "</footer>\r\n" + "<br>\r\n" + "<hr>\r\n"
					+ "<p><i>***!!! FAVOR NO RESPONDER A ESTE CORREO, ES SOLO DE GESTIÓN AUTOMÁTICA Y NO SE MONITOREA ¡¡¡***.</i></p>\r\n"
					+ "<hr>";

			String cuerpoMensajeAsesor = "<h1>Registro de Solicitud de Afiliación</h1>"
					+ "<p>Se ha registrado la solicitud #%s del cliente %s con identificación: %s</p>"
					+ "<p>Fecha registro: %s</p>" + "<p>Observaciones del Cliente: %s</p>" + "<hr>"
					+ "<p><i>***!!! FAVOR NO RESPONDER A ESTE CORREO, ES SOLO DE GESTIÓN AUTOMÁTICA Y NO SE MONITOREA ¡¡¡***.</i></p>"
					+ "<hr>";

			cuerpoMensaje = String.format(cuerpoMensaje, solicitud.getSsptCliente().getNombreCompleto(),
					solicitud.getSsptCliente().getIdentificacion(), solicitud.getObservaciones(),
					format.format(solicitud.getFechaRegistro()), empresa.getNombre(), empresa.getDireccion(),
					empresa.getTelefono(), empresa.getEmail());

			cuerpoMensajeAsesor = String.format(cuerpoMensajeAsesor, solicitud.getId().toString(),
					solicitud.getSsptCliente().getNombreCompleto(), solicitud.getSsptCliente().getIdentificacion(),
					format.format(solicitud.getFechaRegistro()), solicitud.getObservaciones());

			emailService.sendMessageWithAttachment("PROFESSIONAL CARE - REGISTRO DE SOLICITUD", cuerpoMensaje,
					cliente.getCorreo());
			if (solicitud.getSsptUsuario() != null) {
				emailService.sendMessageWithAttachment("PROFESSIONAL CARE - REGISTRO DE SOLICITUD", cuerpoMensajeAsesor,
						solicitud.getSsptUsuario().getEmail());
			}

		} catch (ValidationException e) {
			e.printStackTrace();
			throw new ValidationException(e.getMsg(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(solicitud, HttpStatus.OK);
	}

	@GetMapping(value = "cambiar-asesor/{id}")
	public SsptSolicitudAfiliacion responder(@PathVariable int id, @RequestParam Integer idAsesor) {

		SsptSolicitudAfiliacion soli = service.GetPorId(id);
		SsptUsuario usuarioRta = usuarioService.buscarPorId(idAsesor);

		soli.setSsptUsuario(usuarioRta);

		soli = service.guardar(soli);

		return soli;
	}

	@PostMapping("responder/{id}")
	public SsptSolicitudAfiliacion responder(@PathVariable int id, @RequestBody SolicitudRespuestaEntradaApi entrada) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SsptUsuario usuarioRta = usuarioService.buscarPorUsername(authentication.getName());
		SsptSolicitudAfiliacion soli = service.GetPorId(id);
		soli.setEstadoSolicitud(EstadoSolicitudAfiliacion.valueOf(entrada.getRespuesta()));
		soli.setRespuesta(entrada.getObservacion());
		soli.setSsptUsuario(usuarioRta);
		soli.setFechaRespuesta(new Date());
		SsptEmpresa empresa = empresaService.getEmpresaActual();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String cuerpoMensaje = "<h1>Respuesta de Solicitud de Afiliación</h1>\r\n";
		cuerpoMensaje = String.format(cuerpoMensaje, soli.getSsptCliente().getNombreCompleto(),
				soli.getSsptCliente().getIdentificacion(), soli.getEstadoSolicitud().getNombre(), soli.getRespuesta(),
				format.format(soli.getFechaRespuesta()), empresa.getNombre(), empresa.getDireccion(),
				empresa.getTelefono(), empresa.getEmail());

		emailService.sendMessageWithAttachment("PROFESSIONAL CARE - RESPUESTA DE SOLICITUD", cuerpoMensaje,
				soli.getSsptCliente().getCorreo());
		try {
			soli = service.guardar(soli);
			SsptCliente cliente = soli.getSsptCliente();

			if (soli.getEstadoSolicitud().equals(EstadoSolicitudAfiliacion.APROBADA)) {
				cliente.setPlan(soli.getSsptPlan());
				cliente.setAsesor(soli.getSsptUsuario());
				cliente.setEstadoCliente(EstadoCliente.AFILIADO);
			}
			if (soli.getEstadoSolicitud().equals(EstadoSolicitudAfiliacion.NEGADA)) {
				cliente.setEstadoCliente(EstadoCliente.NEGADO);
			}
			clienteService.guardar(cliente);
		} catch (Exception e) {
			System.out.println(e);
		}

		return soli;
	}

}

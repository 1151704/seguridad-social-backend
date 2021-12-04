package ufps.web.professionacare.backend;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ufps.web.professionacare.backend.model.SsptActividadEconomica;
import ufps.web.professionacare.backend.model.SsptCliente;
import ufps.web.professionacare.backend.model.SsptMunicipio;
import ufps.web.professionacare.backend.model.SsptTipoCliente;
import ufps.web.professionacare.backend.model.SsptTipoIdentificacion;
import ufps.web.professionacare.backend.model.SsptUsuario;
import ufps.web.professionacare.backend.service.SsptActividadEconomicaService;
import ufps.web.professionacare.backend.service.SsptClienteService;
import ufps.web.professionacare.backend.service.SsptMunicipioService;
import ufps.web.professionacare.backend.service.SsptTipoClienteService;
import ufps.web.professionacare.backend.service.SsptTipoIdentificacionService;
import ufps.web.professionacare.backend.service.SsptUsuarioService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SeguridadSocialBackendApplicationTests {

	@Autowired
    private MockMvc mvc;

	@Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private SsptClienteService clienteService;

	@Autowired
	private SsptTipoClienteService tipoClienteService;

	@Autowired
	private SsptTipoIdentificacionService tipoIdentificacionService;

	@Autowired
	private SsptMunicipioService municipioService;

	@Autowired
	private SsptUsuarioService usuarioService;

	@Autowired
	private SsptActividadEconomicaService actividadService;


	@Test
	public void contextLoads() {
	}
	
//	@Test
//	public void guardarClienteTest() throws Exception {

//		SsptCliente cliente = new SsptCliente();
//		cliente.setNombre1("Judith");
//		cliente.setNombre2("Pilar");
//		cliente.setApellido1("Rodriguez");
//		cliente.setApellido2("Tenjo");
//		cliente.setIdentificacion("123456789");
//		cliente.setTipoCliente(tipoClienteService.buscarPorId(1));
//		cliente.setTipoIdentificacion(tipoIdentificacionService.buscarPorTipo("CC"));
//		cliente.setMunicipio(municipioService.getPorId(1));
//		cliente.setAsesor(usuarioService.asesorDisponible());
//		cliente.setActividad(actividadService.buscarPorId(1));
//
//		clienteService.guardar(cliente);
//        mvc.perform(MockMvcRequestBuilders.post("/api/clientes/crear")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(cliente)))
//                .andExpect(jsonPath("$.id").exists())
//                .andReturn();

//		mvc.perform(get("/api/clientes/porCedula/"+cliente.getIdentificacion())
//				.contentType(MediaType.APPLICATION_JSON))
//	      		.andExpect(status().isOk())
//	      		.andExpect(content()
//	      		.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//	      		.andExpect(jsonPath("$.cliente.identificacion", is(cliente.getIdentificacion())));
//	}
}
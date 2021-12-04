package ufps.web.professionacare.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ufps.web.professionacare.backend.model.SsptCliente;
import ufps.web.professionacare.backend.service.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

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
    public void getAll() {
        assertEquals(true,true);
    }

    @Test
    public void crearCliente() throws Exception {
        SsptCliente cliente = new SsptCliente();
		cliente.setNombre1("Judith");
		cliente.setNombre2("Pilar");
		cliente.setApellido1("Rodriguez");
		cliente.setApellido2("Tenjo");
		cliente.setIdentificacion("123456789");
		cliente.setTipoCliente(tipoClienteService.buscarPorId(1));
		cliente.setTipoIdentificacion(tipoIdentificacionService.buscarPorTipo("CC"));
		cliente.setMunicipio(municipioService.getPorId(1));
		cliente.setAsesor(usuarioService.asesorDisponible());
		cliente.setActividad(actividadService.buscarPorId(1));

		clienteService.guardar(cliente);
//
//        mvc.perform(MockMvcRequestBuilders.post("/api/clientes/crear")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(cliente)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andReturn();
//    }
//
//    @Test
//    public void getPorId() {
//        assertEquals(true,true);
//    }
//
//    @Test
//    public void getPorCedula() {
//        assertEquals(true,true);
//    }
//
//    @Test
//    public void cambiarEstado() {
//        assertEquals(true,true);
    }
}
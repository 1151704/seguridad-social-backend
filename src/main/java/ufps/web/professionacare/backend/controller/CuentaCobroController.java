package ufps.web.professionacare.backend.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufps.web.professionacare.backend.container.CuentaCobroGenerar;
import ufps.web.professionacare.backend.model.SsptCuentaCobro;
import ufps.web.professionacare.backend.model.SsptOrdenServicio;
import ufps.web.professionacare.backend.model.SsptUsuario;
import ufps.web.professionacare.backend.repository.SsptCuentaCobroRepository;
import ufps.web.professionacare.backend.repository.SsptOrdenServicioRepository;
import ufps.web.professionacare.backend.service.impl.SsptOrdenServicioServiceImpl;
import ufps.web.professionacare.backend.service.impl.SsptUsuarioServiceImpl;

@RestController
@RequestMapping("/api/cuenta_cobro")
public class CuentaCobroController {
	
	@Autowired
	SsptCuentaCobroRepository repo;
	@Autowired
	SsptUsuarioServiceImpl service;
	
	@Autowired
	SsptOrdenServicioRepository orden;
	
//	@PostMapping("/guardar")
//	public SsptCuentaCobro save(@RequestBody CuentaCobroGenerar cu) {
//		
//		List<SsptOrdenServicio> ordenes = (List<SsptOrdenServicio>) orden.findAll();
//		
//		Set<SsptOrdenServicio> filtrado = new TreeSet<>();
//		int mes = cu.getFecha().getMonth();
//	    int año=  cu.getFecha().getYear();
//		Date fechaInicio= new Date(año, mes, 01);
//	    Date fechaFinal= new Date(año, mes, 28);
//	    SsptUsuario asesor= service.buscarPorId(cu.getId_asesor());
//	    float sum=0;
//		for(int i=0; i<ordenes.size(); i++) {
//			SsptUsuario as=ordenes.get(i).getCliente().getAsesor();
//			Date fechaa=ordenes.get(i).getFechaPago();
//			
//			if(as.getId()==cu.getId_asesor() && fechaa.getMonth()==mes && fechaa.getYear()==año) {
//				sum+=12000;
//			}
//		}
//		
//		SsptCuentaCobro cuenta = new SsptCuentaCobro();
//		cuenta.setTotal(sum);
//		cuenta.setAsesor(asesor);
//		
//		return repo.save(cuenta);
//		
//	}
	
	
	
	@PostMapping("/guardar")
	public SsptCuentaCobro getCuentaCobro(@RequestBody CuentaCobroGenerar cu) {
		
		//Pattern pat= Pattern.compile("^[0-9]{4}-{1}[0-9]{2}-{1}[0-9]{2}$");
		//Matcher mat=pat.matcher(cu.getFecha().to);
		//if(!mat.matches()) {
		//	return null;
		//}
		
		SsptCuentaCobro cuenta= new SsptCuentaCobro();
		//String [] f = fecha.split("-");
		int mes = cu.getFecha().getMonth();
		int año=  cu.getFecha().getYear();
		Date fechaInicio= new Date(año, mes, 01);
		Date fechaFinal= new Date(año, mes, 29);
		System.out.print(fechaInicio.toString());
		
		cuenta.setTotal(repo.findByAsesor(cu.getId_asesor(), fechaInicio, fechaFinal)*12000);
		cuenta.setAsesor(service.buscarPorId(cu.getId_asesor()));
		
		
		return repo.save(cuenta);
		
		
		
	}

}

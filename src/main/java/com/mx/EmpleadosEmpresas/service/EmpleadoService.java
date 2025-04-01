package com.mx.EmpleadosEmpresas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mx.EmpleadosEmpresas.dao.EmpleadoDao;
import com.mx.EmpleadosEmpresas.dtos.Respuesta;
import com.mx.EmpleadosEmpresas.entidad.Empleado;
import com.mx.EmpleadosEmpresas.entidad.Empresa;

public class EmpleadoService implements MetodosEmpleado {
	@Autowired
	EmpleadoDao dao;
	
	
	@Override
	public Respuesta guardar(Empleado empleado) {
		Respuesta rs = new Respuesta();
		if(dao.existsById(empleado.getCurp())) {
			rs.setMensaje("El empleado no se registro porque ya existe su curp");
			rs.setSuccess(false);
			rs.setObj(empleado.getCurp());
			return rs;
		}
		for(Empleado e: dao.findAll()) {
			if(empleado.getNombre().equalsIgnoreCase(e.getNombre()) && empleado.getApellido().equalsIgnoreCase(e.getApellido())) {
				rs.setMensaje("El empleado no se registro porque ya existe");
				rs.setSuccess(false);
				rs.setObj(e);
				return rs;
			}
		}
		empleado = convertirMayuscula(empleado);
		dao.save(empleado);
		rs.setMensaje("El empleado ha sido agregado a la base de datos");
		rs.setSuccess(true);
		rs.setObj(empleado);
		return rs;
	}

	@Override
	public Respuesta editar(Empleado empleado) {
		Respuesta rs = new Respuesta();
		if(dao.existsById(empleado.getCurp())) {
			dao.save(empleado);
			rs.setMensaje("El empleado ha sido editado");
			rs.setSuccess(true);
			rs.setObj(empleado);
			return rs;
		}
		rs.setMensaje("El empleado que tratas de editar no existe");
		rs.setSuccess(false);
		rs.setObj(empleado.getCurp());
		return null;
	}

	@Override
	public Respuesta eliminar(Empleado empleado) {
		Respuesta rs = new Respuesta();
		String curp = empleado.getCurp();
		empleado = dao.findById(empleado.getCurp().toUpperCase()).orElse(null);
		if(empleado == null) {
			rs.setMensaje("El empleado que tratas de eliminar no existe");
			rs.setSuccess(false);
			rs.setObj(curp);
			return rs;
		}
		if(empleado.getEmpresa() != null) {
			empleado.getEmpresa().getEmpleados().remove(empleado);
			empleado.setEmpresa(null);
		}
		rs.setObj(empleado);
		dao.delete(empleado);
		rs.setMensaje("El empleado ha sido eliminado");
		rs.setSuccess(true);
		return rs;
	}

	@Override
	public Respuesta buscar(String curp) {
		Respuesta rs = new Respuesta();
		Empleado empleado = dao.findById(curp).orElse(null);
		if(empleado == null) {
			rs.setMensaje("El empleado que tratas de buscar no exixte");
			rs.setSuccess(false);
			rs.setObj(null);
		}else {
			rs.setMensaje("El empleado ha sido encontrado");
			rs.setSuccess(true);
			rs.setObj(empleado);
		}
		return rs;
	}

	@Override
	public List<Empleado> mostrar() {
		return dao.findAll();
	}
	
	public Empleado convertirMayuscula(Empleado empleado) {
		empleado.setCurp(empleado.getCurp().toUpperCase());
		empleado.setNombre(empleado.getNombre().toUpperCase());
		empleado.setApellido(empleado.getApellido().toUpperCase());
		empleado.setGenero(empleado.getGenero().toUpperCase());
		empleado.setEstadoCivil(empleado.getEstadoCivil().toUpperCase());
		empleado.setCiudad(empleado.getCiudad().toUpperCase());
		return empleado;
	}

}

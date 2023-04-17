package com.pruebaJUnitMockito.service;

import com.pruebaJUnitMockito.domain.Empleado;
import com.pruebaJUnitMockito.repository.EmpleadoRepository;

import java.util.List;
import java.util.Optional;

public class EmpleadoServiceImpl implements EmpleadoService{

    private EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository){
        this.empleadoRepository=empleadoRepository;
    }

    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
        Optional<Empleado> empleadoGuardado = empleadoRepository.findByEmail(empleado.getEmail());
        if(empleadoGuardado.isPresent())
            throw new RuntimeException("Ya existe un empleado con este email : "+empleado.getEmail());
        return empleadoRepository.save(empleado);
    }

    @Override
    public List<Empleado> buscarEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> buscarEmpleadoPorId(long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado actualizarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminarEmpleado(long id) {
        empleadoRepository.deleteById(id);
    }
}

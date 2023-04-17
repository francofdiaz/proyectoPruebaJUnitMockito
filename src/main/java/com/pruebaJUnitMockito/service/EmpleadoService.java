package com.pruebaJUnitMockito.service;

import com.pruebaJUnitMockito.domain.Empleado;

public interface EmpleadoService {
    Empleado guardarEmpleado(Empleado empleado);
    java.util.List<Empleado> buscarEmpleados();
    java.util.Optional<Empleado> buscarEmpleadoPorId(long id);
    Empleado actualizarEmpleado(Empleado empleado);
    void eliminarEmpleado(long id);
}

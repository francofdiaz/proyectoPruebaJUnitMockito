package com.pruebaJUnitMockito.repository;

import com.pruebaJUnitMockito.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    java.util.Optional<Empleado> findByEmail(String email);

}

package com.pruebaJUnitMockito.service;

import com.pruebaJUnitMockito.repository.EmpleadoRepository;
import com.pruebaJUnitMockito.domain.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class EmpleadoServiceTest {

    //Se creaa un empleadoRepository(@Mock) y se lo inyecta en empleadoServiceImpl(@InjectMocks)
    @Mock
    private EmpleadoRepository empleadoRepository;
    @InjectMocks
    private EmpleadoServiceImpl empleadoServiceImpl;
    private Empleado empleado;
    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        empleado = Empleado.builder()
                    .id(1L)
                    .nombres("Raul")
                    .apellidos("Fernandez")
                    .email("raulfernandez@gmail.com")
                    .build();
    }

    @DisplayName("Prueba de JUnit para el metodo guardarEmpleado.")
    @Test
    public void dadoUnObjetoEmpleado_cuandoSeLoGuarde_debeRetornarElObjetoEmpleado(){
    //public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given - precondicion o configuracion
        given(empleadoRepository.findByEmail(empleado.getEmail()))
        //The empty method of the Optional method is used to get the empty instance of the Optional class.
        //The returned object doesn't have any value.
                .willReturn(Optional.empty());
        //Se especifica el comportamiento que debe tener el metodo que se invocara en el repository
        given(empleadoRepository.save(empleado)).willReturn(empleado);

        System.out.println("Este es el objeto empleadoRepository : "+empleadoRepository);
        System.out.println("Este es el objeto empleadoServiceImpl : "+empleadoServiceImpl);

        // when -  la accion o el comportamiento que vamos a testear
        Empleado empleadoGuardado = empleadoServiceImpl.guardarEmpleado(empleado);

        System.out.println("Este es el objeto empleadoGuardado : "+empleadoGuardado);
        // then - verificar la salida
        //assertThat(empleadoGuardado).isInstanceOf(Empleado.class);
        //Alternativamente
        //assertThat(empleadoGuardado).isNotNull();
        //O
        assertThat(empleadoGuardado).isEqualTo(empleado);
    }

    @DisplayName("Prueba JUnit el metodo guardarEmpleado, el cual debe mandar una exception.")
    @Test
    public void dadoUnEmailExistente_cuandoSeVaAGuardarUnEmpleado_debeLanzarUnaException(){
    //public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondicion o configuracion
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                //Optional tiene un constructor privado, y nos proporciona tres métodos factoría estáticos
                //para instanciar la clase. Siendo el método .of el que nos permite recubrir cualquier objeto
                //en un optional
                .willReturn(Optional.of(empleado));

        System.out.println("Este es el objeto empleadoRepository : "+empleadoRepository);
        System.out.println("Este es el objeto empleadoServiceImpl : "+empleadoServiceImpl);

        // when -  la accion o el comportamiento que vamos a testear
        //En este caso debe lanzar una exception porque ya existe un empleado con ese email
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            empleadoServiceImpl.guardarEmpleado(empleado);
        });

        // then
        //Se verifica que el metodo save de empleadoRepository nunca es llamado debido a que se
        //lanzo una exception
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @DisplayName("Prueba JUnit para el metodo buscarEmpleados.")
    @Test
    public void dadaUnaListaDeEmpleados_cuandoSeObtienenTodosLosEmpleados_seDevuelveLaListaDeEmpleados(){
    //public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        // given - precondicion o configuracion
        Empleado empleado1 = Empleado.builder()
                .id(2L)
                .nombres("Tony")
                .apellidos("Stark")
                .email("tony@gmail.com")
                .build();
        Empleado empleado2 = Empleado.builder()
                .id(3L)
                .nombres("Marcos")
                .apellidos("Aurelio")
                .email("marcosaurelio@gmail.com")
                .build();

        given(empleadoRepository.findAll()).willReturn(java.util.List.of(empleado,empleado1,empleado2));

        // when -  la accion o el comportamiento que vamos a testear
        java.util.List<Empleado> listaDeEmpleados = empleadoServiceImpl.buscarEmpleados();

        // then - verificar la salida
        assertThat(listaDeEmpleados).isNotNull();
        assertThat(listaDeEmpleados.size()).isEqualTo(3);
    }

    @DisplayName("Prueba JUnit para el metodo buscarEmpleados (escenario negativo).")
    @Test
    public void dadaUnaListaDeEmpleados_cuandoSeConsultaPorTodosLosEmpleados_seDevuelveLaListaDeEmpleadosVacia(){
    //public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        // given - precondition or setup

        /*Empleado employee1 = Empleado.builder()
                .id(2L)
                .nombres("Tony")
                .apellidos("Stark")
                .email("tony@gmail.com")
                .build();*/

        given(empleadoRepository.findAll()).willReturn(java.util.Collections.emptyList());

        // when -  action or the behaviour that we are going test
        java.util.List<Empleado> listaDeEmpleados = empleadoServiceImpl.buscarEmpleados();

        // then - verify the output
        assertThat(listaDeEmpleados).isEmpty();
        assertThat(listaDeEmpleados.size()).isEqualTo(0);
    }

    @DisplayName("Prueba JUnit para el metodo buscarEmpleadoPorId.")
    @Test
    public void dadoUnId_buscarElEmpleado(){
        long empleadoId = 3L;
        given(empleadoRepository.findById(empleadoId))
        //The empty method of the Optional method is used to get the empty instance of the Optional class.
        //The returned object doesn't have any value.
                .willReturn(Optional.of(empleado));
        //Se especifica el comportamiento que debe tener el metodo que se invocara en el repository
        //given(empleadoRepository.findById(empleadoId)).willReturn(empleado);

        System.out.println("Este es el objeto empleadoRepository : "+empleadoRepository);
        System.out.println("Este es el objeto empleadoServiceImpl : "+empleadoServiceImpl);

        // when -  la accion o el comportamiento que vamos a testear
        Optional empleadoGuardado = empleadoServiceImpl.buscarEmpleadoPorId(empleadoId);

        if(empleadoGuardado.isPresent()){
            System.out.println("Este es el objeto empleadoGuardado : " + empleadoGuardado.get());
            // then - verificar la salida
            assertThat(empleadoGuardado.get()).isInstanceOf(Empleado.class);
        }
        //Alternativamente
        assertThat(empleadoGuardado.isPresent()).isTrue();
        //O
        //assertThat(empleadoGuardado).isEqualTo(empleado);
    }

    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void dadoUnObjetoEmpleado_cuandoSeLoActualiza_retornarElObjetoActualizado(){
    //public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        //Se especifica el comportamiento que debe tener el metodo que se invocara en el repository
        //given(empleadoRepository.findById(empleadoId)).willReturn(empleado);
        Empleado empleado1 = Empleado.builder()
                                        .id(2L)
                                        .nombres("Tony")
                                        .apellidos("Stark")
                                        .email("tony@gmail.com")
                                        .build();
        given(empleadoRepository.save(empleado1)).willReturn(empleado1);
        System.out.println("Este es el objeto empleadoGuardado : "+empleado1.getNombres());
        empleado1.setNombres("Pedro Martin");
        empleado1.setApellidos("Pedroza");

        // when -  la accion o el comportamiento que vamos a testear
        Empleado empleadoGuardado = empleadoServiceImpl.actualizarEmpleado(empleado1);

        System.out.println("Este es el objeto empleadoGuardado : "+empleadoGuardado.getNombres());
        // then - verificar la salida
        assertThat(empleadoGuardado).isInstanceOf(Empleado.class);
        //Alternativamente
        assertThat(empleadoGuardado).isNotNull();
        //O
        assertThat(empleadoGuardado).isEqualTo(empleado1);
    }

    @DisplayName("Prueba JUnit el metodo eliminarEmpleado.")
    @Test
    public void dadoUnId_seDebeEliminarElEmpleado(){
        // given - precondicion o configuracion
        System.out.println("Este es el objeto empleadoRepository : "+empleadoRepository);
        System.out.println("Este es el objeto empleadoServiceImpl : "+empleadoServiceImpl);
        long empleadoId = 1L;
        //Se especifica el comportamiento que debe tener el metodo que se invocara en el repository
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);

        // when -  la accion o el comportamiento que vamos a testear
        empleadoServiceImpl.eliminarEmpleado(empleadoId);

        // then
        //Se verifica que el metodo deleteById de empleadoRepository es llamado una sola vez
        verify(empleadoRepository,times(1)).deleteById(empleadoId);
    }

}
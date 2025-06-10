package edu.pucmm;


import edu.pucmm.exception.DuplicateEmployeeException;
import edu.pucmm.exception.EmployeeNotFoundException;
import edu.pucmm.exception.InvalidSalaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author me@fredpena.dev
 * @created 02/06/2024  - 00:47
 */

public class EmployeeManagerTest {

    private EmployeeManager employeeManager;
    private Position juniorDeveloper;
    private Position seniorDeveloper;
    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setUp() {
        employeeManager = new EmployeeManager();
        juniorDeveloper = new Position("1", "Junior Developer", 30000, 50000);
        seniorDeveloper = new Position("2", "Senior Developer", 60000, 90000);
        employee1 = new Employee("1", "John Doe", juniorDeveloper, 40000);
        employee2 = new Employee("2", "Jane Smith", seniorDeveloper, 70000);
        employeeManager.addEmployee(employee1);
    }

    @Test
    public void testAddEmployee() {
        // TODO: Agregar employee2 al employeeManager y verificar que se agregó correctamente.
        employeeManager.addEmployee(employee2);

        // - Verificar que el número total de empleados ahora es 2.
        assertEquals(2, employeeManager.getEmployees().size(), "Total de empleados debe ser 2");

        // - Verificar que employee2 está en la lista de empleados.
        assertTrue(employeeManager.getEmployees().contains(employee2), "Employee2 debe estar en la lista de empleados");

    }

    @Test
    public void testRemoveEmployee() {
        // TODO: Eliminar employee1 del employeeManager y verificar que se eliminó correctamente.
        // - Agregar employee2 al employeeManager.
        employeeManager.addEmployee(employee2);

        // - Eliminar employee1 del employeeManager.
        employeeManager.removeEmployee(employee1);

        // - Verificar que el número total de empleados ahora es 1.
        assertEquals(1, employeeManager.getEmployees().size(), "Total de empleados debe ser 1");

        // - Verificar que employee1 ya no está en la lista de empleados.
        assertFalse(employeeManager.getEmployees().contains(employee1), "Employee1 no debe estar en la lista de empleados");
    }

    @Test
    public void testCalculateTotalSalary() {
        // TODO: Agregar employee2 al employeeManager y verificar el cálculo del salario total.
        // - Agregar employee2 al employeeManager.
        employeeManager.addEmployee(employee2);

        // - Verificar que el salario total es la suma de los salarios de employee1 y employee2.
        double expectedSalary = employee1.getSalary() + employee2.getSalary();
        assertEquals(expectedSalary, employeeManager.calculateTotalSalary(), "El salario total debe ser " + expectedSalary);
    }

    @Test
    public void testUpdateEmployeeSalaryValid() {
        // TODO: Actualizar el salario de employee1 a una cantidad válida y verificar la actualización.
        // - Actualizar el salario de employee1 a 45000.
        employeeManager.updateEmployeeSalary(employee1, 45000);

        // - Verificar que el salario de employee1 ahora es 45000.
        assertEquals(45000, employee1.getSalary(), "El salario de employee1 debe ser 45000");
    }

    @Test
    public void testUpdateEmployeeSalaryInvalid() {
        // TODO: Intentar actualizar el salario de employee1 a una cantidad inválida y verificar la excepción.
        // - Intentar actualizar el salario de employee1 a 60000 (que está fuera del rango para Junior Developer).
        // - Verificar que se lanza una InvalidSalaryException.
        assertThrows(InvalidSalaryException.class, () -> employeeManager.updateEmployeeSalary(employee1, 60000),
                "Se debe lanzar una InvalidSalaryException al intentar actualizar el salario de employee2 a 60000");
    }

    @Test
    public void testUpdateEmployeeSalaryEmployeeNotFound() {
        // TODO: Intentar actualizar el salario de employee2 (no agregado al manager) y verificar la excepción.
        // - Intentar actualizar el salario de employee2 a 70000.
        // - Verificar que se lanza una EmployeeNotFoundException.
        assertThrows(EmployeeNotFoundException.class, () -> employeeManager.updateEmployeeSalary(employee2, 70000),
                "Se debe lanzar una EmployeeNotFoundException al intentar actualizar el salario de employee2 no agregado al manager");
    }

    @Test
    public void testUpdateEmployeePositionValid() {
        // TODO: Actualizar la posición de employee2 a una posición válida y verificar la actualización.
        // - Agregar employee2 al employeeManager.
        employeeManager.addEmployee(employee2);

        // - Actualizar la posición de employee2 a seniorDeveloper.
        employeeManager.updateEmployeePosition(employee2, seniorDeveloper);

        // - Verificar que la posición de employee2 ahora es seniorDeveloper.
        assertEquals(seniorDeveloper, employee2.getPosition(), "La posición de employee2 debe ser seniorDeveloper");
    }

    @Test
    public void testUpdateEmployeePositionInvalidDueToSalary() {
        // TODO: Intentar actualizar la posición de employee1 a seniorDeveloper y verificar la excepción.
        // - Intentar actualizar la posición de employee1 a seniorDeveloper.
        // - Verificar que se lanza una InvalidSalaryException porque el salario de employee1 no está dentro del rango para Senior Developer.
        assertThrows(InvalidSalaryException.class, () -> employeeManager.updateEmployeePosition(employee1, seniorDeveloper),
                "Se debe lanzar una InvalidSalaryException al intentar actualizar la posición de employee1 a seniorDeveloper con un salario no válido");
    }

    @Test
    public void testUpdateEmployeePositionEmployeeNotFound() {
        // TODO: Intentar actualizar la posición de employee2 (no agregado al manager) y verificar la excepción.
        // - Intentar actualizar la posición de employee2 a juniorDeveloper.
        // - Verificar que se lanza una EmployeeNotFoundException.
        assertThrows(EmployeeNotFoundException.class, () -> employeeManager.updateEmployeePosition(employee2, juniorDeveloper),
                "Se debe lanzar una EmployeeNotFoundException al intentar actualizar la posición de employee2 no agregado al manager");
    }

    static Stream<Arguments> baseSalaryArguments() {
        Position juniorDeveloper = new Position("1", "Junior Developer", 30000, 50000);
        Position seniorDeveloper = new Position("2", "Senior Developer", 60000, 90000);
        return Stream.of(
                Arguments.of(juniorDeveloper, 40000, true),
                Arguments.of(juniorDeveloper, 60000, false),
                Arguments.of(seniorDeveloper, 70000, true),
                Arguments.of(seniorDeveloper, 50000, false)
        );
    }

    @ParameterizedTest(name = "{index} => Position: {0}, Salary: {1}")
    @MethodSource("baseSalaryArguments")
    public void testIsSalaryValidForPosition(Position position, double salary, boolean expectedValid) {
        EmployeeManager manager = new EmployeeManager();
        assertEquals(expectedValid, manager.isSalaryValidForPosition(position, salary),
                "Salario " + salary + " para la posición " + position.getName() + " debe ser " + (expectedValid ? "válido" : "inválido"));
    }

    @Test
    public void testAddEmployeeWithInvalidSalary() {
        // TODO: Intentar agregar empleados con salarios inválidos y verificar las excepciones.
        // - Crear un empleado con un salario de 60000 para juniorDeveloper.
        Employee employee3 = new Employee("3", "Vladimir", juniorDeveloper, 60000);

        // - Verificar que se lanza una InvalidSalaryException al agregar este empleado.
        assertThrows(InvalidSalaryException.class, () -> employeeManager.addEmployee(employee3),
                "Se debe lanzar una InvalidSalaryException al intentar agregar un empleado con un salario no válido para juniorDeveloper");

        // - Crear otro empleado con un salario de 40000 para seniorDeveloper.
        Employee employee4 = new Employee("4", "Osvaldo", seniorDeveloper, 40000);

        // - Verificar que se lanza una InvalidSalaryException al agregar este empleado.
        assertThrows(InvalidSalaryException.class, () -> employeeManager.addEmployee(employee4),
                "Se debe lanzar una InvalidSalaryException al intentar agregar un empleado con un salario no válido para seniorDeveloper");
    }

    @Test
    public void testRemoveExistentEmployee() {
        // TODO: Eliminar un empleado existente y verificar que no se lanza una excepción.
        // - Eliminar employee1 del employeeManager.
        // - Verificar que no se lanza ninguna excepción.
        employeeManager.removeEmployee(employee1);
    }

    @Test
    public void testRemoveNonExistentEmployee() {
        // TODO: Intentar eliminar un empleado no existente y verificar la excepción.
        // - Intentar eliminar employee2 (no agregado al manager).
        // - Verificar que se lanza una EmployeeNotFoundException.
        assertThrows(EmployeeNotFoundException.class, () -> employeeManager.removeEmployee(employee2),
                "Se debe lanzar una EmployeeNotFoundException al intentar eliminar un empleado no existente");

    }

    @Test
    public void testAddDuplicateEmployee() {
        // TODO: Intentar agregar un empleado duplicado y verificar la excepción.
        // - Intentar agregar employee1 nuevamente al employeeManager.
        // - Verificar que se lanza una DuplicateEmployeeException.
        assertThrows(DuplicateEmployeeException.class, () -> employeeManager.addEmployee(employee1),
                "Se debe lanzar una DuplicateEmployeeException al intentar agregar un empleado duplicado");
    }
}

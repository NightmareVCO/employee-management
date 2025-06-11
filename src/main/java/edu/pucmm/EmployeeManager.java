package edu.pucmm;

import edu.pucmm.exception.DuplicateEmployeeException;
import edu.pucmm.exception.EmployeeNotFoundException;
import edu.pucmm.exception.InvalidSalaryException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 01/06/2024  - 23:43
 */
public class EmployeeManager {

    private final List<Employee> employees;

    public EmployeeManager() {
        this.employees = new ArrayList<>();
    }

//    No se permite agregar empleados con salarios por debajo del 10% del salario mínimo de su posición.
    public void addEmployee(Employee employee) {
        if (employees.contains(employee)) {
            throw new DuplicateEmployeeException("Duplicate employee");
        }
        //        No puede haber dos empleados con el mismo ID o nombre.
        if(employeeExistsById(employee.getId())) {
            throw new DuplicateEmployeeException("Employee with ID already exists");
        }
        //        No puede haber dos empleados con el mismo ID o nombre.
        if(employeeExistsByName(employee.getName())) {
            throw new DuplicateEmployeeException("Employee with name already exists");
        }

        if (!isSalaryValidForPosition(employee.getPosition(), employee.getSalary())) {
            throw new InvalidSalaryException("Invalid salary for position");
        }
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        employees.remove(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public double calculateTotalSalary() {
        double totalSalary = 0;
        for (Employee employee : employees) {
            totalSalary += employee.getSalary();
        }
        return totalSalary;
    }

    public void updateEmployeeSalary(Employee employee, double newSalary) {
        if (!employees.contains(employee)) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        if (!isSalaryValidForPosition(employee.getPosition(), newSalary)) {
            throw new InvalidSalaryException("Salary is not within the range for the position");
        }
        employee.setSalary(newSalary);
    }

    public void updateEmployeePosition(Employee employee, Position newPosition) {
        if (!employees.contains(employee)) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        if (!isSalaryValidForPosition(newPosition, employee.getSalary())) {
            throw new InvalidSalaryException("Current salary is not within the range for the new position");
        }
        employee.setPosition(newPosition);
    }

    // El método isSalaryValidForPosition(...) debe retornar false si:
    // Si el salario está fuera de rango pero dentro del 10% inferior del mínimo permitido, se debe ajustar automáticamente al salario mínimo de la nueva posición.
    public boolean isSalaryValidForPosition(Position position, double salary) {
        if (salary < 0 || position == null) return false;

        double min = position.getMinSalary();
        double max = position.getMaxSalary();

        if (salary >= min && salary <= max) {
            return true;
        }

        // <10% del mínimo permitido
        return salary >= min * 0.9 && salary < min;

        // >10% del máximo permitido
    }

    public boolean employeeExistsById(String id) {
        employees.stream().anyMatch(employee -> employee.getId().equalsIgnoreCase(id));
        return false;
    }

    public boolean employeeExistsByName(String name) {
        return employees.stream().anyMatch(employee -> employee.getName().equalsIgnoreCase(name));
    }
}

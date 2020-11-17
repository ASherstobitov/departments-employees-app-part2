package com.alexey.demo.service.impl;

import com.alexey.demo.entity.Department;
import com.alexey.demo.entity.Employee;
import com.alexey.demo.exception.ResourceNotFoundException;
import com.alexey.demo.repository.DepartmentRepository;
import com.alexey.demo.repository.EmployeeRepository;
import com.alexey.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveOrUpdate(Employee employee) {
        Long id = employee.getDepartment().getId();
        Department theDepart = getDepartment(id);
        Employee theEmployee = null;
        if (employee.getId() != null) {
            theEmployee = getEmployee(employee.getId());
            theEmployee.setFirstName(employee.getFirstName());
            theEmployee.setLastName(employee.getLastName());
            theEmployee.setBirthday(employee.getBirthday());
            theEmployee.setSalary(employee.getSalary());
        } else {
            theEmployee = employee;
        }
        theEmployee.setDepartment(theDepart);
        return employeeRepository.save(theEmployee);
    }

    public Department getDepartment(Long id) {

        Optional<Department> optional = departmentRepository.findById(id);
        Department department = null;
        if (optional.isPresent()) {
            department = optional.get();
            return department;
        } else
            throw new ResourceNotFoundException("Department don't find by id: " + id);
    }

    @Override
    public Long deleteEmployee(Long id) {
        getEmployee(id).getDepartment().getId();
        Long departId = getEmployee(id).getDepartment().getId();
        employeeRepository.deleteById(id);
        return departId;
    }

    @Override
    public List<Employee> getAllEmployeesByDepartment(Long id) {
        List<Employee> allEmployees = employeeRepository.findAll();
        return allEmployees.stream()
                .filter(em -> em.getDepartment().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployee(Long id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        Employee employee = null;
        if (optional.isPresent()) {
            employee = optional.get();
        } else {
            throw new ResourceNotFoundException("Employee don't find by id: " + id);
        }
        return employee;
    }

    @Override
    public List<Employee> getAllEmployeesWithDateBirthBetween(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.getAllByBirthdayBetween(startDate, endDate);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}

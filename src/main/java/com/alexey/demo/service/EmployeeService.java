package com.alexey.demo.service;

import com.alexey.demo.entity.Employee;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    Employee saveOrUpdate(Employee employee);

    Long deleteEmployee(Long id);

    List<Employee> getAllEmployeesByDepartment(Long id);

    Employee getEmployee(Long id);

    List<Employee> getAllEmployeesWithDateBirthBetween(LocalDate startDate, LocalDate endDate);

    List<Employee> getAllEmployees();

}

package com.alexey.demo.service.impl;

import com.alexey.demo.entity.Department;
import com.alexey.demo.entity.Employee;
import com.alexey.demo.exception.ResourceNotFoundException;
import com.alexey.demo.repository.DepartmentRepository;
import com.alexey.demo.service.DepartmentService;
import com.alexey.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeService employeeService;

    @Override
    public Department saveOrUpdate(Department department) {
        Department theDepart = null;
        if (department.getId() != null) {
            theDepart = getDepartment(department.getId());
            theDepart.setDepartName(department.getDepartName());
            theDepart.setEmployees(employeeService.getAllEmployeesByDepartment(department.getId()));
            theDepart.setAmountEmployees(countEmployees(department.getId()));
            theDepart.setAverageSalary(setAverageSalaryById(department.getId()));
        } else {
            theDepart = department;
        }
        return departmentRepository.save(theDepart);
    }

    private String setAverageSalaryById(Long id) {
        List<Employee> employees = employeeService.getAllEmployeesByDepartment(id);
        double salary = countAverageSalary(employees);
        return new DecimalFormat("###,###.##").format(salary);
    }

    private int countEmployees(Long id) {
        return employeeService.getAllEmployeesByDepartment(id).size();
    }

    @Override
    public List<Department> getDepartments() {
        List<Department> departsList = new ArrayList<>();
        for (Department department : departmentRepository.findAll()) {
            department.setAverageSalary(setAverageSalaryById(department.getId()));
            department.setAmountEmployees(countEmployees(department.getId()));
            departsList.add(department);
        }
        return departsList;
    }

    @Override
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
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }

    public Double countAverageSalary(List<Employee> employees) {
        DoubleSummaryStatistics stats = employees.stream()
                .mapToDouble((x) -> x.getSalary())
                .summaryStatistics();
        return stats.getAverage();
    }

}

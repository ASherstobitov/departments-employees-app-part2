package com.alexey.demo.controller;

import com.alexey.demo.entity.Employee;
import com.alexey.demo.hellper.DateSearch;
import com.alexey.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Employee employee = employeeService.getEmployee(id);
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @PostMapping("/saveEmployee")
    public String updateEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.saveOrUpdate(employee);
        Long departId = employee.getDepartment().getId();
        return "redirect:/api/employee/showEmployees/" + departId;
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") Long id) {
        Long departId = employeeService.deleteEmployee(id);
        return "redirect:/api/employee/showEmployees/" + departId;
    }

    @GetMapping("/newDateForm")
    public String newDateForm(Model model) {
        DateSearch dateSearch = new DateSearch();
        model.addAttribute("dateSearch", dateSearch);
        return "date_search";
    }

    @PostMapping("/birthday-between")
    public String getEmployeesByDateBirthBetween(@ModelAttribute(value = "dateSearch")
                                                         DateSearch dateSearch, Model model) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(dateSearch.getStartDate().toString(), dateTimeFormatter);
        LocalDate end = LocalDate.parse(dateSearch.getEndDate().toString(), dateTimeFormatter);
        List<Employee> employees = employeeService.getAllEmployeesWithDateBirthBetween(start, end);
        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/showEmployees/{id}")
    public String getEmployeesByDepartment(@PathVariable(value = "id") Long id, Model model) {
        List<Employee> employees = employeeService.getAllEmployeesByDepartment(id);
        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/newEmployeeForm")
    public String newEmployeeForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    @GetMapping
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }
}
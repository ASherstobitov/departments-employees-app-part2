package com.alexey.demo.controller;

import com.alexey.demo.entity.Department;
import com.alexey.demo.entity.Employee;
import com.alexey.demo.service.DepartmentService;
import com.alexey.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    private final EmployeeService employeeService;

    @GetMapping
    public String getDepartments(Model model) {
        List<Department> departments = departmentService.getDepartments();
        model.addAttribute("departs", departments);
        return "departments";
    }

    @GetMapping("/{id}")
    public Department getDepartment(@PathVariable Long id) {
        return departmentService.getDepartment(id);
    }

    @GetMapping("/newDepartmentForm")
    public String newDepartmentForm(Model model) {
        Department department = new Department();
        model.addAttribute("department", department);
        return "new_department";
    }

    @PostMapping("/saveDepartment")
    public String createDepartment(@ModelAttribute("department") Department department) {
        departmentService.saveOrUpdate(department);
        return "redirect:/api/department";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Department department = departmentService.getDepartment(id);
        model.addAttribute("department", department);
        return "update_department";
    }

    @GetMapping("/deleteDepartment/{id}")
    public String deleteDepartment(@PathVariable(value = "id") Long id) {
        departmentService.delete(id);
        return "redirect:/api/department";
    }

    @GetMapping("/showEmployees/{id}")
    public String getEmployeesByDepartment(@PathVariable(value = "id") Long id, Model model) {
        List<Employee> employees = employeeService.getAllEmployeesByDepartment(id);
        model.addAttribute("employees", employees);
        return "employees";
    }
}
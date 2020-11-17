package com.alexey.demo.service;

import com.alexey.demo.entity.Department;
import java.util.List;

public interface DepartmentService {

    Department saveOrUpdate(Department department);

    List<Department> getDepartments();

    Department getDepartment(Long id);

    void delete(Long id);

}

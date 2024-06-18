package com.example.cleanapi.modules.getemployee.data

import com.example.cleanapi.core.network.EmployeeService
import javax.inject.Inject


interface EmployeeRepository {
    suspend fun getEmployee(id: String): Employee
    suspend fun getEmployees(page:Int, pageSize:Int): Employees
}

class EmployeeRepositoryImpl @Inject constructor(private var employeeService: EmployeeService) :
    EmployeeRepository {

    override suspend fun getEmployee(id: String): Employee {
        return employeeService.getEmployee(id)
    }

    override suspend fun getEmployees(page: Int, pageSize: Int): Employees {
        val skip = (page - 1) * pageSize
        return employeeService.getEmployees(limit = pageSize, skip = skip)
    }

}
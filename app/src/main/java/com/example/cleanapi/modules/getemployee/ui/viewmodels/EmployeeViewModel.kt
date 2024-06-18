package com.example.cleanapi.modules.getemployee.ui.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.cleanapi.modules.getemployee.data.Employee
import com.example.cleanapi.modules.getemployee.data.EmployeeRepository
import com.example.cleanapi.modules.getemployee.ui.paging.EmployeesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(private val employeeRepository: EmployeeRepository) :
    ViewModel() {

    private val _employeeFlow = MutableSharedFlow<Employee>()
    val employeeFlow = _employeeFlow.asSharedFlow()

    fun getEmployee(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val employee = employeeRepository.getEmployee(id)
            _employeeFlow.emit(employee)
        }
    }

    fun getEmployeesPagingData(): Flow<PagingData<Employee>> {

        return Pager(
            PagingConfig(pageSize = 5, enablePlaceholders = true, prefetchDistance = 5),
            pagingSourceFactory = { EmployeesPagingSource(employeeRepository) })
            .flow.cachedIn(viewModelScope)
    }




}


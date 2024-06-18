package com.example.cleanapi.modules.getemployee.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cleanapi.modules.getemployee.data.Employee
import com.example.cleanapi.modules.getemployee.data.EmployeeRepository
import retrofit2.HttpException
import java.io.IOException

class EmployeesPagingSource(private val employeeRepository: EmployeeRepository) :PagingSource<Int, Employee>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Employee> {
        return try {
            val page = params.key?:1
            val pageSize =params.loadSize

            val response = employeeRepository.getEmployees(page, pageSize)

            val employees = response.users
            val total = response.total
            val skip = response.skip
            val limit = response.limit

            val prevKey = if (skip > 0) page - 1 else null
            val nextKey = if (skip + limit < total) page + 1 else null

            LoadResult.Page(data = employees, prevKey = prevKey, nextKey = nextKey)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
        catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Employee>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
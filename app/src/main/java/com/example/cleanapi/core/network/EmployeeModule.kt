package com.example.cleanapi.core.network

import com.example.cleanapi.modules.getemployee.data.EmployeeRepository
import com.example.cleanapi.modules.getemployee.data.EmployeeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EmployeeModule {

    @Binds
    abstract fun provideEmployeeRepo(impl: EmployeeRepositoryImpl) : EmployeeRepository
}
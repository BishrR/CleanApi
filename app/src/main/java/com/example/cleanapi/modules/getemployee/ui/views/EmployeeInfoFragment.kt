		

A
D
C
D
D
C
C

A
A
B
package com.example.cleanapi.modules.getemployee.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cleanapi.databinding.FragmentEmployeeInfoBinding
import com.example.cleanapi.modules.getemployee.data.State
import com.example.cleanapi.modules.getemployee.ui.viewmodels.EmployeeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmployeeInfoFragment : Fragment() {

    private lateinit var binding: FragmentEmployeeInfoBinding
    private val viewModel: EmployeeViewModel by viewModels()
    private var employeeID: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        employeeID = arguments?.getInt("employee_id")
        collectEmployeeFlow()
        callGetEmployeeInfoApi()
    }

    private fun callGetEmployeeInfoApi(showShowLoader: Boolean = true) {
        if (showShowLoader) setState(State.Loading)
        viewModel.getEmployee(employeeID.toString())
    }

    private fun collectEmployeeFlow() {
        lifecycleScope.launch {
            viewModel.employeeFlow.collect {
                binding.employeeName.text = "Name : ${it.firstName} ${it.lastName}"
                binding.employeeAge.text = "Age : ${it.age}"
                binding.employeeAddress.text = "Address : ${it.address.city}"
                setImage(it.image, binding.employeePicture)
                setState(State.Success)
            }
        }
    }

    private fun setImage(url: String, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

    private fun setState(state: State) {
        when (state) {
            is State.Loading -> {
                binding.loadingImage.visibility = View.VISIBLE
            }

            is State.Success -> {
                binding.loadingImage.visibility = View.GONE
            }
        }
    }
}

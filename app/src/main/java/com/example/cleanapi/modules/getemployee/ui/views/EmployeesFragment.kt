package com.example.cleanapi.modules.getemployee.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleanapi.databinding.FragmentEmployeesBinding
import com.example.cleanapi.modules.getemployee.ui.adapters.EmployeeAdapter
import com.example.cleanapi.modules.getemployee.ui.viewmodels.EmployeeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EmployeesFragment : Fragment() {

    private lateinit var binding: FragmentEmployeesBinding
    private val viewModel: EmployeeViewModel by viewModels()
    private val adapter: EmployeeAdapter by lazy {
        EmployeeAdapter { position ->
            onItemClick(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeEmployeeFlow()
    }

    private fun setUpRecyclerView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EmployeesFragment.adapter
        }
    }

    private fun observeEmployeeFlow() {
        lifecycleScope.launch {
            viewModel.getEmployeesPagingData().collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
//            adapter.loadStateFlow.collectLatest { loadState ->
//                val isLoading =
//                    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
//                val isError =
//                    loadState.refresh is LoadState.Error || loadState.append is LoadState.Error
//                binding.loadingProgressBar.isVisible = isLoading
//
//                if (isError) {
//                    val errorState = loadState.refresh as? LoadState.Error
//                        ?: loadState.append as? LoadState.Error
//                    errorState?.let { error ->
//                        Toast.makeText(
//                            requireContext(),
//                            "Error: ${error.error.localizedMessage}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }

//            adapter.loadStateFlow.collectLatest { loadStates ->
//                binding.loadingProgressBar.isVisible = loadStates.refresh is LoadState.Loading
////                binding.retryButton.isVisible = loadStates.refresh !is LoadState.Loading
//                binding.errorMsg.isVisible = loadStates.refresh is LoadState.Error
//            }
            adapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading ||
                    loadState.append is LoadState.Loading)
                    binding.loadingProgressBar.isVisible = true
                else {
                    binding.loadingProgressBar.isVisible = false
                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Toast.makeText(context, it.error.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun onItemClick(position: Int) {
        val employee = adapter.getItemAtPosition(position)
        employee?.let {
            val navController = findNavController()
            val action = EmployeesFragmentDirections
                .actionGetEmployeesFragmentToEmployeeInfoFragment(it.id)
            navController.navigate(action)
        }
    }

}


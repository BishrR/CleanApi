package com.example.cleanapi.modules.getemployee.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cleanapi.databinding.EmployeeItemBinding
import com.example.cleanapi.modules.getemployee.data.Employee

class EmployeeAdapter(private val onClickListener: (position: Int) -> Unit) :
    PagingDataAdapter<Employee, EmployeeAdapter.EmployeeViewHolder>(EmployeeDiff) {

    inner class EmployeeViewHolder(private val binding: EmployeeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee?) {
            employee?.let {
                binding.employeeName.text = it.firstName
                setImage(it.image, binding.employeePicture)
                binding.root.setOnClickListener { _ ->
                    onClickListener(bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding =
            EmployeeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = getItem(position)
        holder.bind(employee)
    }

    private fun setImage(url: String?, imageView: ImageView) {
        url?.let {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)

        }

    }

    fun getItemAtPosition(position: Int): Employee? {
        return getItem(position)
    }

    object EmployeeDiff : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }

}

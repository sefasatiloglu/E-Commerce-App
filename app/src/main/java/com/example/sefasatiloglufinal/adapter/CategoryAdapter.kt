package com.example.sefasatiloglufinal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sefasatiloglufinal.databinding.CategoryItemBinding
import com.example.sefasatiloglufinal.models.Category

class CategoryAdapter(
    private val mContext: Context,
    private val categoryList: List<Category>,
    private val categoryClick: CategoryClick
) : RecyclerView.Adapter<CategoryAdapter.CategoryNesneTutucu>() {

    inner class CategoryNesneTutucu(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryNesneTutucu {
        val bindingItem = CategoryItemBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CategoryNesneTutucu(bindingItem)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryNesneTutucu, position: Int) {
        val categoryVerileri = categoryList[position]
        holder.binding.category1.text = categoryVerileri.category_name
        holder.binding.categoryImage.setImageResource(categoryVerileri.category_image)

        holder.itemView.setOnClickListener {
            categoryClick.clickCategory(categoryVerileri)
        }

    }

    interface CategoryClick {
        fun clickCategory(category: Category)
    }


}
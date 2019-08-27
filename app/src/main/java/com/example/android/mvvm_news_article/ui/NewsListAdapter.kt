package com.example.android.mvvm_news_article.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mvvm_news_article.databinding.ListItemArticleBinding
import com.example.android.mvvm_news_article.model.Article

class NewsListAdapter : ListAdapter<Article, NewsListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articleItem = getItem(position)
        holder.bind(articleItem)
    }

    class ViewHolder private constructor(val binding: ListItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {
            binding.article = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ListItemArticleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(view)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
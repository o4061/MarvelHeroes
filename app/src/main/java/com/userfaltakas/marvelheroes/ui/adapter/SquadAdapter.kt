package com.userfaltakas.marvelheroes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.userfaltakas.marvelheroes.data.api.Result
import com.userfaltakas.marvelheroes.databinding.HeroSquadItemBinding

class SquadAdapter : RecyclerView.Adapter<SquadAdapter.HeroViewHolder>() {
    inner class HeroViewHolder(val binding: HeroSquadItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(
            HeroSquadItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = differ.currentList[position]
        val binding = holder.binding

        holder.itemView.apply {
            Glide.with(this).load(hero.thumbnail?.path).into(binding.image)
            binding.name.text = hero.name
            setOnClickListener {
                onItemClickListener?.let { it(hero) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
}
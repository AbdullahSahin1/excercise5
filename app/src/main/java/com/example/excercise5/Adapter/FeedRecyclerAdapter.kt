package com.example.excercise5.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.excercise5.Model.Post
import com.example.excercise5.databinding.RecyclerRowBinding

class FeedRecyclerAdapter(private val postList: ArrayList<Post>): RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent ,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text = postList.get(position).email
        holder.binding.recyclerCommentText.text = postList.get(position).comment

    }

    override fun getItemCount(): Int {
        return  postList.size
    }

    class PostHolder(val binding: RecyclerRowBinding):  RecyclerView.ViewHolder(binding.root){


    }
}
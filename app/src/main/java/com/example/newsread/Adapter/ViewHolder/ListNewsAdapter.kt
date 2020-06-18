package com.example.newsread.Adapter.ViewHolder

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.newsread.Common.ISO8601Parser
import com.example.newsread.Interface.ItemClickListener
import com.example.newsread.Model.Articles
import com.example.newsread.NewsDetail
import com.example.newsread.R
import com.google.gson.internal.bind.util.ISO8601Utils
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.util.*


class ListNewsAdapter(val articleList:MutableList<Articles>,private val context:Context):RecyclerView.Adapter<ListNewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.news_layout,parent,false)
        return ListNewsViewHolder(itemView);
    }

    override fun getItemCount(): Int {
       return articleList.size
    }

    override fun onBindViewHolder(holder: ListNewsViewHolder, position: Int) {
        //Load Image
        Picasso.with(context)
            .load(articleList[position]!!.urlToImage)
            .into(holder.article_image)
        if(articleList[position].title!!.length >65 )
        {
            holder.article_title.text = articleList[position].title!!.substring(0,65)+"..."
        }
        else
        {
            holder.article_title.text = articleList[position].title!!
        }

        if(articleList[position].publishedAt != null)
        {

            var date: Date?=null
            try {
                date = ISO8601Parser.parser(articleList[position].publishedAt!!)
                Log.d("thotest publishedAt:",articleList[position].publishedAt!!)
                holder.article_time.setReferenceTime(date!!.time)
            }catch (ex:ParseException)
            {
                ex.printStackTrace()
            }

        }
        //Set event Click
        holder.setItemClickListener(object : ItemClickListener{
            override fun onClick(view: View, position: Int) {
                val detail = Intent(context,NewsDetail::class.java)
                detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                detail.putExtra("webURL",articleList[position].url)
                context.startActivity(detail)
            }
        })


    }

}
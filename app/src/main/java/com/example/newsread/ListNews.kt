package com.example.newsread

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsread.Adapter.ViewHolder.ListNewsAdapter
import com.example.newsread.Common.Common
import com.example.newsread.Interface.NewsService
import com.example.newsread.Model.News
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_list_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException


class ListNews : AppCompatActivity() {
    var source = ""
    var webHotUrl:String?=""

    lateinit var dialog: AlertDialog
    lateinit var mService:NewsService
    lateinit var adapter:ListNewsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)
        //Init View
        mService = Common.newsService
        dialog =  SpotsDialog.Builder().setContext(baseContext).build()

        swipe_to_refresh.setOnRefreshListener{(loadNews(source,true))}

        diagonalLayout.setOnClickListener{
            val detail = Intent(baseContext,NewsDetail::class.java)
            detail.putExtra("webURL",webHotUrl)
            startActivity(detail)
        }

        list_news.setHasFixedSize(true)
        list_news.layoutManager = LinearLayoutManager(this)

        if (intent != null)
        {
            source = intent.getStringExtra("source")
            if(!source.isEmpty())
                loadNews(source,false)
        }
    }

    private fun loadNews(source: String?, isRefreshed: Boolean) {
        if(isRefreshed)
        {
            try {
                dialog.show()
            }
            catch (ex:WindowManager.BadTokenException)
            {
                ex.printStackTrace()
            }
         //   dialog.show()
            mService.getNewsFromSource(Common.getNewsAPI(source!!))
                .enqueue(object : Callback<News> {
                    override fun onFailure(call: Call<News>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                    override fun onResponse(call: Call<News>, response: Response<News>) {
                        dialog.dismiss()
                        Picasso.with(baseContext)
                            .load(response!!.body()!!.articles!![0].urlToImage)
                            .into(top_image)
                        top_title.text = response!!.body()!!.articles!![0].title
                        top_author.text = response!!.body()!!.articles!![0].author
                        webHotUrl = response!!.body()!!.articles!![0].url
                        //Load all remain articles
                        var removeFirstItem = response!!.body()!!.articles
                        //Because we get first item to hot now
                        removeFirstItem!!.removeAt(0)
                        adapter = ListNewsAdapter(removeFirstItem!!,baseContext)
                        adapter.notifyDataSetChanged()
                        list_news.adapter = adapter
                    }

                })

        }
        else
        {
            swipe_to_refresh.isRefreshing = true

            mService.getNewsFromSource(Common.getNewsAPI(source!!))
                .enqueue(object : Callback<News> {
                    override fun onFailure(call: Call<News>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<News>, response: Response<News>) {
                        swipe_to_refresh.isRefreshing = false
                        Picasso.with(baseContext)
                            .load(response!!.body()!!.articles!![0].urlToImage)
                            .into(top_image)
                        top_title.text = response!!.body()!!.articles!![0].title
                        top_author.text = response!!.body()!!.articles!![0].author
                        webHotUrl = response!!.body()!!.articles!![0].url
                        //Load all remain articles
                        var removeFirstItem = response!!.body()!!.articles
                        //Because we get first item to hot now
                        removeFirstItem!!.removeAt(0)
                        adapter = ListNewsAdapter(removeFirstItem!!,baseContext)
                        adapter.notifyDataSetChanged()
                        list_news.adapter = adapter

                    }

                })


        }
    }
}

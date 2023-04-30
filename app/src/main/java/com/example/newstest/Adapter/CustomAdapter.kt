package com.example.newsappenqueue.Adapters

import android.graphics.Bitmap
import android.renderscript.RenderScript
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.example.newstest.R
import com.example.newstest.retrofit.Article
import com.example.newstest.retrofit.NewsModel
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivArticleImage = itemView.findViewById<ImageView>(R.id.ivArticleImage)

        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        val tvPublishedAt = itemView.findViewById<TextView>(R.id.tvPublishedAt)

        val Source = itemView.findViewById<TextView>(R.id.tvSource)

        val Dialog = itemView.findViewById<AppCompatImageButton>(R.id.Dialog)

    }

    //Changed the 5 variables below to Article from NewsModel
    private val diffcallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }
   //Newsmodel to article
    fun setOnItemClickListener(listen: (Article) -> Unit){
        Log.d("NewsAdapter", "setOnItem")
        onItemClickListener = listen
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnDialogListener(listen: (Article,ArticleViewHolder) -> Unit){
        Log.d("NewsAdapter", "setOnItem")
        onDialogListener = listen
    }

    private var onDialogListener: ((Article,ArticleViewHolder) -> Unit)? = null

//Changed the variable time,imageurl and tvTitle
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        val time: String? = article?.publishedAt

        //Gets the current instantaneous time, the time of this exact moment,we will then return the instant time from a specified timezone
        val currentTimeInHours = Instant.now().atZone(ZoneId.of("Asia/Kolkata"))
        //We will parse the date/time received from out newsapi, since it is in GMT(standard time) converts it into a type `instant`
        //we will then take the newly parsed GMT time into a specified timezone
        val newsTimeInHours = Instant.parse(time).atZone(ZoneId.of("Asia/Kolkata"))
        //We will then calculate the time difference between the current instance(time) and when the article was published in our specified timezone
        val hoursDifference = Duration.between(currentTimeInHours, newsTimeInHours)
        //We will then get the number of hours from the result and convert it into string inorder to put in in a `textview`


        val hoursAgos = if(kotlin.math.abs(hoursDifference.toMinutes().toInt()) == 1){
            kotlin.math.abs(hoursDifference.toMinutes()).toString()+ " Minute ago"

        }else if(kotlin.math.abs(hoursDifference.toMinutes()) < 60){
            kotlin.math.abs(hoursDifference.toMinutes()).toString()+ " Minutes ago"

        }else if(kotlin.math.abs(hoursDifference.toHours().toInt()) == 1){
            kotlin.math.abs(hoursDifference.toHours()).toString()+ " Hour ago"

        }else if(kotlin.math.abs(hoursDifference.toHours()) < 24){
            kotlin.math.abs(hoursDifference.toHours()).toString()+ " Hours ago"
        }
        else if(kotlin.math.abs(hoursDifference.toDays().toInt()) == 1){
            kotlin.math.abs(hoursDifference.toDays()).toString()+ " Day ago"
        }
        else{
            kotlin.math.abs(hoursDifference.toDays()).toString() + " Day ago"
        }

        var imageUrl = article.urlToImage

        holder.itemView.apply {
            if(imageUrl.isNullOrEmpty()){
                Glide.with(this).load(R.drawable.samplenews).centerCrop().into(holder.ivArticleImage)
            }else{
                val requestOptions: RequestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .placeholder(R.drawable.samplenews)
                    .priority(Priority.IMMEDIATE)
                    .encodeFormat(Bitmap.CompressFormat.PNG)
                    .format(DecodeFormat.DEFAULT)

                Glide.with(this).applyDefaultRequestOptions(requestOptions).load(article.urlToImage).error(R.drawable.samplenews).into(holder.ivArticleImage)
            }

            holder.tvTitle.text = article?.title
            holder.tvPublishedAt.text = hoursAgos
            holder.Source.text = article?.source?.name
            holder.Dialog


            //With `setOnClickClistener` upon detecting a click, change the lambda parameter to the article variable.
            setOnClickListener{
                onItemClickListener?.let {
                    it(article)
                }
            }
            onDialogListener?.let {
                it(article,holder)
            }

        }
    }

    override fun getItemCount() = differ.currentList.size
}
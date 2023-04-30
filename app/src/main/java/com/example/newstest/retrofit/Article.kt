package com.example.newstest.retrofit

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.newstest.retrofit.Source
@Entity(
    //In the Entity annotation, the indices parameter specifies the columns we want to index, index is something like a index page in a book, allowing us to find information
    //Far faster which would drastically improve performance when searching for saved articles.

    tableName = "articles", indices = [Index(value = ["url","title"], unique = true)]
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val author: String?,
    val description: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)
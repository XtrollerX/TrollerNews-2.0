package com.example.newstest.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newstest.retrofit.Article


@Dao
interface Doa {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long //returns the id's of the article

    //In this 'query' we are returning a list of "Article", this class contains numerous parameters one of which contains a Non-primative datatype, hence we have to convert it.
    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles")
    suspend fun nukeTable()

    @Query("SELECT (SELECT COUNT(*) FROM articles) == 0")
    suspend fun isEmpty(): Boolean

}
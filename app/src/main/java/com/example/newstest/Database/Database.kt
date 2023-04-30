package com.example.newstest.Database

import android.content.Context
import androidx.room.*
import com.example.newstest.retrofit.Article
import com.example.thenewsapplication.ROOM.Converters


@Database(
    entities = [Article::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class RoomDatabases: RoomDatabase() {

    abstract fun getArticleDao(): Doa

    companion object {
        @Volatile
        private var instance: RoomDatabases? = null
        private val LOCK = Any()

        //In the invoke method, by specifying the lock, under normal circumstances we can use this lock to share with other classes/insatnces. By specifying a lock it allows us to create multiple synchronize blocks(refer to stackoverflow)
        //By using "operator fun invoke" in the companion object, we can call the "invoke function" by calling the class "ArticleDataBase" and specifing the "context", so its something like this "ArticleDataBase(context)" to call the "invoke" fun.
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDatabases::class.java,
                "article_db.db"
            ).fallbackToDestructiveMigration().build()
    }

}
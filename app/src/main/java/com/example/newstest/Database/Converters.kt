package com.example.thenewsapplication.ROOM

import androidx.room.TypeConverter
import com.example.newstest.retrofit.Source


//Converters are needed as 'Room" only supports "Primative data types" or "Known Data types" such as Int, Strings. In order to use Room we need to convert the "Source" data tpye in the "Article" file into a primative datatype.
class Converters {

    @TypeConverter
    fun fromSource(source: Source): String?{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String?): Source {
        return Source(name, name)
    }

}
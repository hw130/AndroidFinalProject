package com.example.auctioninginfoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.auctioninginfoapp.model.FreshDao
import com.example.auctioninginfoapp.model.FreshData
import com.example.auctioninginfoapp.model.SaveItem

/** 데이터베이스 클래스 선언 */
@Database(entities = [FreshData::class, SaveItem::class], version = 1)
abstract class DatabaseModule: RoomDatabase() {

    /**FreshDao를 참조하기 위한 추상 메서드 선언 */
    abstract fun freshDao(): FreshDao

    /** 데이터베이스를 Singleton으로 반환하기 위한 Companin Object 선언 */
    companion object{
        /**database 변수 선언 */
        private var database: DatabaseModule? = null
        /** database 이름 상수 선언 */
        private const val ROOM_DB = "room.db"

        /** 데이터베이스를 Singleton으로 반환하는 함수 선언 */
        fun getDatabase(context: Context): DatabaseModule{
            return database ?: Room.databaseBuilder(
                context.applicationContext,
                DatabaseModule::class.java, ROOM_DB
            ).allowMainThreadQueries() //Main Thread에서 DB 입출력 가능
                .fallbackToDestructiveMigration() //스키마(Database) 버전 변경 가능
                .build()
        } //end of getDatabase
    } //end of companion object
} // end of class DatabaseModule
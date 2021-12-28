package hu.unimiskolc.iit.mobile.untitledwestern.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.converter.DateTypeConverter
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.dao.GameDao
import hu.unimiskolc.iit.mobile.untitledwestern.framework.db.entity.GameEntity

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class
)

abstract class WesternDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "western.db"
        private var instance: WesternDatabase? = null

        private fun create(context: Context) : WesternDatabase = Room.databaseBuilder(context, WesternDatabase::class.java, DATABASE_NAME)
            .addCallback(DB_CALLBACK)
            .build()

        fun getInstance(context: Context) : WesternDatabase = (instance ?: create(context)).also { instance = it}

        private val DB_CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }

    abstract fun gameDao() : GameDao
}
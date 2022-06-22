package eu.faircode.email.data.resource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.faircode.email.data.resource.local.dao.MenuLeftDAO
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity

@Database(entities = [MenuLeftEntity::class], version = 1)
abstract class MenuLeftDatabase: RoomDatabase() {
    abstract fun menuLeftDao(): MenuLeftDAO

    companion object {
        private var INSTANCE: MenuLeftDatabase? = null

        fun getDatabase(context: Context): MenuLeftDatabase {
            val ignoreInstant = INSTANCE
            if (ignoreInstant != null) {
                return ignoreInstant
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MenuLeftDatabase::class.java,
                        "file_share_db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
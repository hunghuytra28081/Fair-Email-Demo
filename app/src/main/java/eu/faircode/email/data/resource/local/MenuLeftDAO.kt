package eu.faircode.email.data.resource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MenuLeftDAO {

    @Query("SELECT * FROM MenuLeftEntity")
    fun getAll(): LiveData<List<MenuLeftEntity>>

    @Insert
    suspend fun insert(item : MenuLeftEntity)

    @Delete
    suspend fun delete(item: MenuLeftEntity)
}
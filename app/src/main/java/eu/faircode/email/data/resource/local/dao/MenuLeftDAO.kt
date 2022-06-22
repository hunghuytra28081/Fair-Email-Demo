package eu.faircode.email.data.resource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity

@Dao
interface MenuLeftDAO {

    @Query("SELECT * FROM MenuLeftEntity")
    fun getAll(): LiveData<List<MenuLeftEntity>>

    @Insert
    suspend fun insert(item : List<MenuLeftEntity>)

    @Delete
    suspend fun delete(item: List<MenuLeftEntity>)
}
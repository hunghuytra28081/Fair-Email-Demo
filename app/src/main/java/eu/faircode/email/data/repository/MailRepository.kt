package eu.faircode.email.data.repository

import androidx.lifecycle.LiveData
import eu.faircode.email.data.resource.local.dao.MenuLeftDAO
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity

class MailRepository(private val dao: MenuLeftDAO) {

    val readAllLiveData : LiveData<List<MenuLeftEntity>> = dao.getAll()

    suspend fun insertMenuLeft(list : List<MenuLeftEntity>) = dao.insert(list)

    suspend fun deleteMenuLeft(list: List<MenuLeftEntity>) = dao.delete(list)
}
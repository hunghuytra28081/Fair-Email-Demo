package eu.faircode.email.data.repository

import eu.faircode.email.data.resource.local.dao.MenuLeftDAO
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity

class MailRepository(private val dao: MenuLeftDAO) {

    fun getAllMenuLeft() = dao.getAll()

    suspend fun insertMenuLeft(item : MenuLeftEntity) = dao.insert(item)

    suspend fun deleteMenuLeft(item: MenuLeftEntity) = dao.delete(item)
}
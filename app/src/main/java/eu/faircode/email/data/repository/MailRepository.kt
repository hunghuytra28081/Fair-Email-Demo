package eu.faircode.email.data.repository

import eu.faircode.email.data.resource.MailDataSource
import eu.faircode.email.data.resource.local.MenuLeftDAO
import eu.faircode.email.data.resource.local.MenuLeftEntity

class MailRepository(
        private val dao: MenuLeftDAO
) {

    fun getAllMenuLeft() = dao.getAll()

    suspend fun insertMenuLeft(item : MenuLeftEntity) = dao.insert(item)

    suspend fun deleteMenuLeft(item: MenuLeftEntity) = dao.delete(item)
}
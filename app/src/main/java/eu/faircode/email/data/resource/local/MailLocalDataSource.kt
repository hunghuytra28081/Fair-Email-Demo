package eu.faircode.email.data.resource.local

import eu.faircode.email.data.resource.MailDataSource

class MailLocalDataSource (private val dao: MenuLeftDAO): MailDataSource.Local{

    override fun getAllMenuLeft(): List<MenuLeftEntity> = dao.getAll()

    override suspend fun insertMenuLeft(item: MenuLeftEntity) = dao.insert(item)

    override suspend fun deleteMenuLeft(item: MenuLeftEntity)  = dao.delete(item)
}

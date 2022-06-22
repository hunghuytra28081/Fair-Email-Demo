package eu.faircode.email.data.resource.local

import androidx.lifecycle.LiveData
import eu.faircode.email.data.resource.MailDataSource
import eu.faircode.email.data.resource.local.dao.MenuLeftDAO
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity

class MailLocalDataSource (private val dao: MenuLeftDAO): MailDataSource.Local{

    override fun getAllMenuLeft(): LiveData<List<MenuLeftEntity>> = dao.getAll()

    override suspend fun insertMenuLeft(item: MenuLeftEntity) = dao.insert(item)

    override suspend fun deleteMenuLeft(item: MenuLeftEntity)  = dao.delete(item)
}

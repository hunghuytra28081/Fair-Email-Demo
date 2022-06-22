package eu.faircode.email.data.resource.local

import androidx.lifecycle.LiveData
import eu.faircode.email.data.resource.MailDataSource
import eu.faircode.email.data.resource.local.dao.MenuLeftDAO
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity

class MailLocalDataSource (private val dao: MenuLeftDAO): MailDataSource.Local{

    override fun getAllMenuLeft(): LiveData<List<MenuLeftEntity>> = dao.getAll()

    override suspend fun insertMenuLeft(list: List<MenuLeftEntity>) = dao.insert(list)

    override suspend fun deleteMenuLeft(list: List<MenuLeftEntity>)  = dao.delete(list)
}

package eu.faircode.email.data.resource

import androidx.lifecycle.LiveData
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity

class MailDataSource {
    interface Local{
        fun getAllMenuLeft(): LiveData<List<MenuLeftEntity>>

        suspend fun insertMenuLeft(item : MenuLeftEntity)

        suspend fun deleteMenuLeft(item: MenuLeftEntity)
    }
}
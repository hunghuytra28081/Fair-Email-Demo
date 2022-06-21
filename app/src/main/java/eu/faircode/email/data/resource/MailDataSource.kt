package eu.faircode.email.data.resource

import eu.faircode.email.data.resource.local.MenuLeftEntity

class MailDataSource {
    interface Local{
        fun getAllMenuLeft(): List<MenuLeftEntity>

        suspend fun insertMenuLeft(item : MenuLeftEntity)

        suspend fun deleteMenuLeft(item: MenuLeftEntity)
    }
}
package eu.faircode.email.ui.leftmenu.menu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import eu.faircode.email.data.repository.MailRepository
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity
import eu.faircode.email.data.resource.local.database.MenuLeftDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MenuLeftViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val readAllDataLive: LiveData<List<MenuLeftEntity>>
    private val repository: MailRepository

    init {
        val mailDao = MenuLeftDatabase.getDatabase(application).menuLeftDao()
        repository = MailRepository(mailDao)
        readAllDataLive = repository.getAllMenuLeft()
    }

    fun insertItemMenu(item: MenuLeftEntity) {
        scope.launch(Dispatchers.IO) {
            repository.insertMenuLeft(item)
        }
    }

    fun deleteItemMenu(item: MenuLeftEntity) {
        scope.launch(Dispatchers.IO) {
            repository.deleteMenuLeft(item)
        }
    }
}
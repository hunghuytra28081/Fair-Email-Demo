package eu.faircode.email.ui.leftmenu.menu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import eu.faircode.email.data.repository.MailRepository
import eu.faircode.email.data.resource.local.database.MenuLeftDatabase
import eu.faircode.email.data.resource.local.entity.MenuLeftEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuLeftViewModel(application: Application) : AndroidViewModel(application) {

    val readAllDataLive: LiveData<List<MenuLeftEntity>>
    private val repository: MailRepository

    init {
        val mailDao = MenuLeftDatabase.getDatabase(application).menuLeftDao()
        repository = MailRepository(mailDao)
        readAllDataLive = repository.readAllLiveData
    }

    fun insertItemMenu(list: List<MenuLeftEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMenuLeft(list)
        }
    }

    fun deleteItemMenu(list: List<MenuLeftEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMenuLeft(list)
        }
    }
}

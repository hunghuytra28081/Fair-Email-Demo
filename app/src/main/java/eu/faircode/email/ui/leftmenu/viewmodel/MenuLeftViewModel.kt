package eu.faircode.email.ui.leftmenu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import eu.faircode.email.data.repository.MailRepository
import eu.faircode.email.data.resource.local.MenuLeftEntity
import eu.faircode.email.data.resource.local.database.MenuLeftDatabase
import kotlinx.coroutines.Dispatchers

class MenuLeftViewModel(application: Application) : AndroidViewModel(application) {

    val readAllDataLive: LiveData<List<MenuLeftEntity>>
    private val repository: MailRepository


    init {
        val mailDao = MenuLeftDatabase.getDatabase(application).menuLeftDao()
        repository = MailRepository(mailDao)
        readAllDataLive = repository.getAllMenuLeft()
    }

    fun insertItemMenu(listPackage: List<MenuLeftEntity>) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertListData(listPackage)
        }
    }

    fun deleteIgnore(aPackage: MenuLeftEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteIgnore(aPackage)
        }
    }
}
package mamali.qa.notify.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mamali.qa.notify.models.Kind
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.data.repositories.NoteRepository

class NoteDetailViewModel(private val repository: NoteRepository) : ViewModel() {

    private fun insert(note: NoteEntity) = viewModelScope.launch {
            repository.insert(note)
    }

    fun getInput(
        title: String,
        desc: String,
        kind: Kind,
        parent: String,
        parentId: Int,
        date: Long
    ) {
        val note = NoteEntity(title, desc, kind, parent, parentId,date)
        insert(note)
    }

    fun updateNote(name: String, desc: String, id: Int, date: Long) {
        viewModelScope.launch {
                repository.updateNote(name, desc, id,date)
        }
    }

    fun deleteNote(id: Int?) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

}

class NoteDetailViewModelFactory(private val repository: NoteRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow view model class")
    }
}
package mamali.qa.notify.viewModel

import androidx.appcompat.widget.DecorContentParent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mamali.qa.notify.NoteViewModel
import mamali.qa.notify.database.NoteEntity
import mamali.qa.notify.databinding.FragmentNoteDetailsBinding
import mamali.qa.notify.repositories.NoteRepository

class NoteDetailViewModel(private val repository: NoteRepository) : ViewModel() {

    fun insert(note: NoteEntity) = viewModelScope.launch {
        repository.insert(note)

    }

    fun getInput(title: String, desc: String, kind: String, parent: String, parentId: Int) {

        var note = NoteEntity(title, desc, kind, parent, parentId)
        insert(note)
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
package mamali.qa.notify.viewModel

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mamali.qa.notify.models.Kind
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.data.repositories.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    // TODO(SHAYAN): Problem, don;t use equal, you return `job` with this operation and
    //  it's not recommended for view to access viewModel job
    private fun insert(note: NoteEntity) {
        viewModelScope.launch {
        repository.insert(note)
    }
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
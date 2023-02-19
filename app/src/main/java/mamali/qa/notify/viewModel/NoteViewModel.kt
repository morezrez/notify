package mamali.qa.notify

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mamali.qa.notify.database.NoteEntity
import mamali.qa.notify.repositories.NoteRepository

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    var listLiveData: LiveData<List<NoteEntity>> = MutableLiveData(emptyList())

    fun getNotes(parentId: Int? = -1) = viewModelScope.launch {
       listLiveData = repository.getNotes(parentId)
    }

    fun getSelectedNote(id : Int)=viewModelScope.launch {
        repository.getSelectedNote(id)
    }

    fun insert(note: NoteEntity) = viewModelScope.launch {
        repository.insert(note)
    }

    fun deleteNote(id: Int?) = CoroutineScope(Dispatchers.IO).launch {
        repository.deleteNote(id)
    }

    fun updateFile(id: Int?, name: String)= CoroutineScope(Dispatchers.IO).launch {
        repository.updateFile(id,name)
    }

    fun addFloatingButtonOnClick(btnFloatingAddFile : FloatingActionButton,btnFloatingAddNote : FloatingActionButton) {
        if (btnFloatingAddFile.visibility == View.INVISIBLE && btnFloatingAddNote.visibility == View.INVISIBLE) {
                btnFloatingAddFile.visibility = View.VISIBLE
                btnFloatingAddNote.visibility = View.VISIBLE
        } else {
                btnFloatingAddFile.visibility = View.INVISIBLE
                btnFloatingAddNote.visibility = View.INVISIBLE
        }
    }

    fun updateToolbar(txtToolbar : TextView, imgBack : ImageView, imgOption : ImageView,parent: String?){
        if (parent!="root"){
            txtToolbar.text=parent
            imgBack.visibility=View.VISIBLE
            imgOption.visibility=View.VISIBLE
        }
    }


}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow view model class")
    }
}

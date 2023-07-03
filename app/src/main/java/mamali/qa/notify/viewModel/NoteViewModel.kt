package mamali.qa.notify.viewModel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mamali.qa.notify.utils.toPersianDigit
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.data.repositories.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    // TODO(SHAYAN): var as public is not good, use lazy here instead of getNotesMethod


    private val _floatingButtonVisibilityLiveData = MutableLiveData(false)
    val floatingButtonVisibilityLiveData : LiveData<Boolean> = _floatingButtonVisibilityLiveData

     fun getNotes(parentId: Int? = -1) = repository.getNotes(parentId)


    fun insert(note: NoteEntity) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun deleteNote(id: Int?) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun updateFile(id: Int?, name: String){
        viewModelScope.launch {
            repository.updateFile(id,name)
        }
    }

    fun addFloatingButtonOnClick() {
        _floatingButtonVisibilityLiveData.value= _floatingButtonVisibilityLiveData.value?.not()
    }

    fun updateToolbar(txtToolbar : TextView, imgBack : ImageView, imgOption : ImageView,parent: String?){
        // TODO(SHAYAN): Check hardCode string is not good way.
        if (parent!="root"){
            txtToolbar.text=parent?.toPersianDigit()
            imgBack.visibility=View.VISIBLE
            imgOption.visibility=View.VISIBLE
        }
    }


}
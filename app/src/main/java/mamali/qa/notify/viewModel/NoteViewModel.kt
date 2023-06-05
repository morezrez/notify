package mamali.qa.notify

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mamali.qa.notify.utils.toPersianDigit
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.models.NoteViewEntity
import mamali.qa.notify.data.repositories.NoteRepository

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    var listLiveData: LiveData<List<NoteViewEntity>> = MutableLiveData(emptyList())
    private val _floatingButtonVisibilityLiveData = MutableLiveData(false)
    val floatingButtonVisibilityLiveData : LiveData<Boolean> = _floatingButtonVisibilityLiveData

    fun getNotes(parentId: Int? = -1) = viewModelScope.launch {
       listLiveData = repository.getNotes(parentId)
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

    fun addFloatingButtonOnClick() {
        _floatingButtonVisibilityLiveData.value= _floatingButtonVisibilityLiveData.value?.not()
    }

    fun updateToolbar(txtToolbar : TextView, imgBack : ImageView, imgOption : ImageView,parent: String?){
        if (parent!="root"){
            txtToolbar.text=parent?.toPersianDigit()
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

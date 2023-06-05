package mamali.qa.notify

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import mamali.qa.notify.database.NoteRoomDataBase
import mamali.qa.notify.repositories.NoteRepository

class NotesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { NoteRoomDataBase.getDataBase(this, applicationScope) }
    val repository by lazy {NoteRepository(database.noteDao())}
}
package mamali.qa.notify

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import mamali.qa.notify.data.datasource.NoteLocalDataSource
import mamali.qa.notify.data.database.NoteRoomDataBase
import mamali.qa.notify.data.repositories.NoteRepository

class NotesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { NoteRoomDataBase.getDataBase(this, applicationScope) }
    private val dataSource by lazy { NoteLocalDataSource(database.noteDao()) }
    val repository by lazy { NoteRepository(dataSource) }
}
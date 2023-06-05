package mamali.qa.notify

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import mamali.qa.notify.data.datasource.NoteLocalDataSource
import mamali.qa.notify.data.database.NoteRoomDataBase
import mamali.qa.notify.data.repositories.NoteRepository
@HiltAndroidApp
class NotesApplication : Application()
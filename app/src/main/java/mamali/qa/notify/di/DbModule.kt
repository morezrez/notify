package mamali.qa.notify.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import mamali.qa.notify.data.database.NoteDao
import mamali.qa.notify.data.database.NoteRoomDataBase
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.models.NoteViewEntity
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Volatile
    private var INSTANCE: NoteRoomDataBase? = null

    @Provides
    @Singleton
    fun getDataBase(@ApplicationContext appContext: Context): NoteRoomDataBase {
        // TODO(SHAYAN): Problem, This is not thread safe
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                appContext.applicationContext,
                NoteRoomDataBase::class.java,
                "note_database"
            )
                .build()
            INSTANCE = instance
            instance
        }
    }


}
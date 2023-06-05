package mamali.qa.notify.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.models.NoteViewEntity

@Database(entities = [NoteEntity::class],views =[NoteViewEntity::class], version = 1, exportSchema = false)
abstract class NoteRoomDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDataBase? = null

        fun getDataBase(context: Context, scope: CoroutineScope): NoteRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDataBase::class.java,
                    "note_database"
                ).addCallback(NoteDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class NoteDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

        }
    }
}
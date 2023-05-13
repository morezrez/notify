package mamali.qa.notify.data.database

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
}
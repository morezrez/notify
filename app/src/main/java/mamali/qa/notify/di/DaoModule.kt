package mamali.qa.notify.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mamali.qa.notify.data.database.NoteDao
import mamali.qa.notify.data.database.NoteRoomDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    // TODO(SHAYAN): Problem, it's better to separate this functions from this file and put it to another file
    @Provides
    @Singleton
    fun provideDao(database: NoteRoomDataBase): NoteDao {
        return database.noteDao()
    }
}
package com.rahulraghuwanshi.letsnote.di

import android.app.Application
import androidx.room.Room
import com.rahulraghuwanshi.letsnote.feature_notes.data.data_source.DatabaseEncryptionUtils
import com.rahulraghuwanshi.letsnote.feature_notes.data.data_source.NoteDatabase
import com.rahulraghuwanshi.letsnote.feature_notes.data.data_source.PassphraseUtils
import com.rahulraghuwanshi.letsnote.feature_notes.data.repository.NoteRepositoryImpl
import com.rahulraghuwanshi.letsnote.feature_notes.domain.repository.NoteRepository
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.AddNoteUseCase
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.DeletedNoteUseCase
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.GetNoteUseCase
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.GetNotesUseCase
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.NotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePassphraseUtils(application: Application): PassphraseUtils {
        return PassphraseUtils(application)
    }

    @Provides
    @Singleton
    fun provideSupportOpenHelperFactory(
        application: Application,
        passphraseUtils: PassphraseUtils
    ): SupportOpenHelperFactory {
        System.loadLibrary("sqlcipher") // This must be called before doing anything with sqlcipher.
        val passphrase = passphraseUtils.getPassphraseOrGenerateIfMissing().value
        val state =
            DatabaseEncryptionUtils.getDatabaseState(application, NoteDatabase.DATABASE_NAME)

        // Check if db is unencrypted then encrypt the db.
        if (state == DatabaseEncryptionUtils.State.UNENCRYPTED) {
            DatabaseEncryptionUtils.encrypt(
                application,
                NoteDatabase.DATABASE_NAME,
                passphrase
            )
        }
        val factory = SupportOpenHelperFactory(passphrase)

        return factory
    }

    @Provides
    @Singleton
    fun provideNoteDatabase(
        application: Application,
        factory: SupportOpenHelperFactory
    ): NoteDatabase {

        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).openHelperFactory(factory).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
        return NotesUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deletedNoteUseCase = DeletedNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository)
        )
    }
}
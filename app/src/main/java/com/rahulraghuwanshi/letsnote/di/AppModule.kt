package com.rahulraghuwanshi.letsnote.di

import android.app.Application
import androidx.room.Room
import com.rahulraghuwanshi.letsnote.feature_notes.data.data_source.NoteDatabase
import com.rahulraghuwanshi.letsnote.feature_notes.data.repository.NoteRepositoryImpl
import com.rahulraghuwanshi.letsnote.feature_notes.domain.repository.NoteRepository
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.AddNoteUseCase
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.DeletedNoteUseCase
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.GetNotesUseCase
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.NotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
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
            addNoteUseCase = AddNoteUseCase(repository)
        )
    }
}
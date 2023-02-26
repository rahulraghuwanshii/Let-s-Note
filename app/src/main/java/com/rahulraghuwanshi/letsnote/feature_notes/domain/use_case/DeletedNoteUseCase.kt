package com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case

import com.rahulraghuwanshi.letsnote.feature_notes.domain.model.Note
import com.rahulraghuwanshi.letsnote.feature_notes.domain.repository.NoteRepository

class DeletedNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}
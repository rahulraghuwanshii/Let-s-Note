package com.rahulraghuwanshi.letsnote.feature_notes.presentation.notes

import com.rahulraghuwanshi.letsnote.feature_notes.domain.model.Note
import com.rahulraghuwanshi.letsnote.feature_notes.domain.util.NoteOrder

sealed class NotesViewEvents {
    data class Order(val noteOrder: NoteOrder) : NotesViewEvents()
    data class DeleteNote(val note: Note) : NotesViewEvents()

    object RestoreNote : NotesViewEvents()

    object ToggleOrderSection : NotesViewEvents()
}

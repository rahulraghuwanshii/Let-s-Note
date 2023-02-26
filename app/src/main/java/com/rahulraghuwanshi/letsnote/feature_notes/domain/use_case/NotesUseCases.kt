package com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case

data class NotesUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deletedNoteUseCase: DeletedNoteUseCase,
    val addNoteUseCase: AddNoteUseCase
)

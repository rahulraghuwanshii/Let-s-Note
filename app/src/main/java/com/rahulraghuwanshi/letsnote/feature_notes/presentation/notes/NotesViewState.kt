package com.rahulraghuwanshi.letsnote.feature_notes.presentation.notes

import com.rahulraghuwanshi.letsnote.feature_notes.domain.model.Note
import com.rahulraghuwanshi.letsnote.feature_notes.domain.util.NoteOrder
import com.rahulraghuwanshi.letsnote.feature_notes.domain.util.OrderType

data class NotesViewState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
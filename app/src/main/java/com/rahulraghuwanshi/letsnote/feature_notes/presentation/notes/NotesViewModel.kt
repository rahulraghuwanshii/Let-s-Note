package com.rahulraghuwanshi.letsnote.feature_notes.presentation.notes


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulraghuwanshi.letsnote.feature_notes.domain.model.Note
import com.rahulraghuwanshi.letsnote.feature_notes.domain.use_case.NotesUseCases
import com.rahulraghuwanshi.letsnote.feature_notes.domain.util.NoteOrder
import com.rahulraghuwanshi.letsnote.feature_notes.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel() {


    private val _viewState = mutableStateOf(NotesViewState())
    val viewState: State<NotesViewState> get() = _viewState

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesViewEvents) {
        when (event) {
            is NotesViewEvents.DeleteNote -> {
                deleteNote(event.note)
            }

            is NotesViewEvents.Order -> {
                orderNotes(event.noteOrder)
            }

            is NotesViewEvents.RestoreNote -> {
                restoreNote()
            }

            is NotesViewEvents.ToggleOrderSection -> {
                _viewState.value = _viewState.value.copy(
                    isOrderSectionVisible = !_viewState.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun orderNotes(noteOrder: NoteOrder) {
        if (_viewState.value.noteOrder::class == noteOrder::class &&
            _viewState.value.noteOrder.orderType == noteOrder.orderType
        ) {
            return
        }

        getNotes(noteOrder)
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = notesUseCases.getNotesUseCase(noteOrder)
            .onEach { notes ->
                _viewState.value = viewState.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesUseCases.deletedNoteUseCase(note)
            recentlyDeletedNote = note
        }
    }

    private fun restoreNote() {
        viewModelScope.launch {
            notesUseCases.addNoteUseCase(recentlyDeletedNote ?: return@launch)
            recentlyDeletedNote = null
        }
    }
}
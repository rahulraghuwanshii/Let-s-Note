package com.rahulraghuwanshi.letsnote.feature_notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rahulraghuwanshi.letsnote.ui.theme.BabyBlue
import com.rahulraghuwanshi.letsnote.ui.theme.LightGreen
import com.rahulraghuwanshi.letsnote.ui.theme.RedOrange
import com.rahulraghuwanshi.letsnote.ui.theme.RedPink
import com.rahulraghuwanshi.letsnote.ui.theme.Violet

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)

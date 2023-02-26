package com.rahulraghuwanshi.letsnote.feature_notes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rahulraghuwanshi.letsnote.ui.theme.LetsNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LetsNoteTheme {}
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    LetsNoteTheme {
//        Greeting("Android")
//    }
//}
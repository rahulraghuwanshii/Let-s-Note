package com.rahulraghuwanshi.letsnote.feature_notes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.rahulraghuwanshi.letsnote.ui.theme.LetsNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LetsNoteTheme {
                Text(text = "Hello this is my first compose text")
            }
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
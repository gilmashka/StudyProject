package com.example.studyproject_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.studyproject_2.model.Note
import com.example.studyproject_2.ui.login.LoginScreen
import com.example.studyproject_2.ui.notes.NotesScreen
import com.example.studyproject_2.ui.notes.AddNoteScreen
import com.example.studyproject_2.ui.navigation.LoginScreenObject
import com.example.studyproject_2.ui.navigation.NotesScreenObject
import com.example.studyproject_2.ui.navigation.AddNoteScreenObject
import com.example.studyproject_2.ui.theme.StudyProject_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyProject_2Theme {
                val navController = rememberNavController()

                val notesState = remember { mutableStateListOf<Note>() }
                val userEmail = remember { mutableStateOf("") }

                NavHost(
                    navController = navController,
                    startDestination = LoginScreenObject
                ) {
                    composable<LoginScreenObject> {
                        LoginScreen(
                            onLoginSuccess = { email ->
                                userEmail.value = email
                                navController.navigate(NotesScreenObject(email))
                            }
                        )
                    }
                    composable<NotesScreenObject> { backStackEntry ->
                        val email = backStackEntry.toRoute<NotesScreenObject>().email
                        NotesScreen(
                            email = email,
                            notes = notesState,
                            onAddNoteClick = {
                                navController.navigate(AddNoteScreenObject)
                            }
                        )
                    }
                    composable<AddNoteScreenObject> {
                        AddNoteScreen(
                            onSaveNote = { title, content ->
                                if (title.isNotBlank()) {
                                    notesState.add(Note(title, content))
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
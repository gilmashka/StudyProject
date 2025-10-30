package com.example.studyproject_2.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studyproject_2.ui.strings.Strings

@Composable
fun AddNoteScreen(
    onSaveNote: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    // Возвращаемся назад без сохранения
                    onSaveNote("", "")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = Strings.BACK_BUTTON
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = Strings.ADD_NOTE_TITLE,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(56.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                titleError = false
            },
            label = { Text(Strings.NOTE_TITLE_FIELD) },
            isError = titleError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (titleError) {
            Text(
                text = Strings.NOTE_TITLE_ERROR,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text(Strings.NOTE_CONTENT_FIELD) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            singleLine = false,
            maxLines = Int.MAX_VALUE
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isBlank()) {
                    titleError = true
                } else {
                    onSaveNote(title, content)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(Strings.SAVE_NOTE_BUTTON)
        }
    }
}
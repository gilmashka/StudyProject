package com.example.studyproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyproject.ui.theme.StudyProjectTheme
import kotlin.text.ifEmpty

class ThirdActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyProjectTheme {
                val previousText  = intent.getStringExtra("FromFirst")?: ""
                ThirdScreenUI(previousText = previousText)
            }
        }
    }
}

@Composable
fun ThirdScreenUI(previousText: String){
    val thirdScreen = LocalContext.current
    val displayText = previousText.ifEmpty { "Third screen" }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF93926B))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = displayText,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF504600),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val thirdIntent = Intent(thirdScreen, MainActivity::class.java)
                thirdIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                thirdScreen.startActivity(thirdIntent)
            },
            modifier = Modifier.fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFE100),
                contentColor = Color(0xFF504600)
            )

        ){
            Text("Go to First Screen 3->1",
                fontSize = 16.sp)
        }
    }
}
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
import kotlin.jvm.java


class SecondActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            StudyProjectTheme {
                val fromFirst = intent.getStringExtra("FromFirst")?: ""
                SecondScreenUI(fromFirst = fromFirst)
            }
        }
    }
}

@Composable
fun SecondScreenUI(fromFirst: String){
    val secondScreen = LocalContext.current
    val displayText = fromFirst.ifEmpty { "Second screen" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F605A))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Text(
            text = displayText,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF5EE7D3),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val secondIntent = Intent(secondScreen, ThirdActivity::class.java)
                secondIntent.putExtra("FromFirst", fromFirst)
                secondScreen.startActivity(secondIntent)
            },
            modifier = Modifier.fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0B9176),
                contentColor = Color(0xFF5EE7D3)
            )
        ) {
            Text("Go to Third Screen 2->3",
                fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val secondIntent = Intent(secondScreen, MainActivity::class.java)
                secondIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                secondScreen.startActivity(secondIntent)
            },
            modifier = Modifier.fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0B9176),
                contentColor = Color(0xFF5EE7D3)
            )
        ) {
            Text("Go to First screen 2->1",
                fontSize = 16.sp)
        }
    }
}
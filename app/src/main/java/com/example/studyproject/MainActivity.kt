package com.example.studyproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                FirstScreenUI()
        }
    }
}

@Composable
fun FirstScreenUI() {
    var text by remember { mutableStateOf("") }
    val firstScreen = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD8BFD8))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    "Input your text here",
                    color = Color(0xFF6A0DAD)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                color = Color(0xFF6A0DAD)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val firstIntent = Intent(firstScreen, SecondActivity::class.java)
                firstIntent.putExtra("FromFirst", text)
                firstScreen.startActivity(firstIntent)
            },
            modifier = Modifier.fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7E57C2),
                contentColor = Color(0xFFD8BFD8)
            )
        ) {
            Text("Go to second screen 1->2",
                fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val secondIntent = Intent(firstScreen, ThirdActivity::class.java)
                secondIntent.putExtra("FromFirst", text)
                firstScreen.startActivity(secondIntent)
            },
            modifier = Modifier.fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7E57C2),
                contentColor = Color(0xFFD8BFD8)
            )
        ){
            Text("Go to third screen 1->3",
                fontSize = 16.sp)
        }
    }
}
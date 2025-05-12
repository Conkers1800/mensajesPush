package com.conkers.mensajespush

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Estado para almacenar el token FCM
    var fcmToken by remember { mutableStateOf("Obteniendo token...") }

    // Usamos LaunchedEffect para obtener el token cuando se componga la UI
    LaunchedEffect(Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fallo al obtener el token de registro", task.exception)
                fcmToken = "Error al obtener token"
                return@addOnCompleteListener
            }
            val token = task.result
            fcmToken = token ?: "Token nulo"
            Log.d("FCM", "Token: $token")
        }
    }

    // Estructura simple con Scaffold y Column para mostrar el token
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FCM con Compose") }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Token FCM:")
                Text(
                    text = fcmToken,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
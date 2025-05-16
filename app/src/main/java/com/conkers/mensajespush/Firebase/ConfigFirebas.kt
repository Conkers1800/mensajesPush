package com.conkers.mensajespush.Firebase

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.conkers.mensajespush.MainActivity
import com.conkers.mensajespush.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Procesar notificación estándar
        remoteMessage.notification?.let {
            val title = it.title ?: "Mensaje FCM"
            val body = it.body ?: "Sin contenido"
            mostrarNotificacion(title, body)
        }

        // Procesar datos personalizados
        if (remoteMessage.data.isNotEmpty()) {
            val mensajePersonalizado = remoteMessage.data["mensaje"]
            mensajePersonalizado?.let {
                Log.d("FCM", "Mensaje de datos: $mensajePersonalizado")
                mostrarNotificacion("Mensaje de datos", mensajePersonalizado)
            }
        }
    }

    private fun mostrarNotificacion(title: String, message: String) {
        val notificationId = 101

        // Crear PendingIntent para abrir MainActivity al tocar la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación
        val builder = NotificationCompat.Builder(this, "canal_id")
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Verificar permiso en dispositivos Android 13 (API 33) y superiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat.from(this).notify(notificationId, builder.build())
            } else {
                Log.w("Notificación", "El permiso POST_NOTIFICATIONS no está concedido.")
            }
        } else {
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        }
    }
}

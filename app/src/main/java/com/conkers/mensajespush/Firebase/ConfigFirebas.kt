package com.conkers.mensajespush.Firebase

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.conkers.mensajespush.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Si el mensaje trae una notificación (payload de notificación)
        remoteMessage.notification?.let {
            val title = it.title ?: "Mensaje FCM"
            val body = it.body ?: "Sin contenido"
            mostrarNotificacion(title, body)
        }

        // Si el mensaje trae datos
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

        // Se crea un PendingIntent para abrir la MainActivity al tocar la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "canal_id")
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        //NotificationManagerCompat.from(this).notify(notificationId, builder.build())
    }
}
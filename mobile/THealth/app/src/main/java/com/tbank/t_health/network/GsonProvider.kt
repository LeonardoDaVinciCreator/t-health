
package com.tbank.t_health.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import java.time.LocalDateTime

object GsonProvider {
    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonSerializer<LocalDateTime> { src, _, _ ->
                    // Отправляем БЕЗ Z — сервер всё равно принимает
                    JsonPrimitive(src.toString())
                }
            )
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonDeserializer<LocalDateTime> { json, _, _ ->
                    var text = json.asString
                    // Удаляем Z на конце, если есть
                    if (text.endsWith("Z")) {
                        text = text.dropLast(1)
                    }
                    // Также обрежем, если слишком много наносекунд (Gson иногда путает)
                    if (text.length > 23) {
                        // Оставляем до миллисекунд: yyyy-MM-ddTHH:mm:ss.SSS
                        text = text.substring(0, 23)
                    }
                    LocalDateTime.parse(text)
                }
            )
            .create()
    }
}
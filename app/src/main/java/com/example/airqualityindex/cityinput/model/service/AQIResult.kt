package com.example.airqualityindex.cityinput.model.service

import androidx.compose.ui.text.toLowerCase
import com.example.airqualityindex.cityinput.model.AQIData
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

sealed class AQIResult {
    data class Success(val data: AQIData): AQIResult()
    data class Error(val message: String): AQIResult()
}

class AQIResultDeserializer: JsonDeserializer<AQIResult> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AQIResult {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid AQI Result JSON")

        if (jsonObject.get("status").asString.lowercase() == "ok") {
            context?.deserialize<AQIData>(jsonObject.get("data"), AQIData::class.java)?.let {
                return AQIResult.Success(it)
            }
        }

        val message = jsonObject.get("data").asString ?: "Invalid AQI data"

        return AQIResult.Error(message)
    }

}
package com.example.airqualityindex.cityinput.model.service

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate

class LocalDateDeserializer: JsonDeserializer<LocalDate> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        return LocalDate.parse(json?.asString)
    }
}
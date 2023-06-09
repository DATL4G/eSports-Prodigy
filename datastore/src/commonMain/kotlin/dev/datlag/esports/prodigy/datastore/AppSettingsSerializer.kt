package dev.datlag.esports.prodigy.datastore

import androidx.datastore.core.Serializer
import dev.datlag.esports.prodigy.datastore.preferences.AppSettings
import java.io.InputStream
import java.io.OutputStream

class AppSettingsSerializer : Serializer<AppSettings> {

    override val defaultValue: AppSettings
        get() = AppSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            AppSettings.parseDelimitedFrom(input)
        } catch (ignored: Throwable) {
            try {
                AppSettings.parseFrom(input)
            } catch (ignored: Throwable) {
                try {
                    AppSettings.parseFrom(input.readBytes())
                } catch (ignored: Throwable) {
                    defaultValue
                }
            }
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        try {
            t.writeDelimitedTo(output)
        } catch (ignored: Throwable) {
            try {
                output.write(t.toByteArray())
            } catch (ignored: Throwable) { }
        }
    }
}
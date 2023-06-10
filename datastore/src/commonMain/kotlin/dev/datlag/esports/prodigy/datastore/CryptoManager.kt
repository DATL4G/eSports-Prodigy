package dev.datlag.esports.prodigy.datastore

import java.io.InputStream
import java.io.OutputStream

expect class CryptoManager {

    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray

    fun decrypt(inputStream: InputStream): ByteArray
}
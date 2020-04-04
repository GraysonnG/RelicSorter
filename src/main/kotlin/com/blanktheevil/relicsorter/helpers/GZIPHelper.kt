package com.blanktheevil.relicsorter.helpers

import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class GZIPHelper {
  companion object {
    private fun zipString(content: String): ByteArray {
      val bos = ByteArrayOutputStream()
      GZIPOutputStream(bos).bufferedWriter(StandardCharsets.UTF_8).use {
        it.write(content)
      }
      return bos.toByteArray()
    }

    private fun unzipString(content: ByteArray): String {
      return GZIPInputStream(content.inputStream()).bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
    }

    fun loadDataFromFile(file: File): String {
      return unzipString(file.readBytes())
    }

    fun saveDataToFile(file: File, content: String) {
      file.writeBytes(zipString(content))
    }
  }
}
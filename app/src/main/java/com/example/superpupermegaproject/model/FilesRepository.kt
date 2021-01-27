package com.example.superpupermegaproject.model

import android.content.Context
import timber.log.Timber
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FilesRepository(private val appContext: Context) {

    private val fileLogName = "Log.txt"

    fun writeToFileLog(s: String) {
        val fileLogPath = "${appContext.cacheDir}/$fileLogName"

        val sb = StringBuilder(readFile(fileLogPath))
            .append(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
            )
            .append(" ")
            .append(s)

        writeFile(fileLogPath, sb.toString())

    }

    fun writeFile(filePath: String, content: String) {
        try {
            val file = File(filePath)
            // отрываем поток для записи
            val writer = file.bufferedWriter()
            // пишем данные
            writer.write(content)
            // закрываем поток
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readFile(filePath: String): String {
        val sb = StringBuilder()

        try {
            val file = File(filePath)
            // открываем поток для чтения
            for(line in file.readLines()) {
                sb.appendLine(line)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return sb.toString()
    }

}
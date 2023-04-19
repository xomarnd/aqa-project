package com.mex.s3

import com.mex.s3.property.S3Properties
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class S3SetProperties(filePatch: String) {
    private val json = Json { ignoreUnknownKeys = true }
    private val s3PropertiesFile = File(filePatch)
    private val s3PropertiesContent = s3PropertiesFile.readText(Charsets.UTF_8)
    private val s3SerializableProp = json.decodeFromString<S3Properties>(s3PropertiesContent)

    val s3FileStorage = S3FileStorage(s3SerializableProp)
}

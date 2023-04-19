package com.mex.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.mex.s3.property.S3Properties
import java.io.ByteArrayInputStream
import java.time.Instant
import java.util.Date

class S3FileStorage(
    private val properties: S3Properties,
) {

    private val client: AmazonS3Client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    properties.accessKey,
                    properties.secretKey,
                ),
            ),
        )
        .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(properties.endpoint, null))
        .withPathStyleAccessEnabled(true)
        .build() as AmazonS3Client

    fun putObject(key: String, byteArray: ByteArray) {
        val meta = ObjectMetadata().also { objMeta ->
            objMeta.contentLength = byteArray.size.toLong()
        }
        val putObjectRequest = PutObjectRequest(
            properties.rootBucket,
            properties.keyPrefix + key,
            ByteArrayInputStream(byteArray),
            meta,
        )

        client.putObject(putObjectRequest)
    }

    fun deleteObject(key: String) {
        val deleteObjectRequest = DeleteObjectRequest(
            properties.rootBucket,
            properties.keyPrefix + key,
        )
        client.deleteObject(deleteObjectRequest)
    }

    fun getPresignedUrl(key: String): String {
        val date = Date(Instant.now().toEpochMilli() + properties.presignedUrlTtl)
        return client.generatePresignedUrl(properties.rootBucket, properties.keyPrefix + key, date)
            .toString()
            .replace(properties.endpoint, properties.presignedUrlEndpoint)
            .substringBefore("?")
    }
}

package com.mex.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import com.mex.s3.property.S3Properties
import java.net.URL
import java.time.Duration
import java.util.UUID

@Testcontainers
internal class S3SetPropertiesFileStorageTest {

    companion object {
        private const val ROOT_BUCKET = "test-bucket"
        private const val KEY_PREFIX = "test-prefix"

        @Container
        private val localstack = LocalStackContainer(DockerImageName.parse("localstack/localstack:1.2"))
            .withServices(LocalStackContainer.Service.S3)!!
    }

    private val properties = S3Properties(
        localstack.accessKey,
        localstack.secretKey,
        ROOT_BUCKET,
        KEY_PREFIX,
        localstack.getEndpointOverride(LocalStackContainer.Service.S3).toString(),
        36000,
        localstack.getEndpointOverride(LocalStackContainer.Service.S3).toString()
            .replace("127.0.0.1", "localhost")
    )

    private val s3FileStorage = S3FileStorage(properties)

    private val client = (
        AmazonS3ClientBuilder
        .standard()
        .withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    localstack.accessKey,
                    localstack.secretKey,
                ),
            ),
        )
        .withEndpointConfiguration(
            AwsClientBuilder.EndpointConfiguration(
                localstack.getEndpointOverride(LocalStackContainer.Service.S3).toString(),
                null,
            )
        )
        .withPathStyleAccessEnabled(true)
        .build() as AmazonS3Client
    )
        .apply {
            if (!doesBucketExistV2(ROOT_BUCKET)) {
                createBucket(ROOT_BUCKET)
            }
        }

    @Test
    fun `upload file to storage`() {
        val fileName = "test_image.jpg"
        val fileBytes = S3SetPropertiesFileStorageTest::class.java.classLoader.getResourceAsStream(fileName)!!
            .readAllBytes()

        val key = "/${UUID.randomUUID()}-$fileName"
        s3FileStorage.putObject(key, fileBytes)

        val keyWithPrefix = KEY_PREFIX + key
        val contentFromS3 = client.getObject(ROOT_BUCKET, keyWithPrefix).objectContent
            .readAllBytes()

        contentFromS3 shouldBe fileBytes
    }

    @Test
    fun `download from presigned url`() {
        val fileName = "test_image.jpg"
        val fileBytes = S3SetPropertiesFileStorageTest::class.java.classLoader.getResourceAsStream(fileName)!!
            .readAllBytes()

        val key = "/${UUID.randomUUID()}-$fileName"
        s3FileStorage.putObject(key, fileBytes)

        val presignedUrl = s3FileStorage.getPresignedUrl(key)
        presignedUrl shouldStartWith properties.presignedUrlEndpoint

        val downloadedFile = URL(presignedUrl)
            .openStream()
            .readAllBytes()

        downloadedFile shouldBe fileBytes
    }

    @Test
    fun `delete existed file`() {
        val fileName = "test_image.jpg"
        val fileBytes = S3SetPropertiesFileStorageTest::class.java.classLoader.getResourceAsStream(fileName)!!
            .readAllBytes()

        val key = "/${UUID.randomUUID()}-$fileName"
        val keyWithPrefix = KEY_PREFIX + key

        s3FileStorage.putObject(key, fileBytes)
        client.doesObjectExist(ROOT_BUCKET, keyWithPrefix) shouldBe true

        s3FileStorage.deleteObject(key)
        client.doesObjectExist(ROOT_BUCKET, keyWithPrefix) shouldBe false
    }
}

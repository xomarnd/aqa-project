package com.mex.s3.property

import kotlinx.serialization.Serializable

@Serializable
data class S3Properties(
    val  accessKey: String,
    val  secretKey: String,
    val  rootBucket: String,
    val  keyPrefix: String,
    val  endpoint: String,
    val  presignedUrlTtl: Long,
    val  presignedUrlEndpoint: String
)
package org.mex.restassured.users

class UserTimeResponse(
    name: String,
    job: String,
    val updatedAt: String
) : UserTime(
    name,
    job
)
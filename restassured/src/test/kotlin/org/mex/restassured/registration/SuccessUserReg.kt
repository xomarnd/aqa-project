package org.mex.restassured.registration

data class SuccessUserReg(
    var id: Int? = null,
    var token: String? = null,
    override var email: String? = null,
    override var password: String? = null
) : Register(
    email,
    password
)
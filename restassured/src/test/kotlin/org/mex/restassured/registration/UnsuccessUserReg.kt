package org.mex.restassured.registration

data class UnsuccessUserReg(
    var error: String? = null,
    override var email: String? = null,
    override var password: String? = null
) : Register(
    email,
    password
)
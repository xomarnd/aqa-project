package org.mex.testutils

import java.util.UUID

class UUIDGenerator {

    fun generate(): UUID = UUID.randomUUID()
    fun generateAndToString(): String = UUID.randomUUID().toString()
}

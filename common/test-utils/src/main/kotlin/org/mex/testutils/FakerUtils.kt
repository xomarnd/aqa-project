package org.mex.testutils

import com.github.javafaker.Faker

val faker = Faker()

inline fun <reified T : Enum<T>> rndEnum(): T = T::class.java.enumConstants.let {
    it[faker.random().nextInt(it.size)]
}

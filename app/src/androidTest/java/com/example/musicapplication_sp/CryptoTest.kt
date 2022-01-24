package com.example.musicapplication_sp

import com.example.musicapplication_sp.Cryptography.Cryptography
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.crypto.spec.SecretKeySpec

class CryptoTest {
    @Test
    fun testCrypto() {
        val crypto = Cryptography()
        val key: SecretKeySpec = crypto.createSecretKey("AES")
        val string = "test"
        val map: MutableMap<String, ByteArray> = crypto.encrypt(string.toByteArray(), key)

        val result = crypto.decrypt(map, key)
        assertEquals(result, "test")
    }
}
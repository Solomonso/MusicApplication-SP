package com.example.musicapplication_sp

import com.example.musicapplication_sp.cryptography.Cryptography
import org.junit.Assert.assertEquals
import org.junit.Test
import java.security.KeyStore
import java.util.*

class CryptoTest {
    @Test
    fun testCrypto() {
        val crypto = Cryptography()
        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
        val alias = "clientIDKey"
        crypto.createSecretKey("AES")
        val string = "test"
        val entry = ks.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        val map: MutableMap<String, ByteArray> = crypto.encrypt(string.toByteArray())

        val result = entry?.let { crypto.decrypt(map, it.secretKey) }


        assertEquals(result, "test")
        //assertNotEquals(entry, null)
    }
}
package com.example.musicapplication_sp

import com.example.musicapplication_sp.Cryptography.Cryptography
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.security.KeyStore
import java.util.*
import javax.crypto.spec.SecretKeySpec

class CryptoTest {
    @Test
    fun testCrypto() {
        val crypto = Cryptography()
        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
        val aliases: Enumeration<String> = ks.aliases()
        val alias = "clientIDKey"
        val entry = ks.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        crypto.createSecretKey("AES")
        val string = "test"
        val map: MutableMap<String, ByteArray> = crypto.encrypt(string.toByteArray())

        val result = entry?.let { crypto.decrypt(map, it.secretKey) }


        assertEquals(result, "test")
        //assertNotEquals(entry, null)
    }
}
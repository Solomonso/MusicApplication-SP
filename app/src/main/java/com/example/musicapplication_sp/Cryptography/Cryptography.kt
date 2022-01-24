package com.example.musicapplication_sp.Cryptography

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

open class Cryptography {

    fun createSecretKey(algo: String): SecretKeySpec {
        // create new key
        val algorithm: String
        if (algo.isEmpty()) {
            algorithm = "AES"
        } else {
            algorithm = algo
        }
        val random = SecureRandom()
        val salt = ByteArray(256)
        random.nextBytes(salt)
        val secretKey: SecretKey = KeyGenerator.getInstance(algorithm).generateKey()
        // get base64 encoded version of the key
        val encodedKey = secretKey.encoded
        return SecretKeySpec(encodedKey, algorithm)
    }

    fun encrypt(
        plainByteArray: ByteArray,
        secretKey: SecretKey
    ): MutableMap<String, ByteArray> {
        val ivRandom = SecureRandom() //not caching previous seeded instance of SecureRandom
        val map: MutableMap<String, ByteArray>
        map = HashMap()
        val iv = ByteArray(16)
        ivRandom.nextBytes(iv)
        val ivSpec = IvParameterSpec(iv) // 2

        val cipher = Cipher.getInstance("AES/CTR/NoPadding") // 1
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encrypted = cipher.doFinal(plainByteArray) // 2
        map["iv"] = iv
        map["encrypted"] = encrypted
        return map

    }

    fun decrypt(map: MutableMap<String, ByteArray>, secretKeySpec: SecretKeySpec): String {
        val iv = map["iv"]
        val encrypted = map["encrypted"]
        val ivSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/CTR/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
        val decrypted = cipher.doFinal(encrypted)
        val sb = StringBuilder()
        for (b in decrypted) {
            sb.append(b.toInt().toChar())
        }
        return sb.toString()
    }

}

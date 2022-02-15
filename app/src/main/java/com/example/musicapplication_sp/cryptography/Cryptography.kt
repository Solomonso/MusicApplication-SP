package com.example.musicapplication_sp.cryptography

import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import java.io.IOException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
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
        var keyStore: KeyStore?
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            keyStore.setEntry(
                "clientIDKey",
                KeyStore.SecretKeyEntry(secretKey),
                KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CTR)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        } catch (e : KeyStoreException) {
            e.printStackTrace();
        } catch (e : CertificateException) {
            e.printStackTrace();
        } catch (e : NoSuchAlgorithmException) {
            e.printStackTrace();
        } catch (e : IOException) {
            e.printStackTrace();
        }

        return SecretKeySpec(encodedKey, algorithm)
    }

    fun encrypt(plainByteArray: ByteArray //secretKey: SecretKey
    ): MutableMap<String, ByteArray> {
        val ivRandom = SecureRandom() //not caching previous seeded instance of SecureRandom
        val map: MutableMap<String, ByteArray>
        map = HashMap()
        var iv = ByteArray(16)
        ivRandom.nextBytes(iv)
        val ivSpec = IvParameterSpec(iv) // 2
        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        val secretKey : SecretKey = ks.getKey("clientIDKey",null) as SecretKey
        //secretKey.g
        val cipher = Cipher.getInstance("AES/CTR/NoPadding") // 1
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        var ivParams = cipher.getParameters().getParameterSpec(IvParameterSpec::class.java)
        iv = ivParams.getIV()
        val encrypted = cipher.doFinal(plainByteArray) // 2
        map["iv"] = iv
        map["encrypted"] = encrypted

        return map

    }

    fun decrypt(map: MutableMap<String, ByteArray>, secretKeySpec: SecretKey): String {
        val iv = map["iv"]
        val encrypted = map["encrypted"]
        val sb2 = StringBuilder()
        if (encrypted != null) {
            for (b in encrypted) {
                sb2.append(b.toInt().toChar())
            }
        }
        val test = sb2.toString()
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

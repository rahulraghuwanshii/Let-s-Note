package com.rahulraghuwanshi.letsnote.feature_notes.data.data_source

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.crypto.KeyGenerator

class PassphraseUtils(private val context: Context) {

    private val ENCRYPTED_SHARED_PREFS_FILENAME =
        "com.rahulraghuwanshi.letsnote.feature_notes" // Choose a unique name!
    private val PASSPHRASE_KEY = "PASSPHRASE"
    private val ENCRYPTION_ALGORITHM = "AES"
    private val PASSPHRASE_KEY_SIZE = 256

    fun getPassphraseOrGenerateIfMissing() = getPassphrase() ?: initializePassphrase()

    /**
     * Retrieves the passphrase for encryption from the encrypted shared preferences.
     * Returns null if there is no stored passphrase.
     */
    private fun getPassphrase(): Passphrase? {
        val passphraseString = encryptedSharedPreferences()
            .getString(PASSPHRASE_KEY, null)
        return passphraseString?.let { Passphrase(it.toByteArray(Charsets.ISO_8859_1)) }
    }

    /**
     * Returns a reference to the encrypted shared preferences.
     */
    private fun encryptedSharedPreferences(): SharedPreferences {
        val masterKey =
            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREFS_FILENAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Generates a passphrase and stores it in the encrypted shared preferences.
     * Returns the newly generated passphrase.
     */
    private fun initializePassphrase(): Passphrase {
        val passphrase = generateRandomPassphrase()

        encryptedSharedPreferences().edit(commit = true) {
            putString(PASSPHRASE_KEY, passphrase.toString(Charsets.ISO_8859_1))
        }

        return Passphrase(value = passphrase)
    }

    /**
     * Generates and returns a passphrase.
     */
    private fun generateRandomPassphrase(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM)
        keyGenerator.init(PASSPHRASE_KEY_SIZE)
        return keyGenerator.generateKey().encoded
    }

    data class Passphrase(val value: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Passphrase

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int {
            return value.contentHashCode()
        }
    }
}
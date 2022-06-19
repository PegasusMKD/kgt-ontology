package finki.ukim.kgt.kgtontology.utility

import finki.ukim.kgt.kgtontology.utility.EncodeUtils.encodeHex
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest

@Component
class HashUtils(private val resourceLoader: ResourceLoader) {

    fun getCheckSumFromResourceFile(digest: MessageDigest, filePath: String): String {
        val resource: Resource = resourceLoader.getResource("classpath:${filePath}")
        val file: File = resource.file
        return getCheckSumFromResourceFile(digest, file)
    }

    fun getCheckSumFromResourceFile(digest: MessageDigest, file: File): String {
        val fis = FileInputStream(file)
        val byteArray = updateDigest(digest, fis).digest()
        fis.close()
        val hexCode = encodeHex(byteArray, true)
        return String(hexCode)
    }

    /**
     * Reads through an InputStream and updates the digest for the data
     *
     * @param digest The MessageDigest to use (e.g. MD5)
     * @param data Data to digest
     * @return the digest
     */
    private fun updateDigest(digest: MessageDigest, data: InputStream): MessageDigest {
        val buffer = ByteArray(STREAM_BUFFER_LENGTH)
        var read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
        while (read > -1) {
            digest.update(buffer, 0, read)
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH)
        }
        return digest
    }

    companion object {
        const val STREAM_BUFFER_LENGTH = 1024
    }

}
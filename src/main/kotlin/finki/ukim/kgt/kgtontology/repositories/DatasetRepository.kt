package finki.ukim.kgt.kgtontology.repositories

import finki.ukim.kgt.kgtontology.models.Dataset
import finki.ukim.kgt.kgtontology.utility.enums.MessageDigestAlgorithm
import org.springframework.data.jpa.repository.JpaRepository

interface DatasetRepository : JpaRepository<Dataset, String> {
    fun existsByChecksumAndHashAlgorithm(checksum: String, hashAlgorithm: MessageDigestAlgorithm): Boolean;
}
package finki.ukim.kgt.kgtontology.models

import finki.ukim.kgt.kgtontology.utility.enums.MessageDigestAlgorithm
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Dataset(

    @Id
    @Column(name = "id", length = 32)
    @GeneratedValue(generator = "strategy-uuid")
    @GenericGenerator(name = "strategy-uuid", strategy = "uuid")
    private val id: String? = null,

    @Column(name = "checksum", length = 150)
    private val checksum: String? = null,

    @Column(name = "version", length = 15)
    private val version: String? = null,

    @Column(name = "populatedAt")
    private val populatedAt: LocalDate? = null,

    @Column(name = "recordCount")
    private val recordCount: Int? = null,

    @Column(name = "hashAlgorithm")
    private val hashAlgorithm: MessageDigestAlgorithm? = null
)

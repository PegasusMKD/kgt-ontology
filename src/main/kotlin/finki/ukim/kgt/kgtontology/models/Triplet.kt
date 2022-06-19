package finki.ukim.kgt.kgtontology.models

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
data class Triplet(
    @Id
    @Column(name = "id", length = 32)
    @GeneratedValue(generator = "strategy-uuid")
    @GenericGenerator(name = "strategy-uuid", strategy = "uuid")
    var id: String? = null,

    @Column(name = "subject")
    var subject: String? = null,

    @Column(name = "subjectURI")
    var subjectURI: String? = null,

    @Column(name = "predicate")
    var predicate: String? = null,

    @Column(name = "object")
    var objectVarChar: String? = null,

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "objectText", columnDefinition = "TEXT")
    var objectText: String? = null

)

package finki.ukim.kgt.kgtontology.dtos

data class TripletDto(
    var id: String? = null,
    var subject: String? = null,
    var predicate: String? = null,
    var `object`: String? = null
)

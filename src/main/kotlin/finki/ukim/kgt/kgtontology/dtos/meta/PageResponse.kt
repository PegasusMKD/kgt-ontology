package finki.ukim.kgt.kgtontology.dtos.meta

class PageResponse<T>(val totalPages: Int, val totalElements: Int, val content: Collection<T>?) {
    constructor(totalPages: Int, totalElements: Long, content: List<T>?) : this(
        totalPages,
        totalElements.toInt(),
        content
    )
}
package finki.ukim.kgt.kgtontology.dtos

import finki.ukim.kgt.kgtontology.dtos.meta.LazyLoadEvent
import org.springframework.data.domain.Pageable

data class AutoCompleteQuery<T>(
    var example: T? = null,
    var searchTerm: String? = null,
    val lazyLoadEvent: LazyLoadEvent? = null
) {
    fun toPageable(): Pageable {
        return lazyLoadEvent?.toPageable() ?: Pageable.unpaged()
    }
}
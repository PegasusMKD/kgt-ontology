package finki.ukim.kgt.kgtontology.dtos.meta

import org.springframework.data.domain.Pageable

class PageRequestByExample<DTO> {
    val example: DTO? = null
    private val lazyLoadEvent: LazyLoadEvent? = null

    fun toPageable(): Pageable {
        return lazyLoadEvent?.toPageable() ?: Pageable.unpaged()
    }
}
package finki.ukim.kgt.kgtontology.dtos.meta

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class LazyLoadEvent {
    /**
     * First row offset.
     */
    var first = 0

    /**
     * Number of rows per page.
     */
    var rows = 0
    var sortField: String? = null
    var sortFields: Array<String> = arrayOf()
    var sortOrder = 0
    fun toPageable(): Pageable {
        return if (sortField != null) {
            PageRequest.of(toPageIndex(), rows, toSortDirection(), sortField)
        } else if (sortFields.isNotEmpty()) {
            PageRequest.of(toPageIndex(), rows, toSortDirection(), *sortFields)
        } else {
            PageRequest.of(toPageIndex(), rows)
        }
    }

    /**
     * Zero based page index.
     */
    fun toPageIndex(): Int {
        return if (rows > 0) (first + rows) / rows - 1 else 1
    }

    fun toSortDirection(): Sort.Direction {
        return if (sortOrder == 1) Sort.Direction.ASC else Sort.Direction.DESC
    }
}
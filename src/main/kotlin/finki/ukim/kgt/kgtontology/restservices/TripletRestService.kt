package finki.ukim.kgt.kgtontology.restservices

import com.querydsl.core.types.Predicate
import finki.ukim.kgt.kgtontology.dtos.AutoCompleteQuery
import finki.ukim.kgt.kgtontology.dtos.TripletDto
import finki.ukim.kgt.kgtontology.mappers.TripletMapper
import finki.ukim.kgt.kgtontology.models.QTriplet
import finki.ukim.kgt.kgtontology.services.TripletService
import finki.ukim.kgt.kgtontology.utility.OptionalBooleanBuilder
import org.apache.commons.lang.StringUtils
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TripletRestService(
    private val tripletService: TripletService,
    private val tripletMapper: TripletMapper
) {

    fun findAll(predicate: Predicate): List<TripletDto> {
        return tripletService.findAll(predicate, Pageable.unpaged())
            .content.map(tripletMapper::toDto)
    }

    fun findSubjectsWithSearchTerm(autoCompleteQuery: AutoCompleteQuery<TripletDto>): List<String?> {
        val termPredicate = makeSubjectsFilterWithSearchTerm(autoCompleteQuery.searchTerm)
        val page = tripletService.findAll(termPredicate, Pageable.unpaged())
        return page.content.map { it.subject }.toSet().take(autoCompleteQuery.lazyLoadEvent?.rows ?: 20)
    }

    fun makeSubjectsFilterWithSearchTerm(searchTerm: String?): Predicate {
        val qTriplet = QTriplet.triplet
        val opBuilder = OptionalBooleanBuilder.builder(qTriplet.isNotNull)
        if (StringUtils.isBlank(searchTerm))
            return opBuilder.build()

        return opBuilder
            .notNullAnd(qTriplet.subject::startsWith, searchTerm)
            .build()
    }

}
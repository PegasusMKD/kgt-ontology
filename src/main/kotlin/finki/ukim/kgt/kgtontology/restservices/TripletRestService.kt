package finki.ukim.kgt.kgtontology.restservices

import finki.ukim.kgt.kgtontology.dtos.TripletDto
import finki.ukim.kgt.kgtontology.mappers.TripletMapper
import finki.ukim.kgt.kgtontology.services.TripletService
import org.springframework.stereotype.Service

@Service
class TripletRestService(
    private val tripletService: TripletService,
    private val tripletMapper: TripletMapper
) {

    fun findAll(): List<TripletDto> {
        return tripletService.findAll().map(tripletMapper::toDto)
    }

}
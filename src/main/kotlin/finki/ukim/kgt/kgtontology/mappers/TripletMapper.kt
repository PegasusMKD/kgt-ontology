package finki.ukim.kgt.kgtontology.mappers

import finki.ukim.kgt.kgtontology.dtos.TripletDto
import finki.ukim.kgt.kgtontology.models.Triplet
import org.mapstruct.Mapper
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
interface TripletMapper {
    fun toDto(triplet: Triplet): TripletDto
    fun toEntity(triplet: Triplet): TripletDto
}
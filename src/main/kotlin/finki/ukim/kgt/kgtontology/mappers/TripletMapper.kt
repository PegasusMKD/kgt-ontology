package finki.ukim.kgt.kgtontology.mappers

import finki.ukim.kgt.kgtontology.dtos.TripletDto
import finki.ukim.kgt.kgtontology.models.Triplet
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class TripletMapper {
    @Mapping(target = "object", ignore = true)
    abstract fun toDto(triplet: Triplet): TripletDto

    abstract fun toEntity(triplet: TripletDto): Triplet

    @AfterMapping
    fun mapObject(triplet: Triplet, @MappingTarget dto: TripletDto) {
        dto.`object` = triplet.objectVarChar ?: triplet.objectText
    }
}
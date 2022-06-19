package finki.ukim.kgt.kgtontology.controllers

import com.querydsl.core.types.Predicate
import finki.ukim.kgt.kgtontology.dtos.AutoCompleteQuery
import finki.ukim.kgt.kgtontology.dtos.TripletDto
import finki.ukim.kgt.kgtontology.models.Triplet
import finki.ukim.kgt.kgtontology.restservices.TripletRestService
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/triplets")
class TripletController(private val tripletRestService: TripletRestService) {

    @GetMapping
    fun findAll(
        @QuerydslPredicate(root = Triplet::class) tripletPredicate: Predicate
    ): ResponseEntity<List<TripletDto?>> {
        return ResponseEntity.ok(tripletRestService.findAll(tripletPredicate))
    }


    @PostMapping
    fun findAllWithSearchTerm(
        @RequestBody autoCompleteQuery: AutoCompleteQuery<TripletDto>
    ): ResponseEntity<List<String?>> {
        return ResponseEntity.ok(tripletRestService.findSubjectsWithSearchTerm(autoCompleteQuery))
    }

}
package finki.ukim.kgt.kgtontology.controllers

import finki.ukim.kgt.kgtontology.dtos.TripletDto
import finki.ukim.kgt.kgtontology.restservices.TripletRestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/triplets")
class TripletController(private val tripletRestService: TripletRestService) {

    @PostMapping("/all")
    fun findAll(): ResponseEntity<List<TripletDto>> {
        return ResponseEntity.ok(tripletRestService.findAll())
    }

}
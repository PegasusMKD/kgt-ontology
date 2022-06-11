package finki.ukim.kgt.kgtontology.repositories

import finki.ukim.kgt.kgtontology.models.Triplet
import org.springframework.data.jpa.repository.JpaRepository

interface TripletRepository: JpaRepository<Triplet, String> {

}
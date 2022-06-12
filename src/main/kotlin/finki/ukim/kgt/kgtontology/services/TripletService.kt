package finki.ukim.kgt.kgtontology.services

import com.querydsl.core.types.Predicate
import finki.ukim.kgt.kgtontology.models.Triplet
import finki.ukim.kgt.kgtontology.repositories.TripletRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TripletService(private val tripletRepository: TripletRepository) {

    private val logger = LoggerFactory.getLogger(TripletService::class.java)

    fun findAll(predicate: Predicate): List<Triplet> {
        return tripletRepository.findAll(predicate).toList()
    }

    fun saveAll(triplets: List<Triplet>?): Int {
        val failed = mutableListOf<Triplet>()
        var ctr = 0
        triplets?.forEach {
            try {
                tripletRepository.saveAndFlush(it)
                ctr++
            } catch (e: Exception) {
                failed.add(it)
            }
        } ?: throw RuntimeException("Triplet issues.")

        if(failed.isNotEmpty()) {
            logger.warn("Problematic items:")
            failed.forEach { logger.warn(it.toString()) }
        }

        return ctr
//        return triplets?.let { tripletRepository.saveAllAndFlush(it).size } ?: throw RuntimeException("Triplet issues.")
    }

}
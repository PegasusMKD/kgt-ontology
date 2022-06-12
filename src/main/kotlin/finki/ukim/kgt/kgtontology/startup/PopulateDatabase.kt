package finki.ukim.kgt.kgtontology.startup

import finki.ukim.kgt.kgtontology.main
import finki.ukim.kgt.kgtontology.models.Dataset
import finki.ukim.kgt.kgtontology.repositories.DatasetRepository
import finki.ukim.kgt.kgtontology.services.TripletService
import finki.ukim.kgt.kgtontology.utility.HashUtils
import finki.ukim.kgt.kgtontology.utility.JenaUtils
import finki.ukim.kgt.kgtontology.utility.enums.MessageDigestAlgorithm
import org.apache.jena.rdf.model.ModelFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate
import javax.annotation.PostConstruct

@Component
class PopulateDatabase(
    private val hashUtils: HashUtils,
    private val datasetRepository: DatasetRepository,
    private val tripletService: TripletService,
    private val jenaUtils: JenaUtils,
    private val resourceLoader: ResourceLoader
) {

    @Value("\${kgt.ontology.dataset.filename}")
    private lateinit var fileName: String

    @Value("\${kgt.ontology.dataset.sub-models.location}")
    private lateinit var subModelsLocation: String

    @Value("\${kgt.ontology.dataset.checksum.strategy:SHA-256}")
    private lateinit var checkSumStrategy: String

    @Async
    @PostConstruct
    fun populate() {
        // Calculate checksum of OWL file
        val hashAlgorithm = MessageDigestAlgorithm.fromLabel(checkSumStrategy)

        val checkSum = hashUtils.getCheckSumFromResourceFile(
            hashAlgorithm.getMessageDigest(),
            fileName
        )

        // Check if executed
        if (datasetRepository.existsByChecksumAndHashAlgorithm(
                checkSum,
                hashAlgorithm
            )
        ) {
            return
        }

        // Remove existing data
//        datasetRepository.deleteAll()

        // Read data from OWL dataset
        val mainModel = ModelFactory.createOntologyModel()
        val file = resourceLoader.getResource("classpath:${fileName}")
        mainModel.read(
            file.inputStream,
            "RDF/XML"
        )

        resourceLoader.getResource("classpath:$subModelsLocation/.").file.walk().forEach {
            if(it.isDirectory)
                return@forEach

            val subModel = ModelFactory.createOntologyModel()
            subModel.read(
                it.inputStream(),
                "RDF/XML"
            )

            mainModel.add(subModel)
        }

        // Transform data into models
        val triplets = jenaUtils.cleanseModel(mainModel)

        // Filter data that we only take useful items
        // TODO: Add filter

        // Save models
        val count = tripletService.saveAll(triplets)

        // Add checksum to check whether there are any changes in future file
        datasetRepository.saveAndFlush(
            Dataset(
                null,
                checkSum,
                createVersion(),
                LocalDate.now(),
                count,
                hashAlgorithm
            )
        )
    }

    private fun createVersion(): String? {
//        TODO("Not yet implemented")
        return "1.0.0"
    }

}
package finki.ukim.kgt.kgtontology.utility

import finki.ukim.kgt.kgtontology.models.Triplet
import org.apache.jena.graph.Graph
import org.apache.jena.graph.Node
import org.apache.jena.graph.NodeFactory
import org.apache.jena.graph.Triple
import org.apache.jena.graph.Triple.createMatch
import org.apache.jena.ontology.OntModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.regex.Matcher

@Component
class JenaUtils {

    private final val logger = LoggerFactory.getLogger(JenaUtils::class.java)

    fun cleanseModel(model: OntModel): List<Triplet> {
        //  Get a set of all subjects
        //  Start iterating through set of subjects
        //      For subject X find all triplets
        //      In found triplets, find the triplet with a predicate named 'label'
        //      Take the 'object' of the triplet with predicate 'label', and set it as the subject to all of those triplets
        //      Transfer transformed data to another set of triplets
        //  After iterating entire set, return newly created set of triplets
        val graph = model.graph
        val result = mutableSetOf<Triplet>()
        model.listSubjects().asSequence().toSet().stream().forEachOrdered { subject ->
            // If it isn't a URI, it's most probably an
            // "internal" subject,
            // blank node,
            // or something that we probably do not need
            val items = if (subject.isURIResource) updateWithLabel(graph, subject.uri.toString())
            else {
                if (!subject.asNode().isBlank) {
                    logger.warn("Subject node is unidentified resource: {}", subject.toString())
                }
                mutableListOf()
            }

            result.addAll(items)
        }

        return result.toList()
    }

    fun updateWithLabel(graph: Graph, subject: String): Collection<Triplet> {
        val triples = graph.find(
            createMatch(NodeFactory.createURI(subject), null, null)
        ).asSequence().toSet()
        val label = triples.find { it.predicate.localName == "label" }
        return triples.filter { it.predicate.localName != "label" && !it.`object`.isBlank }.map {
            val splitPredicate = splitHelper(it.predicate.localName, "[/#]")
            val cleansedObject = cleanObject(it.getObject())
            val actualLabel =
                if (label?.`object`?.literalValue == null) it.subject.toString()
                else label.`object`?.literalValue.toString()
            Triplet(
                null,
                actualLabel,
                splitHelper(it.subject.toString(), "[/#]"),
                cleanPredicate(splitPredicate),
                splitHelper(
                    cleansedObject.toString(), "[/#]"
                )
            )
        }
    }


    fun tripleToTriplet(triple: Triple): Triplet {
        val subject: String = splitHelper(triple.subject.toString(), "[/#]")
        val predicate: String = splitHelper(triple.predicate.localName, "[/#]")
        val `object`: String = splitHelper(cleanObject(triple.getObject()).toString(), "[/#]")

        return Triplet(null, subject, null, cleanPredicate(predicate), `object`)
    }

    fun cleansingTriplets(model: OntModel): List<Triplet>? {
        val cleansedTriplets = mutableListOf<Triplet>()
        val it = model.graph.find()

        while (it.hasNext()) {
            val t = it.next()
            cleansedTriplets.add(tripleToTriplet(t))
        }
        return cleansedTriplets
    }

    private fun cleanObject(node: Node?): String? {
        if (!node?.isConcrete!!) {
            logger.warn("Is not concrete: {}", node.literalValue)
        }

        if (node.isLiteral) {
            return node.literalValue.toString()
        } else if (node.isURI) {
            return node.uri
        } else if (node.isBlank) {
            return ""
        }

        return null
    }

    private fun cleanPredicate(predicate: String): String {
        var pred = predicate

        while (pred.contains("\"")) {
            pred = pred.replace("\"", "")
        }

        if (pred.contains("\\")) {
            pred = Matcher.quoteReplacement(pred)
        }

        if (pred.contains(ACCESSED)) {
            pred = splitHelper(pred, ACCESSED)
        }

        return pred
    }

    private fun splitHelper(arr: String, regex: String): String {
        val split = arr.split(regex).toTypedArray()
        return if (regex == ACCESSED) split[0] else split[split.size - 1]
    }

    companion object {
        const val ACCESSED = ". Accessed"
    }
}
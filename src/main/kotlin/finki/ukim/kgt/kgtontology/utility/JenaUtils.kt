package finki.ukim.kgt.kgtontology.utility

import finki.ukim.kgt.kgtontology.models.Triplet
import org.apache.jena.graph.Graph
import org.apache.jena.graph.Node
import org.springframework.stereotype.Component
import java.util.*
import java.util.regex.Matcher

@Component
class JenaUtils {

    fun cleansingTriplets(graph: Graph): List<Triplet>? {
        // TODO: Rework logic so we do the following
        //  Get a set of all subjects
        //  Start iterating through set of subjects
        //      For subject X find all triplets
        //      In found triplets, find the triplet with a predicate named 'label'
        //      Take the 'object' of the triplet with predicate 'label', and set it as the subject to all of those triplets
        //      Transfer transformed data to another set of triplets
        //  After iterating entire set, return newly created set of triplets

        val cleansedTriplets: MutableList<Triplet> = mutableListOf()
        val it = graph.find()
        while (it.hasNext()) {
            val t = it.next()
            val subject: String = splitHelper(t.subject.toString(), "[/#]")
            val predicate: String = splitHelper(t.predicate.localName, "[/#]")
            val `object`: String = splitHelper(cleanObject(t.getObject()).toString(), "[/#]")

            cleansedTriplets.add(Triplet(null, subject, cleanPredicate(predicate), `object`))
        }
        return cleansedTriplets
    }

    private fun cleanObject(node: Node?): String? {
        if (node?.isLiteral!!) {
            return node.literalValue.toString()
        } else if (node.isURI) {
            return node.uri
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
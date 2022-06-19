package finki.ukim.kgt.kgtontology.repositories

import com.querydsl.core.types.dsl.StringPath
import finki.ukim.kgt.kgtontology.models.QTriplet
import finki.ukim.kgt.kgtontology.models.Triplet
import finki.ukim.kgt.kgtontology.models.utility.TripletText
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings


interface TripletRepository : JpaRepository<Triplet, String>,
    QuerydslPredicateExecutor<Triplet>,
    QuerydslBinderCustomizer<QTriplet> {

    // Going with IDs since we might want to add pagination, so we can have the first call be paginated, and then,
    // just get the select for only paginated data
    @Query(
        "select new finki.ukim.kgt.kgtontology.models.utility.TripletText(t.id, t.objectText) " +
                "from Triplet t " +
                "where t.id in (:ids)"
    )
    fun findMissingObjects(ids: List<String>): List<TripletText>

    override fun customize(bindings: QuerydslBindings, root: QTriplet) {
        bindings.bind(String::class.java).first { path: StringPath, value: String? ->
            path.eq(value)
        }
    }

}

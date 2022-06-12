package finki.ukim.kgt.kgtontology.repositories

import com.querydsl.core.types.dsl.StringPath
import finki.ukim.kgt.kgtontology.models.QTriplet
import finki.ukim.kgt.kgtontology.models.Triplet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings


interface TripletRepository : JpaRepository<Triplet, String>,
    QuerydslPredicateExecutor<Triplet>,
    QuerydslBinderCustomizer<QTriplet> {

    override fun customize(bindings: QuerydslBindings, root: QTriplet) {
        bindings.bind(String::class.java).first { path: StringPath, value: String? ->
            path.containsIgnoreCase(
                value
            )
        }
    }

}

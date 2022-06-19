package finki.ukim.kgt.kgtontology.utility

import com.querydsl.core.types.dsl.BooleanExpression
import org.apache.commons.lang.StringUtils
import org.springframework.util.CollectionUtils
import java.util.function.Function


class OptionalBooleanBuilder private constructor(private val predicate: BooleanExpression) {
    fun rightAnd(right: OptionalBooleanBuilder): OptionalBooleanBuilder {
        return OptionalBooleanBuilder(predicate.and(right.build()))
    }

    fun <T> notNullAnd(expressionFunction: Function<T, BooleanExpression?>, value: T?): OptionalBooleanBuilder {
        return if (value != null) {
            OptionalBooleanBuilder(predicate.and(expressionFunction.apply(value)))
        } else this
    }

    fun <T> notNullOr(expressionFunction: Function<T, BooleanExpression?>, value: T?): OptionalBooleanBuilder {
        return if (value != null) {
            OptionalBooleanBuilder(predicate.or(expressionFunction.apply(value)))
        } else this
    }

    fun notEmptyAnd(expressionFunction: Function<String?, BooleanExpression?>, value: String?): OptionalBooleanBuilder {
        return if (!StringUtils.isEmpty(value)) {
            OptionalBooleanBuilder(predicate.and(expressionFunction.apply(value)))
        } else this
    }

    fun notEmptyOr(expressionFunction: Function<String?, BooleanExpression?>, value: String?): OptionalBooleanBuilder {
        return if (!StringUtils.isEmpty(value)) {
            OptionalBooleanBuilder(predicate.or(expressionFunction.apply(value)))
        } else this
    }

    fun notNullAndExpression(expression: BooleanExpression?): OptionalBooleanBuilder {
        return if (expression != null) {
            OptionalBooleanBuilder(predicate.and(expression))
        } else this
    }

    fun <T> notEmptyAnd(
        expressionFunction: Function<Collection<T?>?, BooleanExpression?>,
        collection: Collection<T?>?
    ): OptionalBooleanBuilder {
        return if (!CollectionUtils.isEmpty(collection)) {
            OptionalBooleanBuilder(predicate.and(expressionFunction.apply(collection)))
        } else this
    }

    fun build(): BooleanExpression {
        return predicate
    }

    companion object {
        fun builder(predicate: BooleanExpression): OptionalBooleanBuilder {
            return OptionalBooleanBuilder(predicate)
        }
    }
}
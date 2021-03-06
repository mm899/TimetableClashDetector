package Persistence.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(
    val name: String = "",
    val type: kotlin.Int = -1
)

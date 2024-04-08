data class Room(
    val id: Int,
    var name: String,
    var photo: String?,
    var description: String?,
    val tasks: MutableList<Task> = mutableListOf(),
    var components: MutableList<Component>? = mutableListOf()
)
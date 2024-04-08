data class User(
    var name: String,
    var email: String,
    var hashedPassword: String,
    val rooms: List<Room>?
)
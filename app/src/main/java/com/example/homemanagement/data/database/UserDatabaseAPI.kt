import org.litote.kmongo.coroutine.CoroutineClient
import java.security.MessageDigest

class UserDatabaseAPI(private val client: CoroutineClient) {

    private val database = client.getDatabase("UserDatabase")
    private val collection = database.getCollection<User>("UserInfo")

    suspend fun addUser(name: String, email: String, password: String, rooms: List<Room>?): Boolean {
        // Check if the user already exists
        val existingUser = collection.findOne("{email: '$email'}")
        if (existingUser != null) {
            println("User with email $email already exists")
            return false
        }
        val newUser = User(name, email, hashPassword(password), rooms)
        collection.insertOne(newUser)
        return true // Assuming successful insertion
    }

    suspend fun deleteUser(userId: String): Boolean {
        val existingUser = collection.findOne("{userId: '$userId'}")
        if (existingUser != null) {
            println("User with id $userId does not exist")
            return false
        }
        collection.deleteOneById(userId)
        return true
    }

    suspend fun changeUserPassword(userId: String, newPassword: String): Boolean {
        val targetUser = collection.findOneById(userId)
        targetUser?.let {
            targetUser.hashedPassword = hashPassword(newPassword)
            collection.replaceOneById(userId, targetUser)
            return true
        }
        return false
    }

    suspend fun changeUserName(userId: String, newName: String): Boolean {
        val targetUser = collection.findOneById(userId)
        targetUser?.let {
            targetUser.name = newName
            collection.replaceOneById(userId, targetUser)
            return true
        }
        return false
    }

    private fun hashPassword(password: String): String {
        // Create MessageDigest instance for SHA-256
        val digest = MessageDigest.getInstance("SHA-256")

        // Apply SHA-256 hashing to the password
        val hashBytes = digest.digest(password.toByteArray())

        // Convert the byte array to a hexadecimal string representation
        val hexString = StringBuilder()
        for (byte in hashBytes) {
            val hex = Integer.toHexString(0xff and byte.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}
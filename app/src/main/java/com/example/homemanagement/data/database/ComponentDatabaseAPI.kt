import com.mongodb.client.MongoCollection
import org.litote.kmongo.findOneById
import org.litote.kmongo.replaceOneById

class ComponentDatabaseAPI(private val collection: MongoCollection<Component>) {
    suspend fun changeComponentName(componentId: String, newName: String): Boolean {
        val component = collection.findOneById(componentId)
        component?.let {
            it.name = newName
            collection.replaceOneById(componentId, component)
            return true
        }
        return false
    }

    suspend fun addPhoto(componentId: String, photoUrl: String): Boolean {
        val component = collection.findOneById(componentId)
        component?.let {
            it.photo = photoUrl
            collection.replaceOneById(componentId, component)
            return true
        }
        return false
    }

    suspend fun changePhoto(componentId: String, newPhotoUrl: String): Boolean {
        val component = collection.findOneById(componentId)
        component?.let {
            it.photo = newPhotoUrl
            collection.replaceOneById(componentId, component)
            return true
        }
        return false
    }

    suspend fun deletePhoto(componentId: String): Boolean {
        val component = collection.findOneById(componentId)
        component?.let {
            it.photo = null
            collection.replaceOneById(componentId, component)
            return true
        }
        return false
    }
}

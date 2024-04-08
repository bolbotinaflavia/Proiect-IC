import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.reactivestreams.KMongo

class RoomsDatabaseAPI(private val client: CoroutineClient) {

    private val database = client.getDatabase("UserDatabase")
    private val collection = database.getCollection<Room>("RoomsDatabase")

    suspend fun addRoom(room: Room): Boolean {
        collection.insertOne(room)
        return true
    }

    suspend fun deleteRoom(roomId: String): Boolean {
        collection.deleteOneById(roomId)
        return true
    }

    suspend fun changeRoomName(roomId: String, newName: String): Boolean {
        var room = collection.findOneById(roomId)
        room?.let {
            room.name = newName
            collection.replaceOneById(roomId, room)
            return true
        }
        return false
    }

    suspend fun changeRoomDescription(roomId: String, newDescription: String): Boolean {
        val room = collection.findOneById(roomId)
        room?.let {
            room.description = newDescription
            collection.replaceOneById(roomId, room)
            return true
        }
        return false
    }

    suspend fun changeRoomPhoto(roomId: String, newPhoto: String): Boolean {
        val room = collection.findOneById(roomId)
        if (room == null || ((!newPhoto.endsWith(".png")) && (!newPhoto.endsWith(".jpeg")))) {
            return false
        }
        room?.let {
            room.photo = newPhoto
            collection.replaceOneById(roomId, room)
            return true
        }
        return false
    }

    suspend fun addComponentRoom(roomId: String, component: Component): Boolean {
        var room = collection.findOneById(roomId)
        room?.let {
            room.components?.add(component)
            collection.replaceOneById(roomId, room)
            return true
        }
        return false
    }

    suspend fun deleteComponentRoom(roomId: String, componentId: Int): Boolean {
        var room = collection.findOneById(roomId)
        room?.let {
            val componentIndex = room.components?.indexOfFirst { room.id == componentId }
            if (componentIndex != -1) {
                if (componentIndex != null) {
                    room.components?.removeAt(componentIndex)
                }
                collection.replaceOneById(roomId, room)
                return true
            }
        }
        return false // Room not found or component not found in the room
    }

    suspend fun addTaskToRoom(roomId: String, task: Task): Boolean {
        var room = collection.findOneById(roomId)
        room?.let {
            room.tasks?.add(task) // Add the new task to the list of tasks
            collection.replaceOneById(roomId, room)
            return true // Assuming successful addition of task
        }
        return false // Room not found
    }

    suspend fun deleteTaskFromRoom(roomId: String, taskId: Int): Boolean {
        var room = collection.findOneById(roomId)
        room?.let {
            val taskIndex = room.tasks?.indexOfFirst { room.id == taskId }
            if (taskIndex != -1) {
                if (taskIndex != null) {
                    room.tasks?.removeAt(taskIndex)
                } // Remove the task from the list
                collection.replaceOneById(roomId, room)
                return true // Assuming successful deletion of task
            }
        }
        return false // Room not found or task not found in the room
    }
}

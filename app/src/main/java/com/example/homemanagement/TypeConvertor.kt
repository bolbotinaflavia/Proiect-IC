package com.example.homemanagement
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.homemanagement.data.database.Task
import com.example.homemanagement.data.database.room.Camera
import com.example.homemanagement.data.database.component.Component
import com.example.homemanagement.data.database.Element
//class ElementListConverter {
//    @TypeConverter
//    fun fromString(value: String): List<Element> {
//        val listType = object : TypeToken<List<Element>>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromList(list: List<Element>): String {
//        return Gson().toJson(list)
//    }
//}
class Converters {
    @TypeConverter
    fun fromTasks(tasks: List<Task>?): String? {
        return Gson().toJson(tasks)
    }

    @TypeConverter
    fun toTasks(tasksString: String?): List<Task>? {
        val type = object : TypeToken<List<Task>>() {}.type
        return Gson().fromJson(tasksString, type)
    }
    @TypeConverter
    fun fromComponents(components: List<Component>?): String? {
        return Gson().toJson(components)
    }

    @TypeConverter
    fun toComponents(componentString: String?): List<Component>? {
        val type = object : TypeToken<List<Component>>() {}.type
        return Gson().fromJson(componentString, type)
    }

    @TypeConverter
    fun fromCamera(camera: Camera?): String? {
        return Gson().toJson(camera)
    }

    @TypeConverter
    fun toCamera(cameraString: String?): Camera? {
        val type = object : TypeToken<Camera>() {}.type
        return Gson().fromJson(cameraString, type)
    }


    @TypeConverter
    fun fromElementsList(elements: List<Element>?): String? {
        return Gson().toJson(elements)
    }


    @TypeConverter
    fun toElements(elementString: String?): List<Element>? {
        val type = object : TypeToken<List<Element>>() {}.type
        return Gson().fromJson(elementString, type)
    }
}

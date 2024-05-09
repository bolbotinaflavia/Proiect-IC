
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun taskDao(): TaskDao
    abstract fun componentDao(): ComponentDao
}
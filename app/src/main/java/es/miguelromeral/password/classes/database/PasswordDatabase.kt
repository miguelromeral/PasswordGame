package es.miguelromeral.password.classes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.miguelromeral.password.classes.Password

@Database(entities = [Password::class], version = 6, exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {

    abstract val passwordDatabaseDao: PasswordDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: PasswordDatabase? = null

        fun getInstance(context: Context): PasswordDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PasswordDatabase::class.java,
                        "db_password")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}
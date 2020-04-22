package es.miguelromeral.password.classes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PasswordDatabaseDao {

    @Insert
    fun insert(password: Password)

    @Update
    fun update(password: Password)

    @Query("DELETE FROM password_table")
    fun clear()

    @Query("DELETE FROM password_table WHERE word = :key")
    fun delete(key: String)

}
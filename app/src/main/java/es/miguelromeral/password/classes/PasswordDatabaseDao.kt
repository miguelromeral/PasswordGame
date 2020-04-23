package es.miguelromeral.password.classes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PasswordDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(password: Password)

    @Update
    fun update(password: Password)

    @Query("SELECT * FROM password_table")
    fun getAllPasswords(): LiveData<List<Password>>

    @Query("SELECT * FROM password_table WHERE level = :level")
    fun getPasswordsByLevel(level: String): LiveData<List<Password>>

    @Query("DELETE FROM password_table")
    fun clear()

    @Query("DELETE FROM password_table WHERE word = :key")
    fun delete(key: String)

}
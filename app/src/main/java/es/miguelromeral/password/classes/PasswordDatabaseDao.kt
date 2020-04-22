package es.miguelromeral.password.classes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PasswordDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(password: CustomPassword)

    @Update
    fun update(password: CustomPassword)

    @Query("SELECT * FROM password_table")
    fun getAllPasswords(): LiveData<List<CustomPassword>>

    @Query("DELETE FROM password_table")
    fun clear()

    @Query("DELETE FROM password_table WHERE word = :key")
    fun delete(key: String)

}
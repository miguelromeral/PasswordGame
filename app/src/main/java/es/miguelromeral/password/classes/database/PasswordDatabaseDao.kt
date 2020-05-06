package es.miguelromeral.password.classes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import es.miguelromeral.password.classes.Password

@Dao
interface PasswordDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(password: Password)

    @Update
    fun update(password: Password)

    @Query("SELECT * FROM password_table")
    fun getAllPasswords(): LiveData<List<Password>>


    @Query("SELECT * FROM password_table")
    fun getAllPasswordsSync(): List<Password>

    @Query("DELETE FROM password_table")
    fun clear()

    @Query("DELETE FROM password_table WHERE word = :key")
    fun delete(key: String)


    @RawQuery
    fun insertDataRawFormat(query: SupportSQLiteQuery): Boolean
}
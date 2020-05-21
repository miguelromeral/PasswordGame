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

    @Query("SELECT * FROM password_table WHERE custom = :custom")
    fun getAllPasswords(custom: Boolean): LiveData<List<Password>>


    @Query("SELECT * FROM password_table WHERE custom = :custom")
    fun getAllPasswordsSync(custom: Boolean): List<Password>

    @Query("DELETE FROM password_table WHERE word = :key")
    fun delete(key: String)

    @Query("DELETE FROM password_table WHERE custom = :custom")
    fun clear(custom: Boolean = false)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //@Insert
    @JvmSuppressWildcards
    fun insertCache(objects: List<Password>)

    @RawQuery
    fun insertDataRawFormat(query: SupportSQLiteQuery): Boolean
}
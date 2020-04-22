package es.miguelromeral.password.classes

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "password_table")
data class CustomPassword(
    @ColumnInfo(name = "category")
    var category: String? = "",

    @ColumnInfo(name = "hints")
    var hints: String? = "",

    @ColumnInfo(name = "language")
    var language: String? = "",

    @ColumnInfo(name = "level")
    var level: String? = "",

    @PrimaryKey
    var word: String = "",

    @ColumnInfo(name = "random")
    var random: Long? = 0
)
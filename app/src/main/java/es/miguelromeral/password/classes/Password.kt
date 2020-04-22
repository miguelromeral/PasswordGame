package es.miguelromeral.password.classes

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

data class Password(
    var category: String? = "",
    var hints: String? = "",
    var language: String? = "",
    var level: String? = "",
    var word: String = "",
    var random: Long? = 0,
    var time: Long = 0
) : Parcelable {

    var solved: Boolean = false
    var failed: Boolean = false


    fun hintsSplit(): List<String> {
        return if(hints.isNullOrEmpty())
            listOf()
        else{
            var h = hints!!
            h.split(SEPARATOR).sorted()
        }
    }

    fun saidHint(word: String): Boolean = hintsSplit().contains(word)

    private constructor(parcel: Parcel) : this(
        category = parcel.readString(),
        hints = parcel.readString(),
        language = parcel.readString(),
        level = parcel.readString(),
        word = parcel.readString() ?: "",
        random = parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(hints)
        parcel.writeString(language)
        parcel.writeString(level)
        parcel.writeString(word)
        parcel.writeLong(random ?: 0)
    }

    override fun describeContents() = 0

    companion object {
        val SEPARATOR = ","

        @JvmField
        val CREATOR = object : Parcelable.Creator<Password> {
            override fun createFromParcel(parcel: Parcel) = Password(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Password>(size)
        }
    }
}

    //constructor(){}

    /*
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "category" to category,
            "hints" to hints,
            "level" to level,
            "word" to word
        )
    }*/

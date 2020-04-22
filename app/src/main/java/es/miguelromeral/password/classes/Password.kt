package es.miguelromeral.password.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

data class Password(
    val category: String? = "",
    val hints: String? = "",
    val language: String? = "",
    val level: String? = "",
    val word: String? = "",
    val random: Long? = 0
) : Parcelable {

    var solved: Boolean = false
    var failed: Boolean = false

    val hintsSplit: List<String>
        get() =
            if(hints.isNullOrEmpty())
                listOf()
            else
                hints.split(SEPARATOR).sorted()


    private constructor(parcel: Parcel) : this(
        category = parcel.readString(),
        hints = parcel.readString(),
        language = parcel.readString(),
        level = parcel.readString(),
        word = parcel.readString(),
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

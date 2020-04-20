package es.miguelromeral.password.classes

import com.google.firebase.firestore.Exclude


data class Password(
    val category: String? = "",
    val hints: String? = "",
    val level: String? = "",
    val word: String? = "",
    val random: Long? = 0
){
    var solved: Boolean = false
    var failed: Boolean = false

    val hintsSplit: List<String>
        get() =
            if(hints.isNullOrEmpty())
                listOf()
            else
                hints.split(SEPARATOR).sorted()


    companion object {
        val SEPARATOR = ","
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

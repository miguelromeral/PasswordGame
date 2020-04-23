package es.miguelromeral.password.classes

interface IRepository {
    fun retrievedWords(list: List<Password>)
}
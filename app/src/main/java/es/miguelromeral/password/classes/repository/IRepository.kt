package es.miguelromeral.password.classes.repository

import es.miguelromeral.password.classes.Password

interface IRepository {
    fun retrievedWords(list: List<Password>)
}
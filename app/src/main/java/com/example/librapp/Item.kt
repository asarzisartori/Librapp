package com.example.librapp

import java.sql.Timestamp

data class Item(val titolo: String, val autore: String, val genere: String, val tipologia: String, val descrizione: String ,val prenotato: String, val user: String, val timestamp: String) {
    constructor() : this("","","","","","","", "")

    override fun toString(): String {
        return "Item(titolo='$titolo', autore='$autore', genere='$genere', tipologia='$tipologia', descrizione='$descrizione', prenotato='$prenotato', user='$user', timestamp='$timestamp')"
    }

}
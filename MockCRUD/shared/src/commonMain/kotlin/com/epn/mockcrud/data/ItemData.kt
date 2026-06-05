package com.epn.mockcrud.data

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val description: String,
    val imageUrl: String? = null
)

val sampleBooks = mutableListOf(
    Book(1L, "El Quijote", "Miguel de Cervantes", "Una de las obras cumbre de la literatura española.", "https://m.media-amazon.com/images/I/71YfSAtFslL._AC_UF1000,1000_QL80_.jpg"),
    Book(2L, "Cien años de soledad", "Gabriel García Márquez", "La obra maestra del realismo mágico.", "https://m.media-amazon.com/images/I/8179uBA8zBL._AC_UF1000,1000_QL80_.jpg"),
    Book(3L, "Rayuela", "Julio Cortázar", "Una novela que se puede leer de varias maneras.", "https://m.media-amazon.com/images/I/81mIOn7vXBL._AC_UF1000,1000_QL80_.jpg"),
    Book(4L, "1984", "George Orwell", "Una distopía sobre el control totalitario.", "https://m.media-amazon.com/images/I/71kxa1-0CkL._AC_UF1000,1000_QL80_.jpg"),
)

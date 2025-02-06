package com.example.listacompraroom.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class Product(
    @PrimaryKey val product: String,
    val quantity: Int,
    val price: Float
)

fun List<Product>.totalSum(): Float {
    var total = 0f
    for (product in this) {
        total += product.price * product.quantity
    }
    return total
}



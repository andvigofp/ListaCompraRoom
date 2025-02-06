package com.example.listacompraroom.ui.navegation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object AddProductScreen : Screen("add_product")
    object ModifyProductScreen : Screen("modify_product/{product}") {
        fun createRoute(product: String) = "modify_product/$product"
    }
    object DetailsScreen : Screen("details/{product}") {  // Nueva pantalla de detalles
        fun createRoute(product: String) = "details/$product"
    }
}





package com.example.listacompraroom.ui.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listacompraroom.entidades.Product
import com.example.listacompraroom.ui.screens.AddProductScreen
import com.example.listacompraroom.ui.screens.DetailsDialog
import com.example.listacompraroom.ui.screens.HomeScreen
import com.example.listacompraroom.ui.screens.ModifyProductScreen
import com.example.listacompraroom.ui.state.ShoppingViewModel

@Composable
fun ShoppingApp(viewModel: ShoppingViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                viewModel,
                onModifyProductClick = { product ->
                    navController.navigate(Screen.ModifyProductScreen.createRoute(product.product))
                },
                onAddProductClick = {
                    navController.navigate(Screen.AddProductScreen.route)
                }
            )
        }
        composable(Screen.AddProductScreen.route) {
            AddProductScreen(viewModel, onAddProduct = { product ->
                viewModel.addProduct(product)
                navController.popBackStack()
            }, onCancel = {
                navController.popBackStack()
            })
        }
        composable(Screen.ModifyProductScreen.route) { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("product")
            val product = viewModel.products.value?.find { it.product == productName }
            product?.let {
                ModifyProductScreen(
                    it,
                    onProductModified = { updatedProduct ->
                        viewModel.updateProduct(updatedProduct)
                    },
                    onCancel = {
                        navController.popBackStack()
                    },
                    onNavigateHome = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

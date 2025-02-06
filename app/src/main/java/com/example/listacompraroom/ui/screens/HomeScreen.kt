package com.example.listacompraroom.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.listacompraroom.entidades.Product
import com.example.listacompraroom.entidades.totalSum
import com.example.listacompraroom.ui.state.ShoppingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ShoppingViewModel,
    onDetailsClick: (Product) -> Unit,
    onModifyProductClick: (Product) -> Unit,
    onAddProductClick: () -> Unit
) {
    val products by viewModel.products.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var productToDelete by remember { mutableStateOf<Product?>(null) } // Estado para el diálogo

    val filteredProducts = products.filter { product ->
        product.product.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        bottomBar = { BottomBar(total = products.totalSum()) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Barra superior con título y botón "+"
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Lista de la Compra", modifier = Modifier.weight(1f))
                        Button(
                            onClick = { onAddProductClick() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Text("+", color = Color.White)
                        }
                    }
                }
            )

            // Buscador de productos
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Buscar producto") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                        }
                    }
                }
            )

            // Lista de productos con scroll
            LazyColumn {
                items(filteredProducts) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(product.product, modifier = Modifier.weight(1f))
                        Text("${product.price}€", modifier = Modifier.weight(1f))
                        IconButton(onClick = { onDetailsClick(product) }) {
                            Icon(Icons.Default.Visibility, contentDescription = "Detalles")
                        }
                        IconButton(onClick = { productToDelete = product }) { // Muestra el diálogo
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                        IconButton(onClick = { onModifyProductClick(product) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modificar Producto")
                        }
                    }
                }
            }
        }

        // Si productToDelete no es nulo, muestra el diálogo de confirmación
        productToDelete?.let { product ->
            AlertDialog(
                onDismissRequest = { productToDelete = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que quieres borrar el producto '${product.product}'?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteProduct(product)
                            productToDelete = null
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { productToDelete = null }
                    ) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun BottomBar(total: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Total:",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Text(
            "$total€",
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}
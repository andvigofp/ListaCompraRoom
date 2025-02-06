package com.example.listacompraroom.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.listacompraroom.entidades.Product
import com.example.listacompraroom.ui.state.ShoppingViewModel

@Composable
fun AddProductScreen(
    viewModel: ShoppingViewModel,
    onAddProduct: (Product) -> Unit,
    onCancel: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var price by remember { mutableStateOf("0.0") }

    var showProductExistsDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) } // Estado para ventana emergente de éxito
    var productAdded by remember { mutableStateOf<Product?>(null) } // Guarda el producto recién añadido

    val products by viewModel.products.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Añadir Producto",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nombre del Producto") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = quantity,
            onValueChange = { quantity = if (it.isEmpty()) "0" else it },
            label = { Text("Cantidad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = price,
            onValueChange = { price = if (it.isEmpty()) "0.0" else it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                val trimmedName = productName.trim()
                if (trimmedName.isNotBlank() && quantity.toIntOrNull() != null && price.toFloatOrNull() != null) {
                    val exists = products.any { it.product.trim().equals(trimmedName, ignoreCase = true) }
                    if (exists) {
                        showProductExistsDialog = true
                    } else {
                        val newProduct = Product(trimmedName, quantity.toInt(), price.toFloat())
                        productAdded = newProduct
                        showSuccessDialog = true // Solo muestra la ventana emergente
                    }
                }
            }) {
                Text("Añadir Producto")
            }
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }

    // Ventana emergente si el producto ya existe
    if (showProductExistsDialog) {
        AlertDialog(
            onDismissRequest = { showProductExistsDialog = false },
            title = { Text("Producto ya existe") },
            text = { Text("Ya existe un producto con ese nombre. Por favor, introduce otro nombre.") },
            confirmButton = {
                TextButton(onClick = { showProductExistsDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Ventana emergente si el producto se ha añadido correctamente
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Producto añadido") },
            text = { Text("El producto se ha añadido correctamente.") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    productAdded?.let { onAddProduct(it) } // Agrega el producto al aceptar
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

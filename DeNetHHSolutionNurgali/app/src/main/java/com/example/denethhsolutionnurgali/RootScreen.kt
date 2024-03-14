package com.example.denethhsolutionnurgali

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.UUID
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun App() {
    NavHost(navController = rememberNavController(), startDestination = "root") {
        composable("root") { RootScreen() }
    }
}

@Composable
fun RootScreen(viewModel: RootViewModel = hiltViewModel<RootViewModel>()) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "name of screen = ${uiState.nameOfScreen}")
        Text(text = "is left child exists? = ${uiState.isLeftChildExist}")
        Text(text = "is right child exists? = ${uiState.isRightChildExist}")
        Text(text = "is parent exists? = ${uiState.isParentExist}")
        Text(text = "count of screens = ${uiState.countExistingScreen}")
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { viewModel.onBackButtonClick() }, Modifier.fillMaxWidth()) {
            TextButtonBack(isExist = uiState.isParentExist)
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Button(onClick = { viewModel.onLeftButtonClick() }, modifier = Modifier.weight(1f)) {
                TextButton(isExist = uiState.isLeftChildExist, direction = "left")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.onRightButtonClick() }, modifier = Modifier.weight(1f)) {
                TextButton(isExist = uiState.isRightChildExist, direction = "right")
            }
        }
    }
}

@Composable
fun TextButton(isExist: Boolean, direction: String) {
    val name = direction
    if (isExist) {
        Text(text = "go to $name")
    } else {
        Text(text = "create $name")
    }
}

@Composable
fun TextButtonBack(isExist: Boolean) {
    if (isExist) {
        Text(text = "go to Back")
    } else {
        Text(text = "You are in the root")
    }
}
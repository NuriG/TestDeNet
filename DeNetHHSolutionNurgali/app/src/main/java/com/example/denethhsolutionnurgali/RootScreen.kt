package com.example.denethhsolutionnurgali

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun App() {
    val navController = rememberNavController()
    val rootScreen by remember {
        mutableStateOf(Node(left = null, right = null, name = "First", parent = null))
    }
    val hashMapForRoot = rememberSaveable {
        hashMapOf<String, Node>()
    }
    hashMapForRoot["first"] = rootScreen
    NavHost(navController = navController, startDestination = "root/first") {
        composable("root/{hash}") { backStackEntry ->
            val screenName = backStackEntry.arguments?.getString("hash")
            val newRoot = hashMapForRoot[screenName]
            if (newRoot != null) {
                RootScreen(navController, newRoot, hashMapForRoot)
            } else {
                RootScreen(navController, rootScreen, hashMapForRoot)
            }
        }
    }
}

@Composable
fun RootScreen(navController: NavController, root: Node, hashMapForRoot: HashMap<String, Node>) {
    Column() {
        ShowInfoScreen(root, hashMapForRoot)
        ShowNavigationButton(navController, root, hashMapForRoot)
    }
}

@Composable
fun LeftAndRightButtonForNavigateToChildOrCreate(
    navController: NavController,
    root: Node,
    hashMapForRoot: HashMap<String, Node>,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            if (root.left == null) {
                val hash = UUID.randomUUID().toString().takeLast(20)
                root.left = Node(left = null, right = null, name = hash, parent = root)
                hashMapForRoot[hash] = root.left!!
                navController.navigate("root/${hash}")
            } else {
                navController.navigate("root/${root.left!!.name}")
            }
        }) {
            Text(text = if (root.left != null) "go To Left Child" else "create Left Child Screen")
        }
        Button(onClick = {
            if (root.right == null) {
                val hash = UUID.randomUUID().toString().takeLast(20)
                root.right = Node(left = null, right = null, name = hash, parent = root)
                hashMapForRoot[hash] = root.right!!
                navController.navigate("root/${hash}")
            } else {
                navController.navigate("root/${root.right!!.name}")
            }
        }) {
            Text(text = if (root.right != null) "go To Right Child" else "create Right Child Screen")
        }
    }
}

@Composable
fun ShowNavigationButton(
    navController: NavController,
    root: Node,
    hashMapForRoot: HashMap<String, Node>,
) {
    Column() {
        if (root.parent != null) {
            val path = root.parent!!.name
            Button(
                onClick = { navController.navigate("root/${path}") },
            ) {
                Text(text = "go Back Screen")
            }
        }
        LeftAndRightButtonForNavigateToChildOrCreate(navController, root, hashMapForRoot)
    }
}

@Composable
fun ShowInfoScreen(root: Node, hashMapForRoot: HashMap<String, Node>) {
    Column {
        Text(text = "Name of screen: ${root.name}")
        Text(text = "Count of existing screen: ${hashMapForRoot.size}")
    }
}
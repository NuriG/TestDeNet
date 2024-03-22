package com.example.denethhsolutionnurgali

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID
import javax.inject.Inject

data class RootScreenUiState(
    val isLeftChildExist: Boolean = false,
    val isRightChildExist: Boolean = false,
    val isParentExist: Boolean = false,
    val nameOfScreen: String = "",
    val countExistingScreen: Int = 1,
)

private const val FILENAME = "node_data.dat"
private const val FILENAMECOUNTSCREEN = "count_existing_screen.dat"

@HiltViewModel
class RootViewModel @Inject constructor(
    @ApplicationContext private val application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RootScreenUiState())
    val uiState: StateFlow<RootScreenUiState> = _uiState.asStateFlow()

    private val _currentNode =
        MutableStateFlow(Node(left = null, right = null, name = "First", parent = null))
    private val currentNode: StateFlow<Node> = _currentNode.asStateFlow()

    fun onLeftButtonClick() {
        if (currentNode.value.left == null) {
            val newNode = Node(left = null, right = null, name = hashName(), parent = currentNode.value)
            _uiState.value = uiState.value.copy(countExistingScreen = uiState.value.countExistingScreen + 1)
            _currentNode.value.left = newNode
            _currentNode.value = newNode
        } else {
            val newNode = currentNode.value.left
            if (newNode != null) {
                _currentNode.value = newNode
            }
        }
        updateUiState(currentNode.value)
    }

    fun onRightButtonClick() {
        if (currentNode.value.right == null) {
            val newNode = Node(left = null, right = null, name = hashName(), parent = currentNode.value)
            _uiState.value = uiState.value.copy(countExistingScreen = uiState.value.countExistingScreen + 1)
            _currentNode.value.right = newNode
            _currentNode.value = newNode
        } else {
            val newNode = currentNode.value.right
            if (newNode != null) {
                _currentNode.value = newNode
            }
        }
        updateUiState(currentNode.value)
    }

    fun onBackButtonClick() {
        val parentNode = currentNode.value.parent
        if (parentNode != null) {
            _currentNode.value = parentNode
            updateUiState(currentNode.value)
        }
    }

    private fun updateUiState(currentNode: Node) {
        _uiState.value = uiState.value.copy(
            isLeftChildExist = currentNode.left != null,
            isRightChildExist  = currentNode.right != null,
            isParentExist = currentNode.parent != null,
            nameOfScreen =  currentNode.name,
        )
    }

    private fun hashName(): String = UUID.randomUUID().toString().takeLast(20)

    init {
        loadNodeData()?.let { _currentNode.value = it }
        loadCountExistingScreen()?.let {
            _uiState.value = uiState.value.copy(countExistingScreen = it)
        }
        updateUiState(currentNode = currentNode.value)
    }

    override fun onCleared() {
        super.onCleared()
        saveNodeData(currentNode.value)
        saveCountExistingScreen(_uiState.value.countExistingScreen)
    }

    private fun saveNodeData(node: Node) {
        try {
            val fileOutputStream = application.openFileOutput(FILENAME, Context.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(node)
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            Log.d("LOG_INSIDE_VIEW_MODEL", "Exception saveNodeData: $e")
            e.printStackTrace()
        }
    }

    private fun saveCountExistingScreen(countExistingScreen: Int) {
        try {
            val fileOutputStream = application.openFileOutput(FILENAMECOUNTSCREEN, Context.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(countExistingScreen)
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            Log.d("LOG_INSIDE_VIEW_MODEL", "Exception saveNodeData: $e")
            e.printStackTrace()
        }
    }

    private fun loadCountExistingScreen(): Int? {
        return try {
            ObjectInputStream(application.openFileInput(FILENAMECOUNTSCREEN)).use { inp ->
                inp.readObject() as? Int
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    private fun loadNodeData(): Node? {
        return try {
            ObjectInputStream(application.openFileInput(FILENAME)).use { inp ->
                inp.readObject() as? Node
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}
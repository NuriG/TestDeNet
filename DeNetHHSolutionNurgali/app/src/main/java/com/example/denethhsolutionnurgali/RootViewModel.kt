package com.example.denethhsolutionnurgali

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

data class RootScreenUiState(
    val isLeftChildExist: Boolean = false,
    val isRightChildExist: Boolean = false,
    val isParentExist: Boolean = false,
    val nameOfScreen: String = "",
    val countExistingScreen: Int = 1,
)

@HiltViewModel
class RootViewModel @Inject constructor() : ViewModel() {

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
        updateUiState(currentNode = currentNode.value)
    }

}
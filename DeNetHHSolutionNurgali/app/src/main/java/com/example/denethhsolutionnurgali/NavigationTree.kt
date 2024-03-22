package com.example.denethhsolutionnurgali

import java.io.Serializable

class Node(
    var left: Node? = null,
    var right: Node? = null,
    var name: String,
    var parent: Node? = null
) : Serializable
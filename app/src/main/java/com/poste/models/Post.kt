package com.poste.models

data class Post(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var url: String = "",
    var tags: List<String> = listOf(),
    var folder: Folder = Folder(),
    var date: String = ""
)

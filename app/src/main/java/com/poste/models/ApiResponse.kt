package com.poste.models

data class ApiResponse(
    val folders: List<Folder>,
    val posts: List<Post>,
    val shared_folders: List<Folder>
)

data class Folder(
    val id: String,
    val title: String,
    val tags: List<String>,
    val parent_id: String,
)

data class Post(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val tags: List<String>
)
package com.poste.models

data class ApiResponse(
    val folders: List<Folder>,
    val posts: List<Post>,
    val shared_folders: List<Folder>
)

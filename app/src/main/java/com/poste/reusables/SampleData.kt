package com.poste.reusables

import com.poste.models.Folder
import com.poste.models.Post
import java.time.LocalDate

/**
 * SampleData for Jetpack Compose Tutorial 
 */
object SampleData {
    // Sample Folder List data
    val defaultCreationDate = "2024-01-17T12:34:56.789012Z"
    val FolderListSample = listOf(
        Folder(
            title="Test Folder",
            description = "abc",
            file_count = 13,
            created_at = defaultCreationDate
        ),
        Folder(
            title="Test Folder 2 asjhgaskjghasg",
            file_count = 11,
            created_at = defaultCreationDate
        ),
        Folder(
            title="Paris 2024",
            file_count = 4,
            created_at = defaultCreationDate
        ),
    )

    //Sample Folder data
    val FolderSample = Folder(
        title="Test Folder",
        description = "Is this the test description?",
        file_count = 10,
        created_at = defaultCreationDate
    )

    //Sample Post data
    val PostSample = Post(
        title = "Test Post",
        url="https://www.facebook.com",
        folder= FolderSample,
        description = "A test post",
        date = LocalDate.now().toString(),
        tags = listOf(
            "funny",
            "memes"
        )
    )

    //Sample Post List data
    val PostListSample = listOf(
        Post(
            title="Test Post",
            url="https://www.facebook.com",
            folder= FolderSample,
            description="A test post",
            date=LocalDate.now().toString(),
            tags=listOf(
                "funny",
                "memes"
            )
        ),
        Post(
            title="Test Post 1",
            url="https://www.facebook.com",
            folder= FolderListSample[2],
            description = "A test post",
            date=LocalDate.now().toString(),
            tags = listOf(
                "funny",
                "memes"
            )
        ),
        Post(
            title="Test Post 2",
            url="https://www.facebook.com",
            folder= FolderListSample[1],
            description="A test post",
            date = LocalDate.now().toString(),
            tags = listOf(
                "funny",
                "memes"
            )
        ),
    )
}

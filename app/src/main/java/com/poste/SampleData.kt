package com.poste

import com.poste.models.Folder
import com.poste.models.Post
import java.time.LocalDate

/**
 * SampleData for Jetpack Compose Tutorial 
 */
object SampleData {
    // Sample Folder List data
    val FolderListSample = listOf(
        Folder(
            title="Test Folder",
            numFiles = 13,
            date = LocalDate.now().toString()
        ),
        Folder(
            title="Test Folder 2 asjhgaskjghasg",
            numFiles = 11,
            date = LocalDate.now().toString()
        ),
        Folder(
            title="Paris 2024",
            numFiles = 4,
            date = LocalDate.now().toString()
        ),
    )

    //Sample Folder data
    val FolderSample = Folder(
        title="Test Folder",
        numFiles = 10,
        date = LocalDate.now().toString()
    )

    //Sample Post data
    val PostSample = Post(
        title = "Test Post",
        url="https://www.facebook.com",
        folder=FolderSample,
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
            folder=FolderSample,
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
            folder=FolderListSample[2],
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
            folder=FolderListSample[1],
            description="A test post",
            date = LocalDate.now().toString(),
            tags = listOf(
                "funny",
                "memes"
            )
        ),
    )
}

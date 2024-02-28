package com.poste

import com.poste.reusables.Folder
import com.poste.reusables.Post
import java.time.LocalDate

/**
 * SampleData for Jetpack Compose Tutorial 
 */
object SampleData {
    // Sample Folder List data
    val FolderListSample = listOf(
        Folder(
          "Test Folder", 13, LocalDate.now()
        ),
        Folder(
            "Test Folder 2 asjhgaskjghasg", 11, LocalDate.now()
        ),
        Folder(
          "Paris 2024", 4, LocalDate.now()
        ),
    )

    //Sample Folder data
    val FolderSample = Folder("Test Folder", 10, LocalDate.now())

    //Sample Post data
    val PostSample = Post(
        "Test Post",
        "https://www.facebook.com",
        FolderSample,
        "A test post",
        LocalDate.now(),
        listOf(
            "funny",
            "memes"
        )
    )

    //Sample Post List data
    val PostListSample = listOf(
        Post(
            "Test Post",
            "https://www.facebook.com",
            FolderSample,
            "A test post",
            LocalDate.now(),
            listOf(
                "funny",
                "memes"
            )
        ),
        Post(
            "Test Post 1",
            "https://www.facebook.com",
            FolderListSample[2],
            "A test post",
            LocalDate.now(),
            listOf(
                "funny",
                "memes"
            )
        ),
        Post(
            "Test Post 2",
            "https://www.facebook.com",
            FolderListSample[1],
            "A test post",
            LocalDate.now(),
            listOf(
                "funny",
                "memes"
            )
        ),
    )
}

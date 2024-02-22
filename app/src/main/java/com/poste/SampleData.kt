package com.poste

import com.poste.reusables.Folder
import com.poste.reusables.Post
import java.time.LocalDate

/**
 * SampleData for Jetpack Compose Tutorial 
 */
object SampleData {
    // Sample conversation data
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

    val FolderSample = Folder("Test Folder", 10, LocalDate.now())

    val PostSample = Post(
        "Test Post",
        "www.facebook.com",
        FolderSample,
        "A test post",
        LocalDate.now(),
        listOf(
            "funny",
            "memes"
        )
    )
}

package com.poste

import java.time.LocalDate

/**
 * SampleData for Jetpack Compose Tutorial 
 */
object SampleData {
    // Sample conversation data
    val FolderSample = listOf(
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
}

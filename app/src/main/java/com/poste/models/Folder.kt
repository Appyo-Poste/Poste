package com.poste.models

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class Folder(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var creator: String = "",
    var tags: List<String> = listOf(),
    var parent_id: String = "",
    var is_root: Boolean = false,
    var created_at: String = "", // expects a date in the format "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'" e.g., "2024-03-22T01:00:01.948063Z"
    var shares: Map<String, String> = mapOf(),
    var file_count: Int = 0,
    var folder_count: Int = 0,
    var child_folders: List<Int> = listOf(),
) {

    /**
     * Django will return a date in the format "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
     * This function will convert it to "dd/MM/yy" to be displayed in the UI
     * If the date is invalid, it will return "00/00/00"
     * @return String representing the date in the format "dd/MM/yy"
     */
    fun getShortCreationDate(): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val parsedDate = parser.parse(this.created_at) ?: return "00/00/00"
        return formatter.format(parsedDate)
    }
}






package com.poste.models

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class Folder(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var tags: List<String> = listOf(),
    var parent_id: String = "",
    var file_count: Int = 0,
    var created_at: String = "",
    var shares: Map<String, String> = mapOf()
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






package com.example.planerfire.fb

data class ListItem (
    var id: String? = "",
    var title: String? = "",
    var point: String? = "",
    var date_create: String? = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "point" to point,
            "date_create" to date_create
        )
    }
}
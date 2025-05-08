package com.example.myapplication

/*
    This class came essentially entirely from this article https://canlioya.medium.com/dynamic-font-sizes-with-jetpack-compose-2665c65d78f7
    It is used to classify what each font size does to the overall size of fonts.
 */

enum class FontSizePrefs(
    val key: String,
    val fontSizeExtra: Int,
    val label: String
) {
    SMALL("S", 2, "Small"),
    DEFAULT("M", 4, "Medium"),
    LARGE("L", 6, "Large"),
    EXTRA_LARGE("XL", 8, "Extra Large");

companion object {
        fun getFontPrefFromKey(key: String?): FontSizePrefs {
            return FontSizePrefs.entries.find {
                it.key == key
            } ?: DEFAULT
        }
    }
}
package com.GetGrinnected.myapplication

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a navigation item for our navigation bar.
 *
 * @property label name associated with the item.
 * @property ImageVector the icon associated with the item.
 *
 */
data class NavItem(
    val label : String,
    val icon : ImageVector,
)

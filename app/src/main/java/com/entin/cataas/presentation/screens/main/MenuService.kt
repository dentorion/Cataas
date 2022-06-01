package com.entin.cataas.presentation.screens.main

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provide items for menus.
 * This items are server - side keys, that are needed for successful api-request.
 */

@Singleton
class MenuService @Inject constructor() {

    fun getFilterMenuItems() =
        listOf("Brak", "blur", "mono", "sepia", "negative", "paint", "pixel")

    fun getColorMenuItems() =
        listOf("black", "white", "gray", "red", "yellow", "green", "blue", "orange", "pink")
}
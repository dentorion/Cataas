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
        mapOf(
            "Czarny" to "black",
            "Biały" to "white",
            "Szary" to "gray",
            "Czerwony" to "red",
            "Żółty" to "yellow",
            "Zielony" to "green",
            "Niebieski" to "blue",
            "Pomarańczowy" to "orange",
            "Różowy" to "pink"
        )
}
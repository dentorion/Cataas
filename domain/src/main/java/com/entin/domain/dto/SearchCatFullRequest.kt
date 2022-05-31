package com.entin.domain.dto

data class SearchCatFullRequest(
    val tag: String,
    val filter: String,
    val text: String,
    val size: String,
    val colors: String,
)

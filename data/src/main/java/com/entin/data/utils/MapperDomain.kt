package com.entin.data.utils

import com.entin.data.model.CatJson
import com.entin.domain.model.CatDomain

/**
 * Mapper
 * CatJson converts into Domain Model to be shown for user.
 */

fun CatJson.mapToCatDomain(): CatDomain =
    CatDomain(
        urlImg = this.url
    )

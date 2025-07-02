package com.example.spoti5.domain.model

data class LanguageModelNew(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null,
    var isShowAnim: Boolean = false,
)
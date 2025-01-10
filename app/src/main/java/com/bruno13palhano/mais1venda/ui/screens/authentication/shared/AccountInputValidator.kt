package com.bruno13palhano.mais1venda.ui.screens.authentication.shared

fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    return email.matches(emailRegex.toRegex())
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 8
}

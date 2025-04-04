package com.bruno13palhano.mais1venda.ui.screens.authentication.shared

fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    return email.matches(emailRegex.toRegex())
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 8
}

fun isPhoneValid(phone: String): Boolean {
    return phone.length == 11
}

fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
    return password == confirmPassword
}

fun isCompanyNameValid(companyName: String): Boolean {
    return companyName.isNotBlank() && companyName.length >= 3
}

fun isAddressValid(address: String): Boolean {
    return address.isNotBlank() && address.length >= 3
}

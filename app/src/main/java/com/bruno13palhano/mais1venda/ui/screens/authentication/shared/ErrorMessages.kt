package com.bruno13palhano.mais1venda.ui.screens.authentication.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bruno13palhano.mais1venda.R

@Composable
fun getErrorMessages(): Map<CodeError, String> {
    return mapOf(
        CodeError.INVALID_EMAIL to stringResource(id = R.string.invalid_email),
        CodeError.INVALID_PASSWORD to stringResource(id = R.string.invalid_password),
        CodeError.PASSWORD_MISMATCH to stringResource(id = R.string.password_mismatch),
        CodeError.INVALID_COMPANY_NAME to stringResource(id = R.string.invalid_company_name),
        CodeError.INVALID_PHONE to stringResource(id = R.string.invalid_phone),
        CodeError.INVALID_ADDRESS to stringResource(id = R.string.invalid_address),
        CodeError.INVALID_CREDENTIALS to stringResource(id = R.string.invalid_credentials),
        CodeError.EMPTY_FIELDS to stringResource(id = R.string.empty_fields),
        CodeError.ERROR_CREATING_ACCOUNT to stringResource(id = R.string.error_creating_account),
    )
}

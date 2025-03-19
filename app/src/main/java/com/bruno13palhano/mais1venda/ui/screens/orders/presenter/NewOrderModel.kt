package com.bruno13palhano.mais1venda.ui.screens.orders.presenter

import androidx.compose.runtime.Immutable
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.mais1venda.ui.screens.authentication.shared.CodeError

@Immutable
internal data class NewOrderState(
    val isLoading: Boolean = false,
    val order: Order? = null,
)

@Immutable
internal sealed interface NewOrderEvent {
    data class LoadOrder(val id: Long) : NewOrderEvent
    data object ConfirmOrder : NewOrderEvent
    data object CancelOrder : NewOrderEvent
    data object ConfirmCancelOrder : NewOrderEvent
    data object DismissKeyboard : NewOrderEvent
    data object NavigateBack : NewOrderEvent
}

@Immutable
internal sealed interface NewOrderSideEffect {
    data class ShowError(val codeError: CodeError) : NewOrderSideEffect
    data object DismissKeyboard : NewOrderSideEffect
    data class ShowDialog(val messageType: DialogMessageType) : NewOrderSideEffect
    data object NavigateBack : NewOrderSideEffect
}

@Immutable
internal enum class DialogMessageType {
    OK,
    CANCEL,
}

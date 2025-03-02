package com.bruno13palhano.mais1venda.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.data.model.shared.OrderStatus
import com.bruno13palhano.mais1venda.R
import com.bruno13palhano.mais1venda.ui.screens.shared.dateFormat

@Composable
internal fun OrderListItem(
    customerName: String,
    productName: String,
    orderDate: Long,
    price: Float,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp),
        overlineContent = {
            Text(
                text = customerName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        headlineContent = {
            Text(
                text = productName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = dateFormat.format(orderDate),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = {
            Text(
                text = stringResource(id = R.string.price_tag, price),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
    )
}

val testProduct = Product(
    id = 1,
    name = "Product 1",
    image = byteArrayOf(),
    category = emptyList(),
    description = "Description 1",
    code = "1",
    quantity = 1,
    exhibitToCatalog = false,
    lastModifiedTimestamp = "",
)

val testCustomer = Customer(
    uid = "1",
    name = "Customer 1",
    email = "email 1",
    phone = "phone 1",
    address = Address("", "", "", ""),
    socialMedia = emptyList(),
    orders = emptyList(),
    lastModifiedTimestamp = "",
)

val testOrder = Order(
    id = 1,
    productName = "Product 1",
    productCode = "1234567",
    quantity = 1,
    unitPrice = 100.0,
    off = 1.2f,
    totalPrice = 100.0,
    customer = testCustomer,
    orderDate = 111111111111212,
    deliveryDate = 12234523453245,
    status = OrderStatus.PROCESSING_ORDER,
    lastModifiedTimestamp = "",
)

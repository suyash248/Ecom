package models

import services.{DefaultDiscountPolicy, DiscountPolicy}

case class Cart(items: Seq[Item] = List(), discountPolicy: DiscountPolicy = DefaultDiscountPolicy) {

  // addToCart -> Adds an item to cart
  def ++(item: Item): Cart = this.copy(items :+ item)

  // Removes an item from the cart
  def --(item: Item): Cart = Cart(items.filter(!_.equals(item)))

  // Checkout -> Creates/generates an Order and prints the invoice.
  def checkout(): Option[Order] = {
    if (items.isEmpty) {
      println("Cart is empty, can't checkout!")
      None
    } else {
      val order: Order = Order(items, discountPolicy)
      val invoice: String = order.generateInvoice()
      println(invoice)
      Some(order)
    }
  }

  // Completes the order, and resets the cart once order is successfully placed.
  def makePayment(order: Order): Cart = {
    order.pay() // This will redirect to selected payment gateway or will deduct the balance from balance sheet/ledger.
    copy(items = Seq())
  }

}

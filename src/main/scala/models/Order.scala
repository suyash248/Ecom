package models

import services.{Commons, DefaultDiscountPolicy, DiscountPolicy}
import services.Commons.{roundOff, scale}

case class Order(items: Seq[Item], discountPolicy: DiscountPolicy = DefaultDiscountPolicy) {
  val orderId: String = Commons.uuid4()
  var payable: Double = 0.0

  /**
    * Calculates the discount available on this order.
    * @return
    */
  def getDiscount(): Double = {
    val discountableItems: Seq[Item] = items.filter(_.product.productCategory.isDiscountable())
    val totalDiscountablePrice: Double = discountableItems.map(_.product.price).sum
    val discountRatio: Double = discountPolicy.discountRatio(totalDiscountablePrice)
    totalDiscountablePrice * discountRatio
  }

  /**
    * Net amount to be paid by the customer.
    * @return
    */
  def amountPayable(): Double = {
    val discount: Double = getDiscount()
    val totalPrice: Double = items.map(_.product.price).sum
    val taxes: Map[String, Double] = items.flatMap(_.taxes()).groupBy(_._1).mapValues(v => v.map(_._2).sum)
    val totalTaxAmount: Double = taxes.values.sum

    val amountPayable: Double = totalPrice + totalTaxAmount - discount
    amountPayable
  }

  /**
    * Calculated price, apply taxes, apply discounts(if available) and generates the invoice for this order.
    * @return
    */
  def generateInvoice(): String = {
    val discount: Double = getDiscount()
    val totalPrice: Double = items.map(_.product.price).sum
    val itemsTaxes: Map[Item, Map[String, Double]] = items.map(item => item->item.taxes()).toMap

    val itemsTotalTaxes: Map[Item, Double] = itemsTaxes.map{case (item, itemTaxes) => (item, itemTaxes.values.sum)}
//    println(itemsTotalTaxes)


    val productsInfo: String = itemsTotalTaxes.map{case (item, totalTax) =>
      (s"${item.product.name}(Qty. ${item.quantity})", s"INR ${scale(item.product.price + totalTax)}/-")}
      .mkString("\n\t\t")


    val taxes: Map[String, Double] = items.flatMap(_.taxes()).groupBy(_._1).mapValues(v => v.map(_._2).sum)
    val totalTaxAmount: Double = taxes.values.sum

    val amountPayable: Double = totalPrice + totalTaxAmount - discount

    val invoice: String = s"""
     Products (Including tax)-
     \t${productsInfo}\n
    (A) Total (Excluding Tax): INR ${scale(totalPrice)}/-
    (B) Tax (B1 + B2 + B3): INR ${scale(totalTaxAmount)}/-
    \t (B1) VAT: INR ${scale(taxes("VAT"))}/-
    \t (B2) Sales tax: INR ${scale(taxes("SALES_TAX"))}/-
    \t (B3) Import Duty: INR ${scale(taxes("IMPORT_DUTY"))}/-
    (C) Discount: INR ${scale(discount)}/-
    (D) Payable (A + B - C): INR ${scale(amountPayable)}/-
        Net amount(Round off (D)) = INR ${roundOff(amountPayable)} /-
    """
    invoice
  }

  /** This method will start the transaction in BEGIN state and will redirect to payment gateway if required,
      and generates bill once transaction is completed successfully.
    */
  def pay(): Transaction = ???

  override def equals(obj: Any): Boolean = {
    obj match {
      case order: Order => orderId.equals(order.orderId)
      case _ => false
    }
  }

  override def hashCode(): Int = orderId.hashCode()
}

case class Item(product: Product, seller: Seller, quantity: Int = 1) {
  val itemId: String = Commons.uuid4()

  val applyTax: Double => Product => Double = {
    taxRatio: Double => (product: Product) => product.price * taxRatio
  }

  /**
    * Calculates the taxes' ratio on this item
    * @return
    */
  def taxes(): Map[String, Double] = {
    val taxPolicy = seller.location.taxPolicy()
    var taxes = Map(
      "VAT" -> applyTax(taxPolicy.VAT_RATIO)(product),
      "SALES_TAX" -> applyTax(taxPolicy.SALES_TAX_RATIO)(product),
      "IMPORT_DUTY" -> 0.0
    )
    if (product.isImported) {
      taxes += "IMPORT_DUTY" -> applyTax(taxPolicy.IMPORT_DUTY_RATIO)(product)
    }
    taxes
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case item: Item => itemId.equals(item.itemId)
      case _ => false
    }
  }

  override def hashCode(): Int = itemId.hashCode()
}
package models

import services.Commons

case class Transaction(order: Order) {
  val txnId: String = Commons.uuid4()
  // Other attributes like payment gateway, txnStatus, amountPaid etc.
}

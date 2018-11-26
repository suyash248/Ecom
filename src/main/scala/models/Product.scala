package models

import services.Commons

case class Product(name: String, price: Double, productCategory: ProductCategory, isImported: Boolean = false) {
  val productId: String = Commons.uuid4()

  override def equals(obj: Any): Boolean = {
    obj match {
      case prod: Product => productId.equals(prod.productId)
      case _ => false
    }
  }

  override def hashCode(): Int = productId.hashCode()
}

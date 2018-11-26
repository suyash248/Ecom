package services

import models.Product
import scala.annotation.tailrec

case class Inventory(products: Map[Product, Int] = Map.empty[Product, Int].withDefaultValue(-1)) {

  /**
    * Add specified units(quantity) of the product to inventory.
    * @param prod
    * @param quantity
    * @return
    */
  def addProduct(prod: Product, quantity: Int = 1): Inventory = this.copy(products + (prod -> quantity))

  /**
    * Removes specified units(quantity) of the product from inventory.
    * @param prod
    * @param reqQty
    * @return
    */
  def removeProduct(prod: Product, reqQty: Int = 1): Inventory = {
    if (products.contains(prod)) {
      val availableQty: Int = products(prod)
      if (availableQty > reqQty) this.copy(products + (prod -> (availableQty - reqQty)))
      else this.copy(products - prod)
    }
    else this
  }

  @tailrec
  final def removeProducts(products: Map[Product, Int]): Inventory = {
    if (products.isEmpty) this
    else if(products.size == 1) {
      val prod = products.keys.head
      removeProduct(prod, products(prod))
    }
    else removeProducts(products - products.keys.head)
  }

  /**
    * Fetches and returns the available(In stock) products which matching the predicate from the inventory.
    * @param predicate
    * @param reqQty
    * @return
    */

  def filter(predicate: Product => Boolean, reqQty: Int = 1): Map[String, Seq[Product]] = {
    val stockInfo: Map[String, Seq[Product]] = Map()

    var prodsInStock: Seq[Product] = Seq()
    var prodsOutOfStock: Seq[Product] = Seq()

    products.filterKeys(prod => predicate(prod)).foreach{case(prod, qty)=>
        if (isProductInStock(prod, reqQty)) prodsInStock = prodsInStock :+ prod
        else prodsOutOfStock = prodsOutOfStock :+ prod
    }
    Map("IN_STOCK"->prodsInStock, "OUT_OF_STOCK"->prodsOutOfStock)
  }

  def isProductInStock(prod: Product, reqQty: Int = 1): Boolean = products(prod) >= reqQty

  override def toString: String =
    s"\n-----------------------\nINVENTORY\n-----------------------\n${status().mkString("\n")}\n-----------------------\n"

  def status() = products.map{ case(prod, qty) => s"${prod.name} - ${qty}"}

}

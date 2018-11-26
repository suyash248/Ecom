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
  def filter(predicate: Product => Boolean, reqQty: Int = 1): Set[Product] = {
    products.filterKeys(prod => predicate(prod) && products(prod) >= reqQty).keySet
  }

//  def poll(predicate: Product => Boolean, reqQty: Int = 1): Inventory = {
//    // Extracts(Filter and deletes) the available(In stock) products which matching the predicate from the inventory.
//    val filteredProduct: Set[Product] = filter(predicate, reqQty)
//    @tailrec
//    def pollHelper(products: Set[Product]): Inventory = {
//      if (products.isEmpty) this
//      else if(products.size == 1) removeProduct(products.head, reqQty)
//      else pollHelper(products - products.head)
//    }
//    pollHelper(filteredProduct)
//  }
}

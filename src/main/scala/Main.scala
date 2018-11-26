import models._
import services.Inventory

object Main extends App {

  val loc1 = Location(29084.0, 29404.0, "NCR")
  val loc2 = Location(93424.0, 90834.0, "KARNATAKA")

  val seller1 = Seller("NCR Enterprises", loc1)
  val seller2 = Seller("Karnataka Enterprises", loc2)
  val seller3 = Seller("Delhi Enterprises", loc1)
  val seller4 = Seller("Noida Enterprises", loc1)

  val prod1 = Product("2 States", 104.0, Book.Fiction)
  val prod2 = Product("Inception", 202.0, Book.Fiction, isImported = true)
  val prod3 = Product("Mindfulness", 207.0, Book.SelfImprovement, isImported = true)
  val prod4 = Product("Towns", 151.0, Book.Management, isImported = true)
  val prod5 = Product("Rule the game", 59.0, Book.Management)
  val prod6 = Product("Built to last", 59.0, Book.Management, isImported = true)

  val item1 = Item(prod1, seller1, quantity = 5)
  val item2 = Item(prod2, seller1)
  val item3 = Item(prod3, seller3)
  val item4 = Item(prod4, seller4)
  val item5 = Item(prod5, seller4)
  val item6 = Item(prod6, seller4, quantity = 10)

  val cart = Cart(Seq(item1))

  val newCart = cart ++ item3

  // Without using inventory,
//  cart.checkout()

  // Using an inventory
  var inventory = Inventory(Map(
    prod1 -> 2,
    prod2 -> 5,
    prod3 -> 1,
    prod4 -> 2,
  ))
  println(inventory)

  inventory = inventory.addProduct(prod5, 7)
  println(inventory)

  val reqQty = 3
  val filteredProducts: Set[Product] = inventory.filter(prod=> prod.name.startsWith("Inception"), reqQty = reqQty)
  inventory = inventory.removeProducts(filteredProducts.zip(Seq.fill(filteredProducts.size)(reqQty)).toMap)

  println(inventory)

  val cart1 = Cart(filteredProducts.map(Item(_, seller1, reqQty)).toSeq)
  cart1.checkout()

}

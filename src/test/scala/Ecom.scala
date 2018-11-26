import models._
import org.scalatest.{FlatSpec, Matchers}
import services.Inventory

class EcomTest extends FlatSpec with Matchers {

  val prod1 = Product("2 States", 104.0, Book.Fiction)
  val prod2 = Product("Inception", 202.0, Book.Fiction, isImported = true)

  val loc1 = Location(29084.0, 29404.0, "NCR")
  val seller1 = Seller("NCR Enterprises", loc1)

  val item1 = Item(prod1, seller1, quantity = 5)
  val item2 = Item(prod2, seller1, quantity = 2)

  it should "check if products in inventory are in-stock" in {
    val inventory = Inventory(Map(prod1 -> 2, prod2 -> 5 ))
    inventory.filter(prod=>prod.price < 200.0, reqQty = 10)("IN_STOCK").isEmpty shouldBe true
    inventory.filter(prod=>prod.price < 200.0, reqQty = 1)("IN_STOCK").nonEmpty shouldBe true
    inventory.isProductInStock(prod1, 5) shouldBe false
    inventory.isProductInStock(prod2, 2) shouldBe true
  }

  it should "add product to cart if it's in stock" in {
    val inventory = Inventory(Map(prod1 -> 7, prod2 -> 2 ))
    inventory.addProductToCart(Cart(),  Item(prod1, seller1, quantity = 5)) shouldBe true
    inventory.addProductToCart(Cart(),  Item(prod2, seller1, quantity = 8)) shouldBe false
  }

  it should "checkout from cart if there are items available in cart" in {
    Cart().checkout().isEmpty shouldBe true
    Cart(Seq(item1, item2)).checkout().nonEmpty shouldBe true
  }

}
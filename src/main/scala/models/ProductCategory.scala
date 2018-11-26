package models

sealed abstract class ProductCategory(val name: String) {
  def isDiscountable(): Boolean = true

  override def equals(obj: Any): Boolean = {
    obj match {
      case prodCat: ProductCategory => name.equals(prodCat.name)
      case _ => false
    }
  }

  override def hashCode(): Int = name.hashCode()
  override def toString: String = name.toString
}

object Book extends ProductCategory("Book") {
  object Management extends ProductCategory("Management") {
    override def isDiscountable(): Boolean = false
  }
  object Science extends ProductCategory("Science")
  object Fiction extends ProductCategory("Fiction")
  object SelfImprovement extends ProductCategory("SelfImprovement")
}
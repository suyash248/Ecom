package services

sealed abstract class DiscountPolicy {
  def discountRatio(price: Double): Double
}

object DefaultDiscountPolicy extends DiscountPolicy {
  override def discountRatio(price: Double): Double = {
    price match {
      case p: Double if p >=500 && p < 1000 => 0.05
      case p: Double if p >=1000 && p < 1500 => 0.10
      case p: Double if p >=1500 && p < 2000 => 0.15
      case p: Double if p >=2000 => 0.20
      case _ => 0.0
    }
  }
}

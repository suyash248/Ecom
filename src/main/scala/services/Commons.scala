package services

object Commons {

  def uuid4(): String = java.util.UUID.randomUUID.toString
  def scale(v: Double, places: Int = 2): Double = BigDecimal(v).setScale(places, BigDecimal.RoundingMode.HALF_UP).toDouble
  def roundOff(v: Double): Int = math.ceil(v).toInt
}

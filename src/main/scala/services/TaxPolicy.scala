package services

object TaxPolicy {
  val regionalTaxPolicy: Map[String, TaxPolicy] = Map(
                                "NCR" -> NCRRegionTaxPolicy,
                                "KARNATAKA" -> KarnatakaRegionTaxPolicy
                          ).withDefaultValue(DefaultTaxPolicy)

  def apply(region: String): TaxPolicy = regionalTaxPolicy(region)
}

sealed abstract class TaxPolicy {
  val VAT_RATIO: Double = 0.0
  val SALES_TAX_RATIO: Double = 0.0
  val IMPORT_DUTY_RATIO: Double = 0.0
}

object DefaultTaxPolicy extends TaxPolicy {
  override val VAT_RATIO: Double = 0.02
  override val SALES_TAX_RATIO: Double = 0.08
  override val IMPORT_DUTY_RATIO: Double = 0.01
}

object NCRRegionTaxPolicy extends TaxPolicy {
  override val VAT_RATIO: Double = 0.04
  override val SALES_TAX_RATIO: Double = 0.07
  override val IMPORT_DUTY_RATIO: Double = 0.05
}

object KarnatakaRegionTaxPolicy extends TaxPolicy {
  override val VAT_RATIO: Double = 0.05
  override val SALES_TAX_RATIO: Double = 0.04
  override val IMPORT_DUTY_RATIO: Double = 0.02
}



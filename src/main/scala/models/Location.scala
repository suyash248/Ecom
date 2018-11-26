package models

import services.{TaxPolicy}

case class Location(longitude: Double, latitude: Double, region: String) {

  /**
    * @return The tax policy applicable for the region associated with this location.
    */
  def taxPolicy(): TaxPolicy = TaxPolicy(region)

  override def equals(obj: Any): Boolean = {
    obj match {
      case loc: Location => longitude.equals(loc.longitude) && latitude.equals(loc.latitude)
      case _ => false
    }
  }

  override def hashCode(): Int = 31 * (longitude.hashCode() + latitude.hashCode())
}

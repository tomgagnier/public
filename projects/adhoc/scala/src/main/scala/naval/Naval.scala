package naval

object Naval {

  trait CombatModifier {
    def wind: Int
    def naval: Int
  }

  object GreatBritain extends CombatModifier { val wind = 1; val naval = 1 }
  object France       extends CombatModifier { val wind = 0; val naval = 0 }
  object Nelson       extends CombatModifier { val wind = 1; val naval = 0 }

  /** http://www.boardgaming.info/EIA-archive/charts/naval_chart.html */
  def percent(roll: Int): Int =
    if (roll < 2) 5
    else if (roll == 2) 10
    else if (roll == 3 || roll == 4) 15
    else if (roll == 5) 20
    else 25

  /** http://www.boardgaming.info/EIA-archive/charts/casualtypercentagetable.html */
  def losses(percent: Int, factor: Int): Int = {
    val table = Map(
      //          1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20
      05 -> Array(0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
      10 -> Array(0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2),
      15 -> Array(0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3),
      20 -> Array(0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4),
      25 -> Array(0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5))

    def casualties(factor: Int) = table(percent)((factor - 1) % 20)

    casualties(factor) + (factor - 1) / 20 * casualties(20)
  }

  def sum(values:Traversable[Int]): Int = values.foldLeft(0) { (value1, value2) => value1 + value2 }

  class Fleet(val name:String, val ships: Int, modifiers: Traversable[CombatModifier]) {
    val windModifier  = sum(modifiers.map(_.wind))
    val navalModifier = sum(modifiers.map(_.naval))

    def -(ships:Int) = new Fleet(name, this.ships - ships, modifiers)

    def damage(fleet: Fleet, roll: Int): Fleet =
      fleet - losses(percent(roll + navalModifier), ships)

    override def toString:String = name + " fleet of " + ships

    override def equals(that:Any): Boolean =
      that.isInstanceOf[Fleet] &&
      windModifier        == that.asInstanceOf[Fleet].windModifier &&
      navalModifier == that.asInstanceOf[Fleet].navalModifier &&
      ships               == that.asInstanceOf[Fleet].ships

    override def hashCode(): Int = modifiers.hashCode * 17 + ships
  }

  def engage(fleet1: Fleet, wind1:Int, combat1: Int,
             fleet2: Fleet, wind2:Int, combat2: Int): (Fleet, Fleet) = {
    val windAdvantage = fleet1.windModifier + wind1 - fleet2.windModifier - wind2
    if (windAdvantage > 0) {
      val f2 = fleet1.damage(fleet2, combat1)
      (f2.damage(fleet1, combat2), f2)
    } else if (windAdvantage < 0) {
      val f1 = fleet2.damage(fleet1, combat2)
      (f1, f1.damage(fleet2, combat1))
    } else {
      (fleet2.damage(fleet1, combat2), fleet1.damage(fleet2, combat1))
    }
  }
}


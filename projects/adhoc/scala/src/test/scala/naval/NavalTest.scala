package naval

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class NavalTest extends FunSuite {

  import naval.Naval._

  /*
   * Great Britain and France are at war. It is Great Britain's Naval Phase.
   * Great Britain moves NELSON and 3 fleets with 60 ships total into a sea
   * area containing 3 French fleets with 31 total ships.
   *
   * First, wind gauge is determined: Great Britain rolls a "2",
   * which becomes a "4" after adding modifiers for NELSON and British fleets.
   * France rolls an unmodified "5". France wins the wind gauge and fights first.
   *
   * France rolls a "5" on the Naval Combat Table. This means 20 % of the number
   * of French ships is inflicted as ship losses on the British. 20% of 31 is 6
   * ships (on the CASUALTY PERCENTAGE TABLE, cross grid the "20%" line with "20"
   * factors and then with "1" factors and add together to get "6").
   *
   * Great Britain rolls a "2", which becomes a "3" after adding the modifier
   * for British fleets. This results in 8 French ship losses
   * (60 British ships minus 6 ships lost = 54 ships or "20" plus "20" plus
   * "14" factors on the "15%" line of the CASUALTY PERCENTAGE TABLE = "8")
   * being suffered by the French.
   */

  val english = new Fleet("English", 60, List(GreatBritain, Nelson))
  val french  = new Fleet("French", 31, List(France))

  test("Naval Chart") {
    expect( 5) { percent(0) }
    expect( 5) { percent(1) }
    expect(10) { percent(2) }
    expect(15) { percent(3) }
    expect(15) { percent(4) }
    expect(20) { percent(5) }
    expect(25) { percent(6) }
    expect(25) { percent(7) }
  }

  test("Casualty Percentage Table") {
    expect(0) { losses(20,  2)}
    expect(2) { losses(20,  8)}
    expect(2) { losses(20, 12)}
    expect(3) { losses(20, 13)}

    expect(0) { losses(25,  1)}
    expect(1) { losses(25,  2)}
    expect(2) { losses(25,  8)}
    expect(3) { losses(25, 10)}
    expect(4) { losses(25, 14)}
    expect(5) { losses(25, 18)}

    expect(11) { losses(25, 45)}
  }

  test("sum") {
    expect(10) { sum(List(1, 2, 3, 4)) }
  }

  test("Fleet - losses") {
    expect(english.ships - 6) { (english - 6).ships }
  }

  test("Fleet damage") {
    expect(english - 6) { french.damage(english, 5) }
    expect(french - 9) {  english.damage(french, 2) }
  }

  test("engage when french fleet has the wind advantage") {
    expect((english - 6, french - 8)) { engage(english, 2, 2, french, 5, 5) }
  }

  test("engage when english fleet has the wind advantage") {
    expect((english - 4, french - 9)) { engage(english, 4, 2, french, 5, 5) }
  }

  test("engage when there is no wind advantage") {
    expect((english - 6, french - 9)) { engage(english, 3, 2, french, 5, 5) }
  }

  test("Print all possible results") {
    for (wind1 <- 1 to 6)
      for (wind2 <- 1 to 6)
        for (combat1 <- 1 to 6)
          for (combat2 <- 1 to 6)
            println(engage(english, wind1, combat1, french, wind2, combat2))
  }
}

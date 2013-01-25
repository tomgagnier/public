package intellij.badenum;

enum Planets implements Planet {
  MARS {
    public double gravity() {
      return 0.386;
    }
  }
}

package interview.zoo2;

public class Lion extends DefaultAnimal {
    @Override
    public String speak() {
        return super.speak() + "ROAR";
    }
}

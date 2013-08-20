package interview.zoo2;

public class Bear extends DefaultAnimal implements TrainedAnimal {

    @Override
    public void sleep() {
        energy += 10;
    }

    public String dance() {
        return "Look Ma! I'm Dancing.";
    }
}

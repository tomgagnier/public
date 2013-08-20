package interview.zoo2;

public class Tiger extends DefaultAnimal {
    @Override
    int energyRequiredToPlay() {
        return 8;
    }

    @Override
    public void eat(Food food) {
        if (food != Food.GRAIN)
            super.eat(food);
    }
}

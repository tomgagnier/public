package interview.zoo;

public class Animal {

    private final Species species;

    int energy;

    public Animal(Species species) {
        this.species = species;
        ++species.population;
    }

    void eat(Food food) {
        species.eat(this, food);
    }

    void sleep() {
        species.sleep(this);
    }

    String speak() {
        return species.speak(this);
    }

    String play() {
        if (energy < species.energyRequiredToPlay())
            return "I'm tired";
        energy -= species.energyRequiredToPlay();
        return "YE-AH";
    }

    String dance() {
        return species.dance();
    }
}

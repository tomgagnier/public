package interview.zoo2;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultAnimal implements Animal {

    private static Map<Class<?>, Integer> population = new HashMap<Class<?>, Integer>();

    int energy = 0;

    DefaultAnimal() {
        population.put(getClass(), population.containsKey(getClass()) ? population() + 1 : 1);
    }

    public int population() {
        return population.get(getClass());
    }

    int energyRequiredToPlay() {
        return 5;
    }

    public void eat(Food food) {
        energy += 3;
    }

    public String play() {
        if (energy < energyRequiredToPlay())
            return "I'm tired";
        energy -= energyRequiredToPlay();
        return "YE-AH";
    }

    public String speak() {
        return "Grrr";
    }

    public void sleep() {
        energy += 3;
    }
}

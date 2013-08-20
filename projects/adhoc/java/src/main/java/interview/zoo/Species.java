package interview.zoo;

public enum Species {
    LION {
        @Override
        public String speak(Animal animal) {
            return super.speak(animal) + "ROAR";
        }
    },
    TIGER {
        @Override
        public int energyRequiredToPlay() {
            return 8;
        }

        @Override
        public void eat(Animal animal, Food food) {
            if (food != Food.GRAIN)
                super.eat(animal, food);
        }
    },
    BEAR {
        @Override
        public void sleep(Animal animal) {
            animal.energy += 10;
        }

        @Override
        public String dance() {
            return "Look Ma! I'm Dancing.";
        }
    };

    public int population = 0;

    public String dance() {
        throw new UnsupportedOperationException(this.name() + " can not dance");
    }

    public void eat(Animal animal, Food food) {
        animal.energy += 3;
    }

    public void sleep(Animal animal) {
        animal.energy += 8;
    }

    public String speak(Animal animal) {
        return "Grrr";
    }

    public int energyRequiredToPlay() {
        return 5;
    }
}

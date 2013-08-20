package interview.zoo2;

import junit.framework.TestCase;

public class ZooTest extends TestCase {

    public void testPopulation() {
        assertEquals(1, new Lion().population());
        assertEquals(2, new Lion().population());
        assertEquals(1, new Bear().population());
        assertEquals(2, new Bear().population());
        assertEquals(3, new Bear().population());
        assertEquals(4, new Bear().population());
        assertEquals(1, new Tiger().population());
    }

    public void testLion() {
        Animal animal = new Lion();

        assertEquals("GrrrROAR", animal.speak());
        assertEquals("I'm tired", animal.play());
        animal.sleep();
        assertEquals("I'm tired", animal.play());
        animal.sleep();
        assertEquals("YE-AH", animal.play());
        assertEquals("I'm tired", animal.play());
    }

    public void testTiger() {
        Animal animal = new Tiger();

        assertEquals("Grrr", animal.speak());
        assertEquals("I'm tired", animal.play());
        animal.sleep();
        assertEquals("I'm tired", animal.play());
        animal.sleep();
        assertEquals("I'm tired", animal.play());
        animal.sleep();
        assertEquals("YE-AH", animal.play());
        assertEquals("I'm tired", animal.play());
    }

    public void testBear() {
        TrainedAnimal animal = new Bear();

        assertEquals("Grrr", animal.speak());
        assertEquals("I'm tired", animal.play());
        animal.sleep();
        assertEquals("YE-AH", animal.play());
        assertEquals("YE-AH", animal.play());
        assertEquals("I'm tired", animal.play());
        assertEquals("Look Ma! I'm Dancing.", animal.dance());
    }

}

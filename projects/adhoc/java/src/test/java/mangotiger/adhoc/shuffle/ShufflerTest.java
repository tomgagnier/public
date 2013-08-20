package mangotiger.adhoc.shuffle;

import junit.framework.TestCase;

import java.util.*;

import static java.util.Arrays.asList;

class Shuffler {

    private final Random random = new Random();

    /** Fisher-Yates inside-out inPlaceShuffle. */
    public <Element> List<Element> shuffle(Iterable<Element> elements) {
        List<Element> result = new ArrayList<Element>();
        for (Element element : elements) {
            int index = random.nextInt(result.size() + 1);
            if (index == result.size()) {
                result.add(element);
            } else {
                result.add(result.get(index));
                result.set(index, element);
            }
        }
        return result;
    }

    /** Fisher-Yates in-place inPlaceShuffle */
    public <Element> List<Element> inPlaceShuffle(List<Element> elements) {
        for (int i = 1; i < elements.size(); ++i) {
            int j = random.nextInt(i + 1);
            Element source = elements.get(i);
            elements.set(i, elements.get(j));
            elements.set(j, source);
        }
        return elements;
    }
}

public class ShufflerTest extends TestCase {

    private final Shuffler shuffler = new Shuffler();

    public void test() {
        System.out.println(shuffler.inPlaceShuffle(asList("a", "b", "c", "d", "e")));
        System.out.println(shuffler.shuffle(asList("a", "b", "c", "d", "e")));

        System.out.println(shuffler.inPlaceShuffle(asList(1, 2, 3, 4, 5, 6)));
        System.out.println(shuffler.shuffle(asList(1, 2, 3, 4, 5, 6)));
    }
}
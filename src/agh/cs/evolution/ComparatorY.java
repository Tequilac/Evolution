package agh.cs.evolution;

import java.util.Comparator;

public class ComparatorY implements Comparator<Vector2d>
{

    @Override
    public int compare(Vector2d vec1, Vector2d vec2) {
        if (vec1.y < vec2.y)
            return -1;
        if (vec1.y == vec2.y && vec1.x <= vec2.x)
            return -1;
        return 1;
    }
}

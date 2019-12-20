package agh.cs.evolution;



import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver
{
    public SortedSet<Vector2d> vectorsX = new TreeSet<Vector2d>(new ComparatorX());
    public void addVector (Vector2d vector)
    {
        vectorsX.add(vector);
    }
    public void removeVector (Vector2d vector)
    {
        vectorsX.remove(vector);
    }
    public void positionChanged (Vector2d oldPosition, Vector2d newPosition)
    {
        removeVector(oldPosition);
        addVector(newPosition);
    }
    public void register (Vector2d position)
    {
        addVector(position);
    }
    public void deregister (Vector2d position)
    {
        removeVector(position);
    }
    public List<Vector2d> searchForSame ()
    {
         Vector2d [] vectors = new Vector2d[vectorsX.size()];
         vectorsX.toArray(vectors);
         List<Vector2d> found = new LinkedList<>();
         int j = 0;
         found.add(0,new Vector2d(-1,-1));
         for (int i=1; i<vectors.length; i++)
         {
             if(vectors[i].equals(vectors[i-1]) && !(vectors[i].equals(found.get(j))))
             {
                 j++;
                 found.add(j,vectors[i]);
             }
         }
         return found;
    }

}

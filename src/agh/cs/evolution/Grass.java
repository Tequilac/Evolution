package agh.cs.evolution;

public class Grass
{
    public Vector2d Position;
    public Grass(Vector2d Position)
    {
        this.Position = Position;
    }
    public Vector2d getPosition()
    {
        return Position;
    }
    public String toString()
    {
        return "*";
    }

}

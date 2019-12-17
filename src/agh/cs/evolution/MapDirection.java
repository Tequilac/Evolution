package agh.cs.evolution;


import java.util.Random;

public enum MapDirection
{
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString()
    {
        switch(this)
        {
            case NORTH: return "N";
            case NORTHEAST: return "NE";
            case EAST: return "E";
            case SOUTHEAST: return "SE";
            case SOUTH: return "S";
            case SOUTHWEST: return "SW";
            case WEST: return "W";
            default: return "NW";
        }
    }
    public Vector2d toUnitVector ()
    {
        switch(this)
        {
            case NORTH: return new Vector2d(0, 1);
            case NORTHEAST: return new Vector2d(1, 1);
            case EAST: return new Vector2d(1, 0);
            case SOUTHEAST: return new Vector2d(1, -1);
            case SOUTH: return new Vector2d(0, -1);
            case SOUTHWEST: return new Vector2d(-1, -1);
            case WEST: return new Vector2d(-1,0);
            default: return new Vector2d(-1, 1);
        }
    }
    public static MapDirection getRandomDirection()
    {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public MapDirection next ()
    {
        switch (this)
        {
            case NORTHWEST: return NORTH;
            case NORTH: return NORTHEAST;
            case NORTHEAST: return EAST;
            case EAST: return SOUTHEAST;
            case SOUTHEAST: return SOUTH;
            case SOUTH: return SOUTHWEST;
            case SOUTHWEST: return WEST;
            default: return NORTHWEST;
        }
    }
    public MapDirection turn (int gene)
    {
        MapDirection dir = this;
        for (int i=0; i<gene; i++)
            dir=dir.next();
        return dir;
    }
}

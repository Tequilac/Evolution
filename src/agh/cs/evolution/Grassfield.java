package agh.cs.evolution;

import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class Grassfield implements IWorldMap, IPositionChangeObserver
{
    protected List<Animal> animals = new ArrayList<>();
    protected List<Grass> grasses = new ArrayList<>();
    protected Map<Vector2d, Animal> all = new LinkedHashMap<>();
    protected MapBoundary mb = new MapBoundary();
    protected int width;
    protected int height;
    protected double jungleRatio;
    protected Vector2d jungleLowerLeft;
    protected Vector2d jungleUpperRight;
    protected int moveEnergy;
    protected int plantEnergy;
    protected int startEnergy;
    protected Statistic stats;

    public Grassfield(long width, long height, double jungleRatio, long moveEnergy, long plantEnergy, long startEnergy, int aniNum)
    {
        this.width=(int)width;
        this.height=(int)height;
        this.jungleRatio=jungleRatio;
        this.jungleLowerLeft = new Vector2d((int) (this.width-this.width*jungleRatio)/2,(int)(this.height-this.height*jungleRatio)/2);
        this.jungleUpperRight = new Vector2d((int)(jungleLowerLeft.x+this.width*jungleRatio),(int)(jungleLowerLeft.y+this.height*jungleRatio));
        this.moveEnergy=(int)moveEnergy;
        this.plantEnergy=(int)plantEnergy;
        this.startEnergy=(int)startEnergy;
        this.stats=new Statistic(animals);
        int i = 0;
        while(i<aniNum)
        {
            Animal animal = new Animal(this, startEnergy);
            if(place(animal))
                i++;
        }

        int grasses = (int)(this.width*this.height*jungleRatio)/20;
        for(int j=0; j<grasses; j++)
        {
            placeGrass();
        }

    }

    public void placeGrass ()
    {
        int i = 0;
        int tries = (int)(width*height*jungleRatio*jungleRatio*(0.75));
        int x = (int)(Math.random() * (this.jungleUpperRight.x-this.jungleLowerLeft.x+1) + this.jungleLowerLeft.x);
        int y = (int)(Math.random() * (this.jungleUpperRight.y-this.jungleLowerLeft.y+1) + this.jungleLowerLeft.y);
        while(i<tries && isOccupied(new Vector2d(x,y)))
        {
            x = (int)(Math.random() * (this.jungleUpperRight.x-this.jungleLowerLeft.x+1) + this.jungleLowerLeft.x);
            y = (int)(Math.random() * (this.jungleUpperRight.y-this.jungleLowerLeft.y+1) + this.jungleLowerLeft.y);
            i++;
        }
        Grass grass = new Grass(new Vector2d(x, y));
        grasses.add(grass);
        mb.register(grass.getPosition());
        int xOut = (int)(Math.random() * (this.width));
        int yOut = (int)(Math.random() * (this.height));
        while(isOccupied(new Vector2d(xOut,yOut)) || (new Vector2d(xOut,yOut).follows(this.jungleLowerLeft) && new Vector2d(xOut,yOut).precedes(this.jungleUpperRight)))
        {
            xOut = (int)(Math.random() * (this.width));
            yOut = (int)(Math.random() * (this.height));
        }
        Grass grassOut = new Grass(new Vector2d(xOut, yOut));
        grasses.add(grassOut);
        mb.register(grassOut.getPosition());
    }
    public String toString() {
        MapVisualizer drawer = new MapVisualizer(this);
        Vector2d lowerLeft;
        Vector2d upperRight;
        if(this.animals.size() == 0 && this.grasses.size() == 0)
        {
            return drawer.draw(new Vector2d(0,0), new Vector2d(0,0));
        }
        if(this.animals.size() != 0)
        {
            lowerLeft = (animals.get(0)).getPosition();
            upperRight = (animals.get(0)).getPosition();
        }
        else
        {
            lowerLeft = (grasses.get(0)).getPosition();
            upperRight = (grasses.get(0)).getPosition();
        }

        lowerLeft=new Vector2d(min(lowerLeft.x,mb.vectorsX.first().x),min(lowerLeft.y,mb.vectorsY.first().y));
        upperRight=new Vector2d(max(upperRight.x,mb.vectorsX.last().x),max(upperRight.y,mb.vectorsY.last().y));
        return drawer.draw(lowerLeft, upperRight);
    }

    public void removeDead ()
    {
        Animal animal;
        for(int i=0;i<animals.size();i++)
        {
            animal=animals.get(i);
            if(animals.get(i).getEnergy()<this.moveEnergy)
            {
                animals.remove(animal);
                all.remove(animal);
                animal.removeObserver(mb);
                mb.deregister(animal.getPosition());
                i--;
            }
        }
    }

    public void run()
    {
        for (Animal animal : animals)
        {
            Vector2d pos = animal.getPosition();
            animal.move(this.moveEnergy);
            positionChanged(pos, animal.getPosition());
        }
    }

    public boolean canMoveTo(Vector2d position)
    {
        if(this.all.containsKey(position))
            return false;
        return true;
    }


    public Vector2d findNewPlace()
    {
        int x = (int) (Math.random()*this.width);
        int y = (int) (Math.random()*this.height);
        while(objectAt(new Vector2d(x,y))!=null)
        {
            x = (int) (Math.random()*this.width);
            y = (int) (Math.random()*this.height);
        }
        return new Vector2d(x,y);
    }

    public boolean place(Animal animal)
    {
        if((this.canMoveTo(animal.getPosition())))
        {
            animals.add(animal);
            all.put(animal.getPosition(),animal);
            animal.addObserver(mb);
            mb.register(animal.getPosition());
            return true;
        }
        return false;
    }

    public void searchForReproduceAndEating ()
    {
        List<Vector2d> found = mb.searchForSame();
        for (Vector2d vector : found)
        {
            List <Animal> samePosAni = new LinkedList<>();
            List <Grass> samePosGra = new LinkedList<>();
            for (Animal animal : animals)
            {
                if(vector.equals(animal.getPosition()))
                {
                    samePosAni.add(animal);
                }
            }
            for (Grass grass : grasses)
            {
                if(vector.equals(grass.getPosition()))
                {
                    samePosGra.add(grass);
                }
            }
            if(samePosGra.size()>0 && samePosAni.size()>0)
                chooseForEating(samePosAni, samePosGra.get(0));
            if(samePosAni.size()>1)
                chooseForReproduce(samePosAni);

        }
    }

    public void chooseForReproduce (List<Animal> ani)
    {
        Animal parent1 = ani.get(0);
        for (int i=1;i<ani.size(); i++)
        {
            if(ani.get(i).getEnergy()>parent1.getEnergy())
                parent1=ani.get(i);
        }
        if(parent1.getEnergy()<this.startEnergy/2)
            return;
        ani.remove(parent1);
        Animal parent2 = ani.get(0);
        for (int i=1;i<ani.size(); i++)
        {
            if(ani.get(i).getEnergy()>parent2.getEnergy())
                parent2=ani.get(i);
        }
        if(parent2.getEnergy()<this.startEnergy/2)
            return;
        reproduce(parent1, parent2);
    }

    public void reproduce (Animal parent1, Animal parent2)
    {
        Vector2d pos = placeForNewAnimal(parent1);
        Animal child = new Animal(this, parent1, parent2, pos);
        this.place(child);
    }

    public Vector2d placeForNewAnimal(Animal parent)
    {
        Vector2d parPos = parent.getPosition();
        for (int i = -1; i<=1; i++)
        {
            for (int j=-1; j<=1; j++)
            {
                if(!(i==0 && j==0) && canMoveTo(new Vector2d(((parPos.x+i+this.width)%this.width), ((parPos.y+j+this.height)%this.height))))
                {
                    return new Vector2d(((parPos.x+i+this.width)%this.width), ((parPos.y+j+this.height)%this.height));
                }
            }
        }
        return parPos;
    }

    public void chooseForEating (List<Animal> ani, Grass grass)
    {
        List <Animal> maxEneAni = new LinkedList<>();
        int maxEne = ani.get(0).getEnergy();
        for (int i=1;i<ani.size(); i++)
        {
            if(ani.get(i).getEnergy()>maxEne)
                maxEne=ani.get(i).getEnergy();
        }
        for (int i=0;i<ani.size(); i++)
        {
            if(ani.get(i).getEnergy()==maxEne)
                maxEneAni.add(ani.get(i));
        }
        for (Animal animal : maxEneAni)
        {
            animal.eat((plantEnergy/maxEneAni.size()));
        }
        grasses.remove(grass);
        mb.deregister(grass.getPosition());
    }


    public boolean isOccupied(Vector2d position)
    {
        if(this.all.containsKey(position))
            return true;
        for (int i = 0; i < grasses.size(); i++)
        {
            if(((grasses.get(i)).getPosition()).equals(position))
                return true;
        }
        return false;
    }

    public Object objectAt(Vector2d position)
    {
        return this.all.getOrDefault(position, null);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition)
    {
        Animal ani = all.get(oldPosition);
        all.remove(oldPosition, ani);
        all.put(newPosition, ani);
    }

    public Vector2d adjust (Vector2d pos)
    {
        int posx = (pos.x+width)%width;
        int posy = (pos.y+height)%height;
        return new Vector2d(posx, posy);
    }

    public void exportStats () throws FileNotFoundException {
        this.stats.export(animals.size(),grasses.size());
    }
}

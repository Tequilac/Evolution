package agh.cs.evolution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Animal
{
    private MapDirection mapDir;
    private Vector2d mapPos;
    private GeneSet genes;
    private int energy;
    private List<IPositionChangeObserver> observers = new ArrayList<>();
    private IWorldMap map;
    private List <Animal> parents = new LinkedList<>();
    private int children=0;
    private int age=0;
    public List <Animal> descendants = new LinkedList<>();

    public Animal(IWorldMap map, long startEnergy)
    {
        this.mapDir=MapDirection.getRandomDirection();
        this.map=map;
        this.mapPos=map.findNewPlace();
        this.genes=new GeneSet();
        this.energy=(int)startEnergy;
        this.parents=new LinkedList<>();
    }

    public Animal(IWorldMap map, Animal parent1, Animal parent2, Vector2d position)
    {
        this.energy=parent1.energy/4 + parent2.energy/4;
        parent1.energy=(3*parent1.energy/4);
        parent2.energy=(3*parent2.energy/4);
        this.genes=new GeneSet(parent1.genes, parent2.genes);
        this.map=map;
        this.mapDir=MapDirection.getRandomDirection();
        this.mapPos=position;
        this.parents.add(parent1);
        parent1.children++;
        this.parents.add(parent2);
        parent2.children++;
        this.addDescendant(this.parents);

    }
    public void addDescendant (List<Animal> parents)
    {
        for(Animal animal : parents)
        {
            if(!animal.descendants.contains(this))
            {
                animal.descendants.add(this);
                if(!animal.parents.isEmpty())
                this.addDescendant(animal.parents);
            }
        }
    }


    public String toString()
    {
        String info ="Descendants: "+ this.descendants.size()+ " Children: " + this.getChildren() + " Genome: ";
        for(int i = 0; i<32; i++)
            info=info+this.genes.geneSet[i];
        if(this.energy==-1)
            info=info+" Death day: "+this.age;
        return info;
    }
    public void die ()
    {
        this.energy=-1;
    }
    public void move(int moveEnergy)
    {
        int gene = this.genes.getGene();
        this.mapDir=this.mapDir.turn(gene);
        Vector2d mPos = this.mapPos;
        mPos = ((this.mapDir).toUnitVector()).add(mPos);
        mPos = map.adjust(mPos);
        this.mapPos = mPos;
        positionChanged(this.mapPos,mPos);
        this.energy=this.energy-moveEnergy;
        this.age++;
    }

    public void eat (int energy)
    {
        this.energy=this.energy+energy;
    }

    public Vector2d getPosition()
    {
        return this.mapPos;
    }
    public int getEnergy() {return this.energy;}
    public int [] getGenes() {return this.genes.geneSet;}
    public int getChildren() {return this.children;}
    public int getAge() {return this.age;}

    void addObserver(IPositionChangeObserver observer)
    {
        observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer)
    {
        observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition)
    {
        for (IPositionChangeObserver observer : observers)
        {
            observer.positionChanged(oldPosition, newPosition);
        }
    }
}

package agh.cs.evolution;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Statistic
{
    Map<GeneSet,Integer> genes=new LinkedHashMap<>();
    int [] dominantGenome=new int[32];
    double averageEnergy;
    double averageAge;
    double averageChildren;
    double averageDeadAge;
    int deadAnimals;
    double sumDeadDays;
    double sumEne;
    double sumAge;
    double sumChild;
    double sumAverageEnergy;
    double sumAverageAge;
    double sumAverageChildren;
    double sumAverageDeadAge;
    double sumAnimals;
    double sumGrasses;
    public Statistic (List<Animal> animals, int graCount)
    {
        this.sumEne=0;
        this.sumAge=0;
        this.sumChild=0;
        this.deadAnimals=0;
        this.sumDeadDays=0;
        for (int i=0; i<animals.size(); i++)
        {
            Animal animal = animals.get(i);

            sumEne=sumEne+animal.getEnergy();
            sumAge=sumAge+animal.getAge();
            sumChild=sumChild+animal.getChildren();
        }

        if(animals.size()>0)
        {
            this.averageEnergy=(sumEne/animals.size());
            this.averageAge=(sumAge/animals.size());
            this.averageChildren=(sumChild/animals.size());
        }
        else
        {
            this.averageEnergy=0;
            this.averageAge=0;
            this.averageChildren=0;
        }
        this.averageDeadAge=0;
        this.sumAverageEnergy=this.averageEnergy;
        this.sumAverageAge=this.averageAge;
        this.sumAverageChildren=this.averageChildren;
        this.sumAverageDeadAge=this.averageDeadAge;
        this.sumAnimals=animals.size();
        this.sumGrasses=graCount;
    }
    public void update (List<Animal> animals, int graCount)
    {
        this.sumEne=0;
        this.sumAge=0;
        this.sumChild=0;
        for (int i=0; i<animals.size(); i++)
        {
            Animal animal = animals.get(i);
            sumEne=sumEne+animal.getEnergy();
            sumAge=sumAge+animal.getAge();
            sumChild=sumChild+animal.getChildren();
        }
        if(animals.size()>0)
        {
            this.averageEnergy=(sumEne/animals.size());
            this.averageAge=(sumAge/animals.size());
            this.averageChildren=(sumChild/animals.size());

        }
        else
        {
            this.averageEnergy=0;
            this.averageAge=0;
            this.averageChildren=0;
        }
        if(deadAnimals>0)
            this.averageDeadAge=sumDeadDays/deadAnimals;
        findDominantGenome();
        this.sumAverageEnergy+=this.averageEnergy;
        this.sumAverageAge+=this.averageAge;
        this.sumAverageChildren+=this.averageChildren;
        this.sumAverageDeadAge+=this.averageDeadAge;
        this.sumAnimals+=animals.size();
        this.sumGrasses+=graCount;
    }
    public void export (String qua, int day) throws FileNotFoundException {
        StatisticParser parser = new StatisticParser();
        parser.parse(this.sumAnimals/day, this.sumGrasses/day,this.getDominantGenome(),this.sumAverageEnergy/day,this.sumAverageAge/day, this.sumAverageDeadAge/day,this.sumAverageChildren/day, qua);
    }
    public void addGenome (GeneSet genome)
    {
        if(this.genes.containsKey(genome))
        {
            for (Map.Entry<GeneSet, Integer> entry : genes.entrySet())
            {
                if (entry.getKey().equals(genome))
                {
                    Integer i = entry.getValue();
                    this.genes.replace(genome,i,i+1);
                    break;
                }
            }
        }
        else
            this.genes.put(genome,1);
    }
    public void removeGenome (GeneSet genome)
    {
        for (Map.Entry<GeneSet, Integer> entry : genes.entrySet())
        {
            if (entry.getKey().equals(genome))
            {
                Integer i = entry.getValue();
                if(i==1)
                    this.genes.remove(genome);
                else
                    this.genes.replace(genome,i,i-1);
                break;
            }
        }
    }
    public void findDominantGenome ()
    {
        GeneSet genome=null;
        Integer i = 0;
        for (Map.Entry<GeneSet, Integer> entry : genes.entrySet())
        {
            if(entry.getValue()>i)
                genome=entry.getKey();
        }
        if(genome!=null)
            this.dominantGenome=genome.geneSet;
    }

    public String getDominantGenome()
    {
        String domGen="";
        for (int i=0; i<32; i++)
        {
            domGen=domGen+this.dominantGenome[i];
        }
        return domGen;
    }

}

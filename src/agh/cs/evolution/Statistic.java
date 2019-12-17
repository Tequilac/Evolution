package agh.cs.evolution;

import java.io.FileNotFoundException;
import java.util.List;

public class Statistic
{
    int dominatingGene;
    double averageEnergy;
    double averageAge;
    double averageChildren;
    int [] genes = new int [8];
    double sumEne;
    double sumAge;
    double sumChild;
    public Statistic (List<Animal> animals)
    {
        this.sumEne=0;
        this.sumAge=0;
        this.sumChild=0;
        for (int i=0; i<8; i++)
            genes[i]=0;
        for (int i=0; i<animals.size(); i++)
        {
            Animal animal = animals.get(i);
            for(int j=0; j<32; j++)
            {
                genes[animal.getGenes()[j]]++;
            }
            sumEne=sumEne+animal.getEnergy();
            sumAge=sumAge+animal.getAge();
            sumChild=sumChild+animal.getChildren();
        }
        int domGene = 0;
        for (int i=1; i<8; i++)
            if(genes[i]>genes[domGene])
                domGene=i;
        this.dominatingGene=domGene;
        this.averageEnergy=(sumEne/animals.size());
        this.averageAge=(sumAge/animals.size());
        this.averageChildren=(sumChild/animals.size());
    }
    public void update (List<Animal> animals)
    {
        this.sumEne=0;
        this.sumAge=0;
        this.sumChild=0;
        for (int i=0; i<8; i++)
            genes[i]=0;
        for (int i=0; i<animals.size(); i++)
        {
            Animal animal = animals.get(i);
            for(int j=0; j<32; j++)
            {
                genes[animal.getGenes()[j]]++;
            }
            sumEne=sumEne+animal.getEnergy();
            sumAge=sumAge+animal.getAge();
            sumChild=sumChild+animal.getChildren();
        }
        int domGene = 0;
        for (int i=1; i<8; i++)
            if(genes[i]>genes[domGene])
                domGene=i;
        this.dominatingGene=domGene;
        this.averageEnergy=(sumEne/animals.size());
        this.averageAge=(sumAge/animals.size());
        this.averageChildren=(sumChild/animals.size());
    }
    public void export (int aniCount, int graCount) throws FileNotFoundException {
        StatisticParser parser = new StatisticParser();
        parser.parse(aniCount, graCount,this.dominatingGene,this.averageEnergy,this.averageAge,this.averageChildren);
    }

}

package agh.cs.evolution;

import java.util.Arrays;

public class GeneSet
{
    public int[] geneSet = new int[32];
    public GeneSet ()
    {
        for (int i=0; i<32; i++)
        {
            if (i<8)
                this.geneSet[i]= i;
            else
                this.geneSet[i]= (int) (Math.random()*8);
        }
        Arrays.sort(this.geneSet);
    }
    public GeneSet (GeneSet genes1, GeneSet genes2)
    {
        int firstCut = (int) (Math.random()*30) + 1;
        int secondCut = (int) (Math.random()*(32-firstCut)) + firstCut;
        for (int i = 0; i<32; i++)
        {
            if(i<firstCut || i>=secondCut)
                this.geneSet[i]=genes1.geneSet[i];
            else
                this.geneSet[i]=genes2.geneSet[i];
        }
        boolean [] check = this.checkIfAll();
        boolean areAll = true;
        for (int i=0; i<8; i++)
            areAll = areAll && check[i];
        while (!areAll)
        {
            for (int i = 0; i<8; i++)
            {
                if (!check[i])
                    this.geneSet[(int) (Math.random()*32)] = i;
            }
            check = this.checkIfAll();
            areAll=true;
            for (int i=0; i<8; i++)
                areAll = areAll && check[i];
        }
        Arrays.sort(this.geneSet);
    }
    public boolean [] checkIfAll ()
    {
        boolean [] check = new boolean[8];
        for (int i=0; i<8; i++)
            check[i]=false;
        for (int i=0; i<32; i++)
            check[this.geneSet[i]]=true;
        return check;
    }
    public int getGene ()
    {
        return this.geneSet[(int) (Math.random()*32)];
    }

    public boolean equals (GeneSet other)
    {
        boolean equality=true;
        for(int i=0; i<32; i++)
        {
            equality=equality && (this.geneSet[i]==other.geneSet[i]);
        }
        return equality;
    }
}

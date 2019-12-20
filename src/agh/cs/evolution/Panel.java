package agh.cs.evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Panel extends JPanel
{
    Grassfield map;
    JButton [][] buttons;
    JPanel world;
    JPanel statistics;
    Animal aniFollow;
    JLabel stats;
    List<Grass> gra;
    List<Animal> ani;
    List<Animal> deadAni;
    public Panel (Grassfield map, int wid, int hei)
    {
        this.map=map;
        int width=map.width;
        int height=map.height;
        this.world = new JPanel(new GridLayout(width,height));
        this.add(world);
        this.statistics = new JPanel();
        this.add(statistics);
        buttons = new JButton[width][height];
        int size = Math.min(wid/width,hei/height);
        Dimension dim = new Dimension(size,size);
        for(int i=0; i<width; i++)
        {
            for(int j=0; j<height; j++)
            {
                buttons[i][j]=new JButton();
                buttons[i][j].setEnabled(true);
                buttons[i][j].setPreferredSize(dim);
                buttons[i][j].setBackground(new Color(242, 242, 177));
                world.add(buttons[i][j]);
            }

        }
        gra = map.grasses;
        ani = map.animals;

        for(Grass grass : gra)
        {
            int x = grass.getPosition().x;
            int y = grass.getPosition().y;
            buttons[x][y].setBackground(new Color(64, 163, 18));
        }
        for(Animal animal : ani)
        {
            int x = animal.getPosition().x;
            int y = animal.getPosition().y;
            buttons[x][y].setBackground(new Color(((animal.getEnergy()*60/map.startEnergy)),0,((animal.getEnergy()*255/map.startEnergy))));
            buttons[x][y].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    aniFollow=animal;
                    stats = new JLabel(aniFollow.toString());
                    statistics.removeAll();
                    statistics.add(stats);
                    updateUI();
                }
            });
        }


    }
    public void refresh ()
    {
        gra = map.grasses;
        ani = map.animals;
        deadAni = map.deadAnimals;
        for(int i=0; i<map.width; i++)
        {
            for(int j=0; j<map.height; j++)
            {

                for( ActionListener al : buttons[i][j].getActionListeners() ) {
                    buttons[i][j].removeActionListener( al );
                }
                if(map.objectAt(new Vector2d(i,j))==null)
                {
                    buttons[i][j].setBackground(new Color(242, 242, 177));
                    updateUI();
                }

            }
        }
        for(Animal animal : deadAni)
        {
            int x = animal.getPosition().x;
            int y = animal.getPosition().y;
            buttons[x][y].setBackground(new Color(255, 255, 255));
            buttons[x][y].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    aniFollow=animal;
                    stats = new JLabel(aniFollow.toString());
                    statistics.removeAll();
                    statistics.add(stats);
                    updateUI();
                }
            });

        }
        for(Grass grass : gra)
        {
            int x = grass.getPosition().x;
            int y = grass.getPosition().y;
            buttons[x][y].setBackground(new Color(64, 163, 18));
        }
        for(Animal animal : ani)
        {
            int x = animal.getPosition().x;
            int y = animal.getPosition().y;
            buttons[x][y].setBackground(new Color(Math.min((animal.getEnergy()*60/map.startEnergy),60),0,Math.min((animal.getEnergy()*255/map.startEnergy),255)));
            buttons[x][y].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    aniFollow=animal;
                    stats = new JLabel(aniFollow.toString());
                    statistics.removeAll();
                    statistics.add(stats);
                    updateUI();
                }
            });

        }
        updateUI();
        updateFollow();

    }


    public void highlight ()
    {
        List<Animal> ani = map.animals;
        for(Animal animal : ani)
        {
            int x = animal.getPosition().x;
            int y = animal.getPosition().y;
            if(animal.getGenes().equals(map.stats.dominantGenome))
                buttons[x][y].setBackground(new Color(0,255,255));

        }
    }
    public void updateFollow ()
    {
        if(aniFollow!=null && !aniFollow.toString().equals(stats.getText()))
        {
            stats = new JLabel(aniFollow.toString());
            statistics.removeAll();
            statistics.add(stats);
            updateUI();
        }
    }


}

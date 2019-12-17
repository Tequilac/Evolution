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
        List<Grass> gra = map.grasses;
        List<Animal> ani = map.animals;
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
                    JLabel stats = new JLabel(animal.toString());
                    statistics.removeAll();
                    statistics.add(stats);
                    updateUI();
                }
            });
        }
    }
    public void refresh ()
    {
        for(int i=0; i<map.width; i++)
        {
            for(int j=0; j<map.height; j++)
            {
                buttons[i][j].setBackground(new Color(242, 242, 177));
                for( ActionListener al : buttons[i][j].getActionListeners() ) {
                    buttons[i][j].removeActionListener( al );
                }
            }

        }
        List<Grass> gra = map.grasses;
        List<Animal> ani = map.animals;
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
                    JLabel stats = new JLabel(animal.toString());
                    statistics.removeAll();
                    statistics.add(stats);
                    updateUI();
                }
            });
        }
    }





}

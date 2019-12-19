package agh.cs.evolution;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Frame extends JFrame
{
    public Evolution evolution;
    public List <Grassfield> maps = new LinkedList<>();
    public List <Panel> panels = new LinkedList<>();
    JPanel panel;
    public JLabel [] stats;
    String [] quantity = new String[2];
    Thread thread;

    public Frame (Evolution evolution)
    {
        this.evolution=evolution;
        setSize(1920,1080);
        setTitle("Evolution");
        setResizable(false);
        setLayout(new GridLayout());
        JButton button1 = new JButton("One map");
        button1.setBounds(0,0,200, 50);
        add(button1);
        JButton button2 = new JButton("Two maps");
        button2.setBounds(0,50,200, 50);
        add(button2);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                button1.setVisible(false);
                remove(button1);
                button2.setVisible(false);
                remove(button2);
                side();
                try {
                    oneMap();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                stat();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                button1.setVisible(false);
                remove(button1);
                button2.setVisible(false);
                remove(button2);
                side();
                try {
                    twoMaps();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                stat();
            }
        });


    }

    public void oneMap () throws IOException, ParseException
    {
        quantity[0]="";
        Grassfield map = this.evolution.getMap();
        this.maps.add(map);
        Panel panel = new Panel(map, getWidth()/2, getHeight());
        this.panels.add(panel);
        add(panel);

    }
    public void twoMaps () throws IOException, ParseException
    {
        Grassfield map1 = this.evolution.getMap();
        Panel panel1 = new Panel(map1, getWidth()/3, getHeight());
        Grassfield map2 = this.evolution.getMap();
        Panel panel2 = new Panel(map2, getWidth()/3, getHeight());
        this.maps.add(map1);
        this.maps.add(map2);
        this.panels.add(panel1);
        this.panels.add(panel2);
        add(panel1);
        add(panel2);
        quantity[0]="Left ";
        quantity[1]="Right ";
    }
    public void side ()
    {
        panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        JButton play = new JButton("Play");
        JButton stop = new JButton("Stop");
        JButton domGen = new JButton("Highlight dominant gene");
        JButton export = new JButton("Export statistics to JSON");
        play.setBounds(0,0,350, 50);
        panel.add(play);
        stop.setBounds(0,50,350, 50);
        panel.add(stop);
        domGen.setBounds(0,100,350, 50);
        panel.add(domGen);
        export.setBounds(0,150,350, 50);
        panel.add(export);
        Frame frame=this;



        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                thread=new AnotherThread(frame);
                thread.start();

            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                thread.interrupt();
            }
        });
        domGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(Panel panel : panels)
                    panel.highlight();
            }
        });
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int i=0;
                for(Grassfield map : maps)
                {
                    try {
                        map.exportStats(quantity[i]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        });


    }

    public void stat ()
    {
        stats = new JLabel[maps.size()*8];
        int i=0;
        for(Grassfield map : maps)
        {
            stats[i*8]=new JLabel(quantity[i]+"Map statistics:");
            stats[1+i*8]=new JLabel("Animals: "+map.animals.size());
            stats[2+i*8]=new JLabel("Grasses: "+map.grasses.size());
            stats[3+i*8]=new JLabel("Average energy: "+(double)(Math.round(map.stats.averageEnergy*100))/100);
            stats[4+i*8]=new JLabel("Dominant gene: "+map.stats.getDominantGenome());
            stats[5+i*8]=new JLabel("Average age: "+(double)(Math.round(map.stats.averageAge*100))/100);
            stats[6+i*8]=new JLabel("Average dead age: "+(double)(Math.round(map.stats.averageDeadAge*100))/100);
            stats[7+i*8]=new JLabel("Average number of children: "+(double)(Math.round(map.stats.averageChildren*100))/100);
            i++;
        }
        for(int j=0; j<maps.size()*8; j++)
        {
            panel.add(stats[j]);
            stats[j].setBounds(0,200+j*20,350, 20);
        }
    }


}

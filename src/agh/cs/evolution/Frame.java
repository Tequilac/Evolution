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
import java.util.concurrent.TimeUnit;

public class Frame extends JFrame
{
    public Evolution evolution;
    public List <Grassfield> maps = new LinkedList<>();
    public List <Panel> panels = new LinkedList<>();
    public JLabel [] stats;
    public boolean go = false;

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
            }
        });


    }

    public void oneMap () throws IOException, ParseException {
        Grassfield map = this.evolution.getMap();
        this.maps.add(map);
        Panel panel = new Panel(map, getWidth()/2, getHeight());
        this.panels.add(panel);
        add(panel);

    }
    public void twoMaps () throws IOException, ParseException {
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
    }
    public void side () {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        //panel.setBounds(0,0,200,getHeight());
        panel.setPreferredSize(new Dimension(200, getHeight()));
        add(panel);
        JButton play = new JButton("Play");
        JButton stop = new JButton("Stop");
        JButton domGen = new JButton("Highlight dominant gene");
        JButton export = new JButton("Export statistics to JSON");
        play.setBounds(0,0,200, 50);
        panel.add(play);
        stop.setBounds(0,50,200, 50);
        panel.add(stop);
        domGen.setBounds(0,100,200, 50);
        panel.add(domGen);
        export.setBounds(0,150,200, 50);
        panel.add(export);

        //stats = new JLabel[maps.size()*6];


        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(Grassfield map : maps)
                    evolution.oneDay(map);
                for(Panel panel : panels)
                    panel.refresh();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        domGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(Grassfield map : maps) {
                    try {
                        map.exportStats();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }



}

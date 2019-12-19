package agh.cs.evolution;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;

public class Evolution
{
    private int aniNum;
    public Evolution (int aniNum)
    {
        this.aniNum=aniNum;
        Frame frame = new Frame(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    public Grassfield getMap () throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader("data.json"));
        JSONObject jo = (JSONObject) obj;
        long width = (long) jo.get("width");
        long height = (long) jo.get("height");
        long startEnergy = (long) jo.get("startEnergy");
        long moveEnergy = (long) jo.get("moveEnergy");
        long plantEnergy = (long) jo.get("plantEnergy");
        double jungleRatio = (double) jo.get("jungleRatio");
        Grassfield map = new Grassfield(width, height, jungleRatio, moveEnergy, plantEnergy, startEnergy, this.aniNum);
        return map;
    }



    public void oneDay (Grassfield map)
    {
            map.removeDead();
            map.run();
            map.searchForReproduceAndEating();
            map.placeGrass();
            map.stats.update(map.animals, map.grasses.size());
            map.day++;
    }


}

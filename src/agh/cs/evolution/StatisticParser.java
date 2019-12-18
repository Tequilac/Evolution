package agh.cs.evolution;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StatisticParser
{
    public void parse (int aniCount, int graCount, int domGene, double avgEne, double avgAge, double avgChild, String qua) throws FileNotFoundException {
        JSONObject jo = new JSONObject();
        jo.put("Total number of animals", aniCount);
        jo.put("Total number of grasses", graCount);
        jo.put("Dominant gene", domGene);
        jo.put("Average energy", avgEne);
        jo.put("Average age", avgAge);
        jo.put("Average number of children", avgChild);
        PrintWriter pw = new PrintWriter(qua+"MapStatistic.json");
        pw.write(jo.toJSONString());
        pw.flush();
        pw.close();
    }
}

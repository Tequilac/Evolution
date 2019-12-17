package agh.cs.evolution;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class World
{
    public static void main (String [] args) throws IOException, ParseException {
        Evolution evolution = new Evolution((Integer.parseInt(args[0])));
    }
}
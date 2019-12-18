package agh.cs.evolution;

import java.util.List;

public class AnotherThread extends Thread
{
    Frame frame;

    public AnotherThread (Frame frame)
    {
        this.frame=frame;
    }

    @Override public void run() {
        try {
            while (!Thread.currentThread().isInterrupted())
            {
                for (int j = 0; j < frame.maps.size() * 7; j++)
                    frame.panel.remove(frame.stats[j]);
                for (Grassfield map : frame.maps)
                    frame.evolution.oneDay(map);
                for (Panel panel : frame.panels)
                    panel.refresh();
                frame.stat();
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e) {}


    }
}

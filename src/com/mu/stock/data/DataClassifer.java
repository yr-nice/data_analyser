package com.mu.stock.data;

import java.io.File;

/**
 *
 * @author Peng Mu
 */
public class DataClassifer
{
    static public void classify()
    {
        File root = new File("historical_data");
        File pennyFolder = new File(root, "0.3");
        File lowFolder = new File(root, "1");
        File midFolder = new File(root, "5");
        File highFolder = new File(root, "higher");
        File removedFolder = new File(root, "removed");
        pennyFolder.mkdirs();
        lowFolder.mkdirs();
        midFolder.mkdirs();
        highFolder.mkdirs();
        removedFolder.mkdirs();
        
        for(File f : root.listFiles())
        {
            if(f.isDirectory())
                continue;
            Stock s = DataMgr.getStock(f);
            if(s == null ||s.getPriceHistory()==null || s.getPriceHistory().size()==0)
            {
                f.renameTo(new File(removedFolder, f.getName()));
            }
            else
            {
                float latestPrice = s.getPriceHistory().getLast().getAdjClose();
                if(latestPrice<0.3)
                    f.renameTo(new File(pennyFolder, f.getName()));
                else if(latestPrice<1)
                    f.renameTo(new File(lowFolder, f.getName()));
                else if(latestPrice<5)
                    f.renameTo(new File(midFolder, f.getName()));
                else
                    f.renameTo(new File(highFolder, f.getName()));
            }
        }
    }

    static public void main(String[] argu)
    {
        classify();
    }
}

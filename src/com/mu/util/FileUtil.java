package com.mu.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author Peng Mu
 */
public class FileUtil
{
    static public File locateFirstFile(File folder, String partialName)
    {
        File re = null;
        File[] fileList = folder.listFiles();
        for(File f : fileList)
        {
            if(f.isDirectory())
                re = locateFirstFile(f, partialName);
            else if(f.getName().toLowerCase().indexOf(partialName.toLowerCase())!=-1)
                return f;
            if(re != null)
                break;
        }
        return re;
    }



}

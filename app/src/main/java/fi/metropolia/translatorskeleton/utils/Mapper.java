package fi.metropolia.translatorskeleton.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bang on 30/04/16.
 */
public class Mapper {

    private static Map<String,String> wordToId;
    private static Mapper instance;

    private Mapper(){
        wordToId = new HashMap<String,String>();
    }

    public static Mapper getInstance(){
        if (instance == null)
            instance = new Mapper();
        return instance;
    }

    public static Map<String,String> getMap(){
        return wordToId;
    }

}

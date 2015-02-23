package org.silentsoft.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class MapUtil {
	
	public static List<?> sortByKey(final Map map){
        List<String> list = new ArrayList();
        list.addAll(new TreeSet<String>(map.keySet()));
        
        return list;
    }
	
    public static List<?> sortByValue(final Map map){
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
        
        Collections.sort(list,new Comparator(){
            
            public int compare(Object o1,Object o2){
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                
                return ((Comparable) v1).compareTo(v2);
            }
            
        });
        
        Collections.reverse(list);
        
        return list;
    }
}

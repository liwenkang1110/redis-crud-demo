package search;

import java.util.HashSet;
import java.util.Map;


public class SearchUtil {

    /**
     * 姓名 分割并存入索引到集合中
     */
    public Map<String, HashSet<String>> add(Map<String, HashSet<String>> catchMap,String userName, String userId) {
        String[] contextArr = userName.split(" ");//分割
        for (String str : contextArr) { //循环
            //判断Map 中是否有key为单字
            if (catchMap.containsKey(str) && str != "") {//存在
                catchMap.get(str).add(userId);
            } else {
                HashSet<String> set = new HashSet<>();
                set.add(userId);
                catchMap.put(str, set);
            }
        }
        return catchMap;
    }

    /**
     * 删除集合索引
     */
    public Map<String, HashSet<String>> delete(Map<String, HashSet<String>> catchMap,String userName, String userId) {
        String[] contextArr = userName.split(" ");//分割
        for (String str : contextArr) { //循环
            if (str != "") {
                catchMap.get(str).remove(userId);
            }
        }
        return catchMap;
    }

    //返回交集
    public HashSet<String> intersect(Map<String, HashSet<String>> catchMap,String content) {
        HashSet<String> set = new HashSet<String>();
        String[] contextArr = content.split(" ");
        set = catchMap.get(contextArr[0]);
        for (int i = 0; i <=contextArr.length-1; i++) {
            set.retainAll(catchMap.get(contextArr[i]));
        }
        return set;
    }


    //返回并集
    public HashSet<String> union(Map<String, HashSet<String>> catchMap,String content) {
        HashSet<String> set = new HashSet<String>();
        String[] contextArr = content.split(" ");
        set = catchMap.get(contextArr[0]);
        for (int i = 1; i <=contextArr.length-1; i++) {
            set.addAll(catchMap.get(contextArr[i]));
        }
        return set;
    }
}

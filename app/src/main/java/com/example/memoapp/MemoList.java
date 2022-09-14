package com.example.memoapp;


import java.util.ArrayList;
import java.util.List;

//class to make sure that there is only one memo list in all the application
public class MemoList {

    private final List<Memo> list;
    private static MemoList memoList;

    public static MemoList getInstance() {
        if(memoList == null)
            memoList = new MemoList();
        return memoList;
    }
    //constructor
    private MemoList() {
        this.list = new ArrayList<>();
    }

    public void addElement(Memo c) {
        if(list.isEmpty()) {
            list.add(c);
            return;
        }
        else{
            //order by date
            for(Memo e : list) {
                if(!Utils.isExpired(e.getDay(),c.getDay()))
                    continue;
                list.add(list.indexOf(e), c);
                return;
            }
            list.add(c);
        }

    }

    public void removeElement(int index) {
        list.remove(index);
    }

    //get memo with index position
    public Memo memoAtIndex(int index) {
        return list.get(index);
    }
    //get memo with string,date and hours
    public Memo getMemo(String name, String date, String hours) {
        for (int i = 0; i<list.size(); i++)
        {
            if (list.get(i).getTitle().equals(name) && list.get(i).getDay().equals(date)&&list.get(i).getHour().equals(hours))
                return list.get(i);
        };
        return null;
    }

    public int size() {
        return list.size();
    }
    //get an arrayList of active memos
    public ArrayList<Memo> getActiveMemos(){
        ArrayList<Memo> activeMemos = new ArrayList<Memo>();
        for (int i=0; i< memoList.size(); i++){
            if(memoAtIndex(i).getStatus().equals("active")){
                activeMemos.add(memoAtIndex(i));
            }
        }
        return activeMemos;
    }
}

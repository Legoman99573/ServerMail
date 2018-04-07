package com.tyrellplayz.servermail.menus;

public enum EnumSort {
    LATEST(0, "Latest messages"),
    OLD(1, "Old messages");

    private int sortNumber;
    private String sortName;

    EnumSort(int sortNumber, String sortName) {
        this.sortNumber = sortNumber;
        this.sortName = sortName;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public String getSortName() {
        return sortName;
    }

    @Override
    public String toString() {
        return sortName;
    }

    public static EnumSort getSort(int num){
        for(EnumSort sort:EnumSort.values()){
            if(sort.getSortNumber() == num){
                return sort;
            }
        }
        return null;
    }

    public static EnumSort getSort(String name){
        for(EnumSort sort:EnumSort.values()){
            if(sort.getSortName().equalsIgnoreCase(name)){
                return sort;
            }
        }
        return null;
    }

    public EnumSort nextSort(){
        EnumSort nextSort = EnumSort.getSort(sortNumber+1);
        if(nextSort == null){
            return EnumSort.LATEST;
        }
        return nextSort;
    }

}

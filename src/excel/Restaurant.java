package excel;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String name;
    private List<Item> menu = new ArrayList<>();

    public Restaurant(String newName){
        name = newName;
    }
    public void addItem(Item newItem){
        menu.add(newItem);
    }
    public List<Item> menu(){
        return menu;
    }
    public String name(){
        return name;
    }
}

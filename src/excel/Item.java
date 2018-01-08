package excel;

public class Item {
    private String name;
    private String description;
    private float price;
    private String location;

    public Item(String newName, String newDescription, float newPrice, String newLocation){
        name = newName;
        description = newDescription;
        price = newPrice;
        location = newLocation;
    }
    public String name(){
        return name;
    }
    public String description(){
        return description;
    }
    public float price(){
        return price;
    }
    public String location(){
    	return location;
    }
}

package uqac.dim.recipeplus;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private int id;
    private String name;
    private String desc;
    private float avgPrice;

    public Ingredient(int id, String name,String desc,float avgPrice){
        this.id = id;
        this.name = name;
        this.desc = desc;
        this. avgPrice = avgPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(float avgPrice) {
        this.avgPrice = avgPrice;
    }
}

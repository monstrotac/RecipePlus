package uqac.dim.recipeplus;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private int id;
    private String name;
    private String desc;
    private List<Ingredient> ingredients;

    public Recipe(int id,String name,String desc,List<Ingredient> ingredients){
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.ingredients = ingredients;
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}

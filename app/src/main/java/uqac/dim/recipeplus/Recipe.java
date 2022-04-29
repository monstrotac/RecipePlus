package uqac.dim.recipeplus;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private int id;
    private int creatorId;
    private String name;
    private String desc;
    private String instruc;
    private List<Ingredient> ingredients;

    public Recipe(int id,int creatorId,String name,String desc,String instruc,List<Ingredient> ingredients){
        this.id = id;
        this.creatorId = creatorId;
        this.name = name;
        this.desc = desc;
        this.instruc = instruc;
        this.ingredients = ingredients;
    }

    public Recipe(String name,String desc,String instruc,List<Ingredient> ingredients){
        this.name = name;
        this.desc = desc;
        this.instruc = instruc;
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

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getInstruc() {
        return instruc;
    }

    public void setInstruc(String instruc) {
        this.instruc = instruc;
    }

}

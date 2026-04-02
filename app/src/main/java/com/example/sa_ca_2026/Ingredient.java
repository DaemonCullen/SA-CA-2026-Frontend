package com.example.sa_ca_2026;

public class Ingredient {


    public int id;
    public String name;
    public double price;  // decimal in C# -> double in Java
    public String origin;
    public boolean isOrganic;
    public double fats;
    public double protein;
    public double carbohydrates;
    public double fiber;
    public boolean isVegetarian;  // Changed to boolean (matches C# bool intent; was String before)
    public double energyContent;
    public double servingSize;
    public double caloriesPerServing;
    public String nutrientGroups;
    public double sodiumContent;
    public double macronutrientComposition;
    public double dietaryFiberPercentage;
    public double calorieDensity;

}

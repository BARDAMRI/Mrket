package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemTest {


    @Test
    @DisplayName("Invalid item details - Invalid ID")
    public void InvalidItemID()
    {
        String name = "Milk";
        double price = 5.0;
        String info ="1.0L";
        List<String> keywords = new ArrayList<>();
        keywords.add("dairy");
        Item.Category category = Item.Category.general;
        try {
            Item testItem = new Item(-1,name,price,info,category,keywords);
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("Invalid item details - Invalid price ")
    public void InvalidItemDetails()
    {
        String name = "Milk";
        String info ="1.0L";
        List<String> keywords = new ArrayList<>();
        keywords.add("dairy");
        Item.Category category = Item.Category.general;
        try {
            Item testItem = new Item(2,name,-1,info,category,keywords);
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("Valid item details")
    public void ValidItemDetails()
    {
        String name = "Milk";
        double price = 5.0;
        String info ="";
        List<String> keywords = new ArrayList<>();
        Item.Category category = Item.Category.general;
        try {
            Item testItem = new Item(1,name , price,info,category,keywords);
            testItem = new Item(2,name,price,null,null,null);
            assert true;
        }
        catch (MarketException e){
            assert false;
        }
    }
    @Test
    @DisplayName("Valid item details - inserting null where legal")
    public void ValidItemWithNullLegalDetails()
    {
        String name = "Milk";
        double price = 5.0;
        try {
            Item testItem = new Item(2,name,price,null,null,null);
            assert true;
        }
        catch (MarketException e){
            assert false;
        }
    }

    @Test
    @DisplayName("Null input in category for item")
    public void NullCategory()
    {
        String name = "Milk";
        double price = 5.0;
        String info ="";
        List<String> keywords = new ArrayList<>();
        Item.Category category = Item.Category.general;
        try {
            Item testItem = new Item(1,name , price,info,null,keywords);
            Assertions.assertEquals(Item.Category.general.name(),testItem.getCategory().name());
            //assert true;
        }
        catch (MarketException e){
            assert false;
        }
    }
}

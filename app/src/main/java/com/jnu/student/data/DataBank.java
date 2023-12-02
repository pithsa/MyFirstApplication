package com.jnu.student.data;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBank {
    final String fileName = "shopItems.data";
    ArrayList<ShopItem> data = new ArrayList<>();
    public ArrayList<ShopItem> LoadShopItems(Context context) {
        try{
            FileInputStream fileIn = context.openFileInput(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<ShopItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            Log.d("Serialization","Data Loaded successfully"+data.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void SaveShopItems(Context context, ArrayList<ShopItem> shopItems){
        try{
            FileOutputStream fileOut = context.openFileOutput(fileName, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(shopItems);
            out.close();
            fileOut.close();
            Log.d("Serialization","Data saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

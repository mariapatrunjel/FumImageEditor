package com.editor.image.maria.funimageeditor.utils;


import java.util.ArrayList;
import java.util.List;

public final class ModifiersList {

    private static final ModifiersList INSTANCE = new ModifiersList();

    private ArrayList<Modifier> list;

    private ModifiersList() {
        list = new ArrayList<>() ;
    }

    public static ModifiersList getInstance() {
        return INSTANCE;
    }

   public void insert(Modifier modifier){
       list.add(list.size(),modifier);
   }

   public void delete(){
       list.remove(list.size()-1);
   }

   public ColorTransform getLastColorTransform(){
       ColorTransform colorTransform = new ColorTransform("Normal",0,0,0,1.0f);
       Boolean filter,red,green,blue,brightness;
       filter = red = green = blue = brightness = false;
       for(int i=list.size()-1;i>=0;i--)
       {
           if(list.get(i).getName().equals("Red") && !red) {
               colorTransform.setRedValue(Integer.parseInt(list.get(i).getValue()));
               red = true;
           }

           if(list.get(i).getName().equals("Green") && !green) {
               colorTransform.setGreenValue(Integer.parseInt(list.get(i).getValue()));
               green = true;
           }
           if(list.get(i).getName().equals("Blue") && !blue) {
               colorTransform.setBlueValue(Integer.parseInt(list.get(i).getValue()));
               blue = true;
           }
           if(list.get(i).getName().equals("Filter") && !filter) {
               colorTransform.setFilter(list.get(i).getValue());
               filter = true;
           }
           if(list.get(i).getName().equals("Brightness") && !brightness) {
               colorTransform.setBrightness(Float.parseFloat(list.get(i).getValue()));
               brightness = true;
           }
           if(filter && red && green && blue && brightness)
               break;
       }
       return colorTransform;
   }

   public int size(){
       return list.size();
   }

   public Boolean isEmpty(){
       if(list.size()==0)
           return true;
       return false;
   }

   public void reset(){
       list.clear();
   }
}


package it.polimi.ngsw.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DevCardParser {

    /**
     * Method that returns a DevDeck of DevCards parsed from the given json file.
     * @param path is the path of the json file.
     * @return a List of DevCards, also called a devDeck.
     * @throws FileNotFoundException if the path isn't correct.
     */
    public List<DevCard> parseDevDeck(String path) throws FileNotFoundException {

        List<DevCard> devDeck;

        GsonBuilder gsonBuilder = new GsonBuilder();
        Type DevDeckListType =  new TypeToken<List<DevCard>>(){}.getType();

        JsonDeserializer<List<DevCard>> deserializer = (json, Type1, context) -> {

            JsonArray jsonArray = json.getAsJsonArray();
            List<DevCard> devCardList = new ArrayList<>();

            for(int i=0; i<4; i++){

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                String string = jsonObject.get("devCardColour").getAsString();

                DevCardColour devCardColour = whatDevCardColour(string);

                List<Resource> costList = new ArrayList<>();

                for(JsonElement object : jsonObject.get("devCost").getAsJsonArray()){
                    whatResource(costList, object);
                }

                List<Resource> resourcesNeeded = new ArrayList<>();
                List<Resource> resourceToProduce = new ArrayList<>();

                for(JsonElement object : jsonObject.get("resourceToPay").getAsJsonArray()){
                    whatResource(resourcesNeeded, object);
                }

                for(JsonElement object : jsonObject.get("resourceToProduce").getAsJsonArray()){
                    whatResource(resourceToProduce, object);
                }

                ProductionPower productionPower = new ProductionPower(resourcesNeeded, resourceToProduce);

                DevCard devCard = new DevCard(
                        jsonObject.get("devLevel").getAsInt(),
                        devCardColour,
                        costList,
                        productionPower,
                        jsonObject.get("PV").getAsInt()
                );
                devCardList.add(devCard);
            }

            return devCardList;

        };

        gsonBuilder.registerTypeAdapter(DevDeckListType, deserializer);
        Gson customGson = gsonBuilder.create();
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(path));
        devDeck = customGson.fromJson(reader, DevDeckListType);
        return devDeck;

    }

    /**
     * Manage the resources neeeded
     * @param resourcesNeeded the resources
     * @param object the json object that describes the resources needed
     */
    private void whatResource(List<Resource> resourcesNeeded, JsonElement object) {
        if(object.getAsString().equals("MONEY")){
            resourcesNeeded.add(Resource.MONEY);
        }
        if(object.getAsString().equals("SHIELD")){
            resourcesNeeded.add(Resource.SHIELD);
        }
        if(object.getAsString().equals("SLAVE")){
            resourcesNeeded.add(Resource.SLAVE);
        }
        if(object.getAsString().equals("STONE")){
            resourcesNeeded.add(Resource.STONE);
        }
        if (object.getAsString().equals("FAITHPOINT")){
            resourcesNeeded.add(Resource.FAITHPOINT);
        }
    }

    /**
     * Manage the colour of the resources needed
     * @param string wich color by string
     * @return the enum colour
     */
    private DevCardColour whatDevCardColour(String string){
        if(string.equals("GREEN")){
            return DevCardColour.GREEN;
        }
        if(string.equals("BLUE")){
            return DevCardColour.BLUE;
        }
        if(string.equals("YELLOW")){
            return DevCardColour.YELLOW;
        }
        if(string.equals("PURPLE")){
            return DevCardColour.PURPLE;
        }
        return null;
    }

}

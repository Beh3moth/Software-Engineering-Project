package it.polimi.ngsw.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderCardParser {

    private List<LeaderCard> leaderCards = new ArrayList<>(4);
    private List<SpaceAbility> spaceLeaderCards = new ArrayList<>(4);
    private List<DiscountAbility> discountLeaderCards = new ArrayList<>(4);
    private List<WhiteMarbleAbility> whiteMarbleLeaderCards = new ArrayList<>(4);
    private List<ProductionPowerAbility> productionPowerLeaderCards = new ArrayList<>(4);


    public void initSpaceLeaderCards() throws FileNotFoundException {
        // Get Gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type SpaceAbilityListType =  new TypeToken<List<SpaceAbility>>(){}.getType();

        JsonDeserializer<List<SpaceAbility>> deserializer = (json, SpaceAbilityListType1, context) -> {

            JsonArray jsonArray = json.getAsJsonArray();
            List<SpaceAbility> spaceAbilities = new ArrayList<>();

            for(int i=0; i<4; i++){

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                List<Resource> list = new ArrayList<>();

                for(int j=0; j<4; j++){
                    if(jsonObject.get("leaderCardCost").getAsJsonArray().get(i).getAsString().equals("MONEY")){
                        list.add(Resource.MONEY);
                    }
                    if(jsonObject.get("leaderCardCost").getAsJsonArray().get(i).getAsString().equals("SHIELD")){
                        list.add(Resource.SHIELD);
                    }
                    if(jsonObject.get("leaderCardCost").getAsJsonArray().get(i).getAsString().equals("SLAVE")){
                        list.add(Resource.SLAVE);
                    }
                    if(jsonObject.get("leaderCardCost").getAsJsonArray().get(i).getAsString().equals("STONE")){
                        list.add(Resource.STONE);
                    }
                }

                Resource resource = Resource.EMPTY;

                if(jsonObject.get("resourceToIncrease").getAsString().equals("MONEY")){
                    resource = Resource.MONEY;
                }
                if(jsonObject.get("resourceToIncrease").getAsString().equals("SHIELD")){
                    resource = Resource.SHIELD;
                }
                if(jsonObject.get("resourceToIncrease").getAsString().equals("SLAVE")){
                    resource = Resource.SLAVE;
                }
                if(jsonObject.get("resourceToIncrease").getAsString().equals("STONE")){
                    resource = Resource.STONE;
                }

                SpaceAbility spaceAbility = new SpaceAbility(
                        new ConcreteLeaderCard(),
                        jsonObject.get("PV").getAsInt(),
                        list,
                        resource
                );
                spaceAbilities.add(spaceAbility);
            }

            return spaceAbilities;

        };
        gsonBuilder.registerTypeAdapter(SpaceAbilityListType, deserializer);
        Gson customGson = gsonBuilder.create();
        FileReader reader = new FileReader("src/main/java/it/polimi/ngsw/resources/SpaceAbility.json");
        spaceLeaderCards = customGson.fromJson(reader, SpaceAbilityListType);
    }

    public void initDiscountLeaderCards() throws FileNotFoundException {
        // Get Gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type DiscountAbilityListType =  new TypeToken<List<DiscountAbility>>(){}.getType();

        JsonDeserializer<List<DiscountAbility>> deserializer = (json, SpaceAbilityListType, context) -> {

            JsonArray jsonArray = json.getAsJsonArray();
            List<DiscountAbility> discountAbilities = new ArrayList<>();

            for(int i=0; i<4; i++){

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                List<DevCardColour> devCardColourList = new ArrayList<>();

                for(int j=0; j<2; j++){
                    searchLeaderCardCost(jsonObject, devCardColourList, j);
                }

                Resource resource = Resource.EMPTY;

                if(jsonObject.get("discountResource").getAsString().equals("MONEY")){
                    resource = Resource.MONEY;
                }
                if(jsonObject.get("discountResource").getAsString().equals("SLAVE")){
                    resource = Resource.SLAVE;
                }
                if(jsonObject.get("discountResource").getAsString().equals("STONE")){
                    resource = Resource.STONE;
                }
                if(jsonObject.get("discountResource").getAsString().equals("SHIELD")){
                    resource = Resource.SHIELD;
                }

                DiscountAbility discountAbility = new DiscountAbility(
                        new ConcreteLeaderCard(),
                        jsonObject.get("PV").getAsInt(),
                        devCardColourList,
                        resource
                );
                discountAbilities.add(discountAbility);
            }

            return discountAbilities;

        };
        gsonBuilder.registerTypeAdapter(DiscountAbilityListType, deserializer);
        Gson customGson = gsonBuilder.create();
        FileReader reader = new FileReader("src/main/java/it/polimi/ngsw/resources/DiscountAbility.json");
        discountLeaderCards = customGson.fromJson(reader, DiscountAbilityListType);
    }

    private void searchLeaderCardCost(JsonObject jsonObject, List<DevCardColour> devCardColourList, int j) {
        if(jsonObject.get("leaderCardCost").getAsJsonArray().get(j).getAsString().equals("YELLOW")){
            devCardColourList.add(DevCardColour.YELLOW);
        }
        if(jsonObject.get("leaderCardCost").getAsJsonArray().get(j).getAsString().equals("GREEN")){
            devCardColourList.add(DevCardColour.GREEN);
        }
        if(jsonObject.get("leaderCardCost").getAsJsonArray().get(j).getAsString().equals("PURPLE")){
            devCardColourList.add(DevCardColour.PURPLE);
        }
        if(jsonObject.get("leaderCardCost").getAsJsonArray().get(j).getAsString().equals("BLUE")){
            devCardColourList.add(DevCardColour.BLUE);
        }
    }

    public void initWhiteMarbleLeaderCards() throws FileNotFoundException {
        // Get Gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type WhiteMarbleAbilityListType =  new TypeToken<List<WhiteMarbleAbility>>(){}.getType();

        JsonDeserializer<List<WhiteMarbleAbility>> deserializer = (json, SpaceAbilityListType, context) -> {

            JsonArray jsonArray = json.getAsJsonArray();
            List<WhiteMarbleAbility> whiteMarbleAbilities = new ArrayList<>();

            for(int i=0; i<4; i++){

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                List<DevCardColour> devCardColourList = new ArrayList<>();

                for(int j=0; j<3; j++){
                    searchLeaderCardCost(jsonObject, devCardColourList, j);
                }

                Resource resource = Resource.EMPTY;

                if(jsonObject.get("resourceToObtain").getAsString().equals("MONEY")){
                    resource = Resource.MONEY;
                }
                if(jsonObject.get("resourceToObtain").getAsString().equals("SLAVE")){
                    resource = Resource.SLAVE;
                }
                if(jsonObject.get("resourceToObtain").getAsString().equals("STONE")){
                    resource = Resource.STONE;
                }
                if(jsonObject.get("resourceToObtain").getAsString().equals("SHIELD")){
                    resource = Resource.SHIELD;
                }

                WhiteMarbleAbility whiteMarbleAbility = new WhiteMarbleAbility(
                        new ConcreteLeaderCard(),
                        jsonObject.get("PV").getAsInt(),
                        devCardColourList,
                        resource
                );
                whiteMarbleAbilities.add(whiteMarbleAbility);
            }

            return whiteMarbleAbilities;

        };
        gsonBuilder.registerTypeAdapter(WhiteMarbleAbilityListType, deserializer);
        Gson customGson = gsonBuilder.create();
        FileReader reader = new FileReader("src/main/java/it/polimi/ngsw/resources/WhiteMarbleAbility.json");
        whiteMarbleLeaderCards = customGson.fromJson(reader, WhiteMarbleAbilityListType);
    }

    public void initProductionPowerLeaderCards() throws FileNotFoundException {
        // Get Gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type ProductionPowerAbilityType =  new TypeToken<List<ProductionPowerAbility>>(){}.getType();

        JsonDeserializer<List<ProductionPowerAbility>> deserializer = (json, SpaceAbilityListType, context) -> {

            JsonArray jsonArray = json.getAsJsonArray();
            List<ProductionPowerAbility> productionPowerAbilities = new ArrayList<>();

            for(int i=0; i<4; i++){

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                DevCardColour devCardColour = DevCardColour.EMPTY;

                if(jsonObject.get("leaderCardCost").getAsString().equals("YELLOW")){
                    devCardColour = DevCardColour.YELLOW;
                }
                if(jsonObject.get("leaderCardCost").getAsString().equals("GREEN")){
                    devCardColour = DevCardColour.GREEN;
                }
                if(jsonObject.get("leaderCardCost").getAsString().equals("PURPLE")){
                    devCardColour = DevCardColour.PURPLE;
                }
                if(jsonObject.get("leaderCardCost").getAsString().equals("BLUE")){
                    devCardColour = DevCardColour.BLUE;
                }

                Resource resource = Resource.EMPTY;

                if(jsonObject.get("inputResource").getAsString().equals("MONEY")){
                    resource = Resource.MONEY;
                }
                if(jsonObject.get("inputResource").getAsString().equals("SLAVE")){
                    resource = Resource.SLAVE;
                }
                if(jsonObject.get("inputResource").getAsString().equals("STONE")){
                    resource = Resource.STONE;
                }
                if(jsonObject.get("inputResource").getAsString().equals("SHIELD")){
                    resource = Resource.SHIELD;
                }

                ProductionPowerAbility productionAbility = new ProductionPowerAbility(
                        new ConcreteLeaderCard(),
                        jsonObject.get("PV").getAsInt(),
                        devCardColour,
                        resource
                );
                productionPowerAbilities.add(productionAbility);
            }

            return productionPowerAbilities;

        };
        gsonBuilder.registerTypeAdapter(ProductionPowerAbilityType, deserializer);
        Gson customGson = gsonBuilder.create();
        FileReader reader = new FileReader("src/main/java/it/polimi/ngsw/resources/ProductionPowerAbility.json");
        productionPowerLeaderCards = customGson.fromJson(reader, ProductionPowerAbilityType);
    }


    public List<LeaderCard> initLeaderCards() throws FileNotFoundException {
        initProductionPowerLeaderCards();
        initWhiteMarbleLeaderCards();
        initDiscountLeaderCards();
        initSpaceLeaderCards();
        leaderCards.addAll(spaceLeaderCards);
        leaderCards.addAll(discountLeaderCards);
        leaderCards.addAll(whiteMarbleLeaderCards);
        leaderCards.addAll(productionPowerLeaderCards);
        Collections.shuffle(leaderCards);
        return leaderCards;
    }

    public List<SpaceAbility> returnSpaceAbilityList(){
        return spaceLeaderCards;
    }

    public List<DiscountAbility> returnDiscountAbilityList(){
        return discountLeaderCards;
    }

    public List<WhiteMarbleAbility> returnWhiteMarbleAbilityList(){
        return whiteMarbleLeaderCards;
    }

    public List<ProductionPowerAbility> returnProductionPowerList(){
        return productionPowerLeaderCards;
    }

}

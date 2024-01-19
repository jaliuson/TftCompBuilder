package com.example.RiotApiHelper;

import java.net.*;
import java.io.*;
import java.util.Collection;
import java.util.*;
public class RiotApiHelper {
    final private String API_KEY = "RGAPI-30aeefd8-6fc9-406b-84da-f7b40a8a37fb";
    final private String REGION = "americas.api.riotgames.com"; //URL prefix for NA region
    public RiotApiHelper() {

    }
    public String getPuuid (String summonerNameIngress) {
        try{
            String[] riotAccount = summonerNameIngress.split("#", 2);
            String gameName = riotAccount[0];
            String tagLine = riotAccount[1];
            URL url = new URL("https://" + REGION +"/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine +"?api_key=" + API_KEY);
            HashMap<String, String> responseMap = sendGetRequest(url);
            return((String)responseMap.get("puuid"));
        }
        catch (Exception e){
            System.out.println("Error: Couldn't fetch account info" + e);
        }
        return("error");
    }

    public Collection getMatchHistory (String puuidIngress) {
        ArrayList<String> output = new ArrayList<String>();
        try{
            URL matchHistoryURL = new URL("https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/" + puuidIngress + "/ids?start=0&count=20&api_key=" + API_KEY);
            output = sendGetRequestArray(matchHistoryURL);
        }
        catch(Exception e){
            System.out.println("Error: Getting match history of puuid: " + puuidIngress);
        }
        String[] arr = new String[1];
        return (output);
    }

    /*
        Sends HTTP GET request to the input url and returns a hashmap of the response object
     */
    private HashMap<String, String> sendGetRequest(URL url){
        HashMap<String, String> responseMap = new HashMap<String, String>();
        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            System.out.println("Response Code : " + (int)conn.getResponseCode());

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            responseMap = parseResponse(response.toString());

        }
        catch (Exception e){
            System.out.println("Error: Problem sending or recieving HTTP request from Riot API");
            System.out.println(e);
        }
        return responseMap;
    }

    /*
        Sends HTTP GET request to the input url and returns an array of strings of the response object
    */
    private ArrayList<String> sendGetRequestArray(URL url){
        ArrayList<String> output = new ArrayList<String>();
        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            System.out.println("Response Code : " + (int)conn.getResponseCode());
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            String removedBrackets = response.toString().substring(1, response.toString().length()-1);
            for(String element : removedBrackets.split(",")){
                output.add(element.substring(1, element.length()-1));
            }
        }
        catch (Exception e){
            System.out.println("Error: Problem sending or recieving HTTP request from Riot API");
            System.out.println(e);
        }
        return output;
    }

    private HashMap<String,String>parseResponse(String httpResponse){
        httpResponse = httpResponse.substring(1, httpResponse.length()-1);
        HashMap<String, String> output = new HashMap<String, String>();
        String[] keyValuePairs = httpResponse.split(",");
        try{
            for(String element: keyValuePairs){
                if(element.indexOf("\":\"") > 0){
                    output.put(element.substring(1, element.indexOf("\":\"")), element.substring(element.indexOf("\":\"")+3, element.length()-1));
                }
                else if(element.indexOf("\":") > 0)
                    output.put(element.substring(1, element.indexOf("\":")), element.substring(element.indexOf("\":")+2, element.length()));
            }
        }
        catch(Exception e) {
            System.out.println("Error: Parsing Response " + e);
        }
        for(String  key : output.keySet()) {
            System.out.println(key + " : " + output.get(key));
        }
        return output;
    }
}



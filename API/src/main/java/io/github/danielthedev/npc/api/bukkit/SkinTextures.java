package io.github.danielthedev.npc.api.bukkit;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SkinTextures {

    private static final String TEXTURE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private static final String UUID_URL = "https://api.mojang.com/profiles/minecraft";

    private final String texture;
    private final String signature;

    public SkinTextures(String textures, String signature) {
        this.texture = textures;
        this.signature = signature;
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }

    public static void getByUsername(Plugin plugin, String username, BiConsumer<Boolean, SkinTextures> callback) {
        new BukkitRunnable(){
            @Override
            public void run() {
                JSONArray array = new JSONArray();
                array.add(username);
                UUID result = null;

                try {
                    HttpRequest request = HttpRequest.newBuilder(new URI(UUID_URL))
                            .setHeader("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(array.toString()))
                            .timeout(Duration.ofSeconds(5))
                            .build();

                    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                    if(response.statusCode() == HttpURLConnection.HTTP_OK) {
                        JSONArray uuidArray = (JSONArray) new JSONParser().parse(response.body());
                        if(uuidArray.size() > 0) {
                            String uuidStr = (String) ((JSONObject) uuidArray.get(0)).get("id");
                            result = UUID.fromString(uuidStr.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
                        }
                    }

                } catch (URISyntaxException | InterruptedException | IOException | ParseException e) {
                    e.printStackTrace();
                }

                if(result == null) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            callback.accept(false, null);
                        }
                    }.runTask(plugin);
                } else {
                    SkinTextures.getByUUID(plugin, result, callback);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public static void getByUUID(Plugin plugin, UUID uuid, BiConsumer<Boolean, SkinTextures> callback) {
        new BukkitRunnable(){
            @Override
            public void run() {
                SkinTextures result = null;

                try {
                    HttpRequest request = HttpRequest.newBuilder(new URI(String.format(TEXTURE_URL, uuid.toString().replace("-", ""))))
                            .timeout(Duration.ofSeconds(5))
                            .GET()
                            .build();

                    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                    if(response.statusCode() == HttpURLConnection.HTTP_OK) {
                        JSONArray properties = (JSONArray) ((JSONObject) new JSONParser().parse(response.body())).get("properties");
                        for(int t = 0; t < properties.size(); t++) {
                            JSONObject obj = (JSONObject) properties.get(t);
                            if(obj.containsKey("name") && obj.get("name").equals("textures")) {
                                result = new SkinTextures((String)obj.get("value"), (String)obj.get("signature"));
                            }
                        }
                    }

                } catch (URISyntaxException | InterruptedException | IOException | ParseException e) {
                    e.printStackTrace();
                }

                final SkinTextures skinTextures = result;

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(skinTextures == null) {
                            callback.accept(false, null);
                        } else {
                            callback.accept(true, skinTextures);
                        }
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }

    public static CompletableFuture<SkinTextures> getByUsername(String username) {
        return CompletableFuture.supplyAsync(()->{
            JSONArray array = new JSONArray();
            array.add(username);
            UUID result = null;
            try {
                HttpRequest request = HttpRequest.newBuilder(new URI(UUID_URL))
                        .setHeader("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(array.toString()))
                        .timeout(Duration.ofSeconds(5))
                        .build();

                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                    JSONArray uuidArray = (JSONArray) new JSONParser().parse(response.body());
                    if (uuidArray.size() > 0) {
                        String uuidStr = (String) ((JSONObject) uuidArray.get(0)).get("id");
                        result = UUID.fromString(uuidStr.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
                    }
                }
            } catch (URISyntaxException | InterruptedException | IOException | ParseException e) {
                e.printStackTrace();
            }
            if(result == null)
                throw new IllegalArgumentException("SkinTextures not found by username");
            else {
                return getByUUID(result).join();
            }
        });
    }

    public static CompletableFuture<SkinTextures> getByUUID(UUID uuid) {
        return CompletableFuture.supplyAsync(()->{
            SkinTextures result = null;
            try {
                HttpRequest request = HttpRequest.newBuilder(new URI(String.format(TEXTURE_URL, uuid.toString().replace("-", ""))))
                        .timeout(Duration.ofSeconds(5))
                        .GET().build();

                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                    JSONArray properties = (JSONArray) ((JSONObject) new JSONParser().parse(response.body())).get("properties");
                    for (int t = 0; t < properties.size(); t++) {
                        JSONObject obj = (JSONObject) properties.get(t);
                        if (obj.containsKey("name") && obj.get("name").equals("textures")) {
                            result = new SkinTextures((String) obj.get("value"), (String) obj.get("signature"));
                        }
                    }
                }
            } catch (URISyntaxException | InterruptedException | IOException | ParseException e) {
                e.printStackTrace();
            }
            if(result==null)
                throw new IllegalArgumentException("SkinTextures not found by uuid");
            else
                return result;
        });
    }
}

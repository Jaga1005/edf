package com.edf;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResourceMap implements Serializable {
    private Map<String, String> resources;

    public ResourceMap() {
        resources = new HashMap<>();
    }

    public ResourceMap(Map<String, String> val) {
        resources = val;
    }

    public void put(String key, String val) {
        resources.put(key, val);
    }

    public String getFilePath(String key) {
        return resources.get(key);
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }

    public static ResourceMap loadFromFile(String resourceFile) throws IOException {
        File file = new File(resourceFile);
        ResourceMap resourceMap = null;

        if (file.exists()) {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(resourceFile));
            resourceMap = gson.fromJson(reader, ResourceMap.class);
        }

        return resourceMap == null ? new ResourceMap() : resourceMap;
    }

    public void saveToFile(String resourceFile) throws FileNotFoundException {
        Gson gson = new Gson();
        PrintWriter pw = new PrintWriter((resourceFile));
        pw.write(gson.toJson(this));
        pw.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceMap that = (ResourceMap) o;
        return resources.equals(that.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resources);
    }

    @Override
    public String toString() {
        return "ResourceMap{" +
                "resources=" + resources +
                '}';
    }
}

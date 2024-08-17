package com.example.integration.sandbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonIdChecker {

    public static List<String> findMissingIds(List<String> ids, String jsonFilePath) {
        List<String> notFoundIds = new ArrayList<>();
        Set<String> idSet = new HashSet<>(ids);

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            //System.out.println(jsonContent);
            for (String id : ids) {
                if (!jsonContent.contains(id)) {
                    notFoundIds.add(id);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return notFoundIds;
    }

    public static void main(String[] args) {

        String idsString = "3967652661, 3949443993, 3968522216, 3985944850, 3985949035, 3985948161, 3968157351, 3965447727, 3967659436, 3967687573, 3927802963, 3967904091, 3945503921, 3968567614, 3967675681, 3968561359, 3968138737, 3967898197, 3949713465, 3965427750, 3983531737, 3965445949";

        String jsonFilePath = "/Users/exampleqi/Documents/GitHub/integration/src/main/resources/response/search-job-java-24h-stockholm2.json";

        List<String> idsList = Arrays.asList(idsString.trim().split("\\s*,\\s*"));

        List<String> notFoundIds = findMissingIds(idsList, jsonFilePath);

        System.out.println("IDs not found in JSON:");
        notFoundIds.forEach(System.out::println);
    }
}

package com.lampirg.recommendator.anidb.specific.anilist.json;

public record GetUserListAndRecommendations(Data data) {

    public record Data(Completed completed, Other other) {}

    public static String variables = """
            {
            	"name": "%s"
            }
            """;
}


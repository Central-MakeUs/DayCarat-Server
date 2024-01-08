package com.example.daycarat.domain.episode.dto;

import java.util.List;

public record PostEpisode (String title, String date, String episodeType, String participationRole, List<String> activityTags) {

}
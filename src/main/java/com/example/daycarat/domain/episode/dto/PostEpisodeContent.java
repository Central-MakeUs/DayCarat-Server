package com.example.daycarat.domain.episode.dto;

import com.example.daycarat.domain.episode.entity.EpisodeContentType;

public record PostEpisodeContent (EpisodeContentType episodeContentType, String content){
}

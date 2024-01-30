package com.example.daycarat.domain.announcement.service;

import com.example.daycarat.domain.announcement.dto.GetAnnouncement;
import com.example.daycarat.domain.announcement.dto.PostAnnouncement;
import com.example.daycarat.dynamodb.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public Boolean createAnnouncement(PostAnnouncement postAnnouncement) {

        announcementRepository.save(postAnnouncement.toEntity());
        return true;

    }

    public List<GetAnnouncement> getAnnouncements() {
        return GetAnnouncement.listOf(announcementRepository.findAll());
    }

    public Boolean deleteAnnouncement(String announcementId) {
        announcementRepository.deleteById(announcementId);
        return true;
    }
}

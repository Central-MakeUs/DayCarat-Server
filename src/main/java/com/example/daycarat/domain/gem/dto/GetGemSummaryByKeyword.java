package com.example.daycarat.domain.gem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class GetGemSummaryByKeyword {
    private Long communication;
    private Long conflictResolution;
    private Long passion;
    private Long diligence;
    private Long collaboration;
    private Long leadership;
    private Long feedback;
    private Long unset;

    public void handleNull() {
        if (this.communication == null) {
            this.communication = 0L;
        }
        if (this.conflictResolution == null) {
            this.conflictResolution = 0L;
        }
        if (this.passion == null) {
            this.passion = 0L;
        }
        if (this.diligence == null) {
            this.diligence = 0L;
        }
        if (this.collaboration == null) {
            this.collaboration = 0L;
        }
        if (this.leadership == null) {
            this.leadership = 0L;
        }
        if (this.feedback == null) {
            this.feedback = 0L;
        }
        if (this.unset == null) {
            this.unset = 0L;
        }
    }
}

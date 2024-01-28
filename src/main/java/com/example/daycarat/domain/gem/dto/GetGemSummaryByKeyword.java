package com.example.daycarat.domain.gem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class GetGemSummaryByKeyword {
    private Long communication;
    private Long problemSolving;
    private Long creativity;
    private Long challengeSpirit;
    private Long proficiency;
    private Long execution;
    private Long unset;

    public void handleNull() {
        if (this.communication == null) {
            this.communication = 0L;
        }
        if (this.problemSolving == null) {
            this.problemSolving = 0L;
        }
        if (this.creativity == null) {
            this.creativity = 0L;
        }
        if (this.challengeSpirit == null) {
            this.challengeSpirit = 0L;
        }
        if (this.proficiency == null) {
            this.proficiency = 0L;
        }
        if (this.execution == null) {
            this.execution = 0L;
        }
        if (this.unset == null) {
            this.unset = 0L;
        }
    }
}

package com.transaction.models;

import com.transaction.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MatchScore {
    MatchType matchType;
    int matchScore;

    public MatchScore() {
        this.matchScore = 0;
        this.matchType = null;
    }
}

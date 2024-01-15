package com.example.daycarat.domain.gem.validator;

import com.example.daycarat.domain.gem.entity.Gem;
import com.example.daycarat.global.error.exception.CustomException;
import com.example.daycarat.global.error.exception.ErrorCode;

public class GemValidator {

    public static void checkIfGemExists(Gem gem) {
        if (gem == null || gem.getIsDeleted()) {
            throw new CustomException(ErrorCode.GEM_NOT_FOUND);
        }
    }

}

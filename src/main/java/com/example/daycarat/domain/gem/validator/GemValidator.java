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

    public static void validateContents(Gem gem) {
        if (gem.getContent1() == null || gem.getContent2() == null || gem.getContent3() == null || gem.getContent4() == null || gem.getContent5() == null) {
            throw new CustomException(ErrorCode.GEM_CONTENTS_NOT_FILLED);
        }
    }
}

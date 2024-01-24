package com.example.daycarat.global.util;

import com.example.daycarat.domain.gem.dto.GetEpisodeClipboardDto;

public class StringParser {

    public static String getSubString(String str) {
        int length = 50;

        if (str.length() > length) {
            return str.substring(0, length) + "...";
        }
        return str;
    }

    public static String getClipboard(GetEpisodeClipboardDto getEpisodeClipboardDto) {
        String clipboard = "";

        clipboard += "상황 (Situation)\n";
        clipboard += "어떤 상황 또는 어떤 문제였나요?\n";
        clipboard += getEpisodeClipboardDto.content1() + "\n\n";

        clipboard += "목표 (Objective)\n";
        clipboard += "당시 무엇을 성취하는 것이 목표였나요?\n";
        clipboard += getEpisodeClipboardDto.content2() + "\n\n";

        clipboard += "행동 (Action)\n";
        clipboard += "목표를 위해 구체적으로 어떤 행동을 하였나요?\n";
        clipboard += getEpisodeClipboardDto.content3() + "\n\n";

        clipboard += "결과 (Result)\n";
        clipboard += "그로 인해 어떤 결과가 발생했나요?\n";
        clipboard += getEpisodeClipboardDto.content4() + "\n\n";

        clipboard += "영향 (Aftermath)\n";
        clipboard += "그 결과가 내게 미친 영향은 무엇인가요?\n";
        clipboard += getEpisodeClipboardDto.content5() + "\n\n";

        clipboard += "AI 추천 문장\n";
        clipboard += getEpisodeClipboardDto.generatedContent1() + "\n\n";
        clipboard += getEpisodeClipboardDto.generatedContent2() + "\n\n";
        clipboard += getEpisodeClipboardDto.generatedContent3() + "\n\n";

        return clipboard;
    }
}

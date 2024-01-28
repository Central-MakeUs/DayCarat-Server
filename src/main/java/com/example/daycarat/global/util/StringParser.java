package com.example.daycarat.global.util;

import com.example.daycarat.domain.gem.dto.GetEpisodeClipboardDto;

import java.util.ArrayList;
import java.util.List;

public class StringParser {

    // 한글 초성, 중성, 종성 분리
    // 한글 유니코드에서 초성의 시작
    private static final int INITIAL_SOUND_UNICODE = 0xAC00;

    // 각각 초성, 중성, 종성의 개수
    private static final int NUM_INITIAL_CONSONANTS = 19;
    private static final int NUM_MEDIAL_VOWELS = 21;
    private static final int NUM_FINAL_CONSONANTS = 28;

    // 초성, 중성, 종성 배열
    private static final char[] INITIAL_SOUNDS = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final char[] MEDIAL_VOWELS = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };
    private static final char[] FINAL_SOUNDS = {
            '\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public static String decompose(String str) {
        StringBuilder decomposedStr = new StringBuilder();

        for (char ch : str.toCharArray()) {
            if (ch >= INITIAL_SOUND_UNICODE && ch <= INITIAL_SOUND_UNICODE + (NUM_INITIAL_CONSONANTS * NUM_MEDIAL_VOWELS * NUM_FINAL_CONSONANTS - 1)) {
                int unicode = ch - INITIAL_SOUND_UNICODE;
                int finalConsonantIndex = unicode % NUM_FINAL_CONSONANTS;
                int medialVowelIndex = (unicode / NUM_FINAL_CONSONANTS) % NUM_MEDIAL_VOWELS;
                int initialConsonantIndex = (unicode / (NUM_MEDIAL_VOWELS * NUM_FINAL_CONSONANTS));

                decomposedStr.append(INITIAL_SOUNDS[initialConsonantIndex]);
                decomposedStr.append(MEDIAL_VOWELS[medialVowelIndex]);
                if (finalConsonantIndex != 0) { // 종성이 있으면 추가
                    decomposedStr.append(FINAL_SOUNDS[finalConsonantIndex]);
                }
            } else { // 한글이 아니면 그대로 추가
                decomposedStr.append(ch);
            }
        }

        return decomposedStr.toString();
    }

    public static List<String> decomposeToList(String str) {
        List<String> result = new ArrayList<>();
        StringBuilder decomposedStr = new StringBuilder();

        for (char ch : str.toCharArray()) {
            if (ch >= INITIAL_SOUND_UNICODE && ch <= INITIAL_SOUND_UNICODE + (NUM_INITIAL_CONSONANTS * NUM_MEDIAL_VOWELS * NUM_FINAL_CONSONANTS - 1)) {
                int unicode = ch - INITIAL_SOUND_UNICODE;
                int finalConsonantIndex = unicode % NUM_FINAL_CONSONANTS;
                int medialVowelIndex = (unicode / NUM_FINAL_CONSONANTS) % NUM_MEDIAL_VOWELS;
                int initialConsonantIndex = (unicode / (NUM_MEDIAL_VOWELS * NUM_FINAL_CONSONANTS));

                decomposedStr.append(INITIAL_SOUNDS[initialConsonantIndex]);
                result.add(decomposedStr.toString());
                decomposedStr.append(MEDIAL_VOWELS[medialVowelIndex]);
                result.add(decomposedStr.toString());

                if (finalConsonantIndex != 0) { // 종성이 있으면 추가
                    decomposedStr.append(FINAL_SOUNDS[finalConsonantIndex]);
                    result.add(decomposedStr.toString());
                }
            } else { // 한글이 아니면 그대로 추가
                decomposedStr.append(ch);
                result.add(decomposedStr.toString());
            }
        }

        return result;
    }

    // 초성을 얻는 메서드
    private static char getCho(int index) {
        final char[] CHO = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
        return CHO[index];
    }

    // 중성을 얻는 메서드
    private static char getJung(int index) {
        final char[] JUNG = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};
        return JUNG[index];
    }

    // 종성을 얻는 메서드
    private static char getJong(int index) {
        final char[] JONG = {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
        return JONG[index];
    }

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

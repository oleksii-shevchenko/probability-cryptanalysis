package dev.flanker.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Alphabet {
    private static final List<Character> CYRILLIC = Arrays.asList(
            'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к',
            'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х',
            'ц', 'ч', 'ш', 'щ', 'ы', 'ь', 'э', 'ю', 'я'
    );

    private static final List<Character> ENGLISH = Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z'
    );

    public static final Alphabet ENGLISH_ALPHABET = new Alphabet(ENGLISH);
    public static final Alphabet CYRILLIC_ALPHABET = new Alphabet(CYRILLIC);

    private final Map<Character, Integer> encoding;
    private final Map<Integer, Character> decoding;

    public Alphabet(List<Character> letters) {
        Map<Character, Integer> mapping = directMapping(letters);
        this.encoding = new HashMap<>(mapping);
        this.decoding = inverseMapping(mapping);
    }

    public int code(char c) {
        return encoding.get(c);
    }

    public char symbol(int code) {
        return decoding.get(code);
    }

    public int size() {
        return encoding.size();
    }

    public Collection<Character> letters() {
        return encoding.keySet();
    }

    private static Map<Integer, Character> inverseMapping(Map<Character, Integer> mapping) {
        return mapping.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private static Map<Character, Integer> directMapping(List<Character> letters) {
        Map<Character, Integer> mapping = new HashMap<>();
        for (int i = 0; i < letters.size(); i++) {
            mapping.put(letters.get(i), i);
        }
        return mapping;
    }

    @Override
    public String toString() {
        return "Alphabet{" +
                "encoding=" + encoding +
                '}';
    }
}

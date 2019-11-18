package dev.flanker.criteria;

public class ForbiddenNgramCriteria implements TextCriteria {
    @Override
    public boolean isRandom(String text) {
        return false;
    }
}

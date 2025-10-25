package dev.proststuff.utilitary.utility.data;

public interface ILanguageGeneratable {
    default String getTranslationKey() {return "";}
    default String getTranslation(String lang) {return "";}
}

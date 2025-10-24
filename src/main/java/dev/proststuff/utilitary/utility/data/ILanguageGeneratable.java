package dev.proststuff.utilitary.utility.data;

public interface ILanguageGeneratable {
    String getTranslationKey();
    String getTranslation(String lang);
}

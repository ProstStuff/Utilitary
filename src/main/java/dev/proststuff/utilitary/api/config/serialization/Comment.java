package dev.proststuff.utilitary.api.config.serialization;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record Comment(@NonNull Component text, @NonNull Map<String, Comment> comments) {
    public static final Comment EMPTY = new Comment(Component.empty(), ImmutableMap.of());

    public static Comment of(Component text, Map<String, Comment> comments) {
        return new Comment(text, comments);
    }

    public static Comment of(String text, Map<String, Comment> comments) {
        return of(Component.literal(text), comments);
    }

    public static Comment of(Component text) {
        return new Comment(text, new HashMap<>());
    }

    public static Comment of(String text) {
        return of(Component.literal(text));
    }

    public static Comment of(Map<String, Comment> comments) {
        return new Comment(Component.empty(), comments);
    }

    public @Nullable String getNonEmptyString() {
        String result = text.getString();
        return result.isEmpty() ? null : result;
    }

    public Comment addComment(String key, Comment comment) {
        comments.put(key, comment);
        return this;
    }

    public Comment addComment(String key, Component comment) {
        return addComment(key, Comment.of(comment));
    }

    public Comment addComment(String key, String comment) {
        return addComment(key, Comment.of(comment));
    }

    public @NonNull Comment get(String key) {
        return comments.getOrDefault(key, EMPTY);
    }

    @Override
    public @NonNull String toString() {
        String nonEmptyString = getNonEmptyString();
        return (nonEmptyString != null ? nonEmptyString + " : " : "") + comments;
    }
}
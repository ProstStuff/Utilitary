package dev.proststuff.utilitary.api.utility;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record SimpleIdentifier(String namespace, String path) implements Comparable<SimpleIdentifier> {
    public static SimpleIdentifier of(String namespace, String path) {
        return new SimpleIdentifier(namespace, path);
    }

    public static SimpleIdentifier ofVanilla(String path) {
        return new SimpleIdentifier(Identifier.DEFAULT_NAMESPACE, path);
    }

    public static SimpleIdentifier fromIdentifier(Identifier identifier) {
        return new SimpleIdentifier(identifier.getNamespace(), identifier.getPath());
    }

    public static SimpleIdentifier parse(String identifier) throws IllegalArgumentException {
        int i = identifier.indexOf(':');
        if (i == -1) {
            throw new IllegalArgumentException("Invalid identifier '" + identifier + "', no ':' to separate between namespace and path.");
        }

        return new SimpleIdentifier(identifier.substring(0, i), identifier.substring(i + 1));
    }

    public static SimpleIdentifier tryParse(String identifier) {
        try {
            return parse(identifier);
        } catch (IllegalArgumentException _) {}

        return null;
    }

    public String toLanguageKey(String prefix, String infix, String suffix) {
        return prefix + "." + namespace + "." + infix + path + "." + suffix;
    }

    public String toLanguageKeyPrefix(String prefix) {
        return prefix + "." + namespace + "." + path;
    }

    public String toLanguageKeyInfix(String infix) {
        return namespace + "." + infix + "." + path;
    }

    public String toLanguageKeySuffix(String suffix) {
        return namespace + "." + path + "." + suffix;
    }

    public String toLanguageKey() {
        return namespace + "." + path;
    }

    public boolean isValidIdentifier() {
        return Identifier.isValidNamespace(namespace) && Identifier.isValidPath(path);
    }

    public Optional<Identifier> toIdentifier() {
        if (!isValidIdentifier()) {
            return Optional.empty();
        }

        return Optional.of(Identifier.fromNamespaceAndPath(namespace, path));
    }

    @Override
    public @NonNull String toString() {
        return namespace + ":" + path;
    }

    @Override
    public int compareTo(@NonNull SimpleIdentifier o) {
        int result = this.path.compareTo(o.path);
        if (result == 0) {
            result = this.namespace.compareTo(o.namespace);
        }

        return result;
    }
}
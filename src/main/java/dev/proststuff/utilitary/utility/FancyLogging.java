package dev.proststuff.utilitary.utility;

import org.slf4j.Logger;

public interface FancyLogging {
    Logger getLogger();
    boolean canPrint();

    default void debug(LogType type, String format, Object... args) {if (canPrint()) getLogger().debug(type.format(format), args);}
    default void debug(String format, Object... args) {debug(LogType.ACTION, format, args);}
    default void trace(LogType type, String format, Object... args) {if (canPrint()) getLogger().trace(type.format(format), args);}
    default void trace(String format, Object... args) {trace(LogType.WARN, format, args);}

    default void info(LogType type, String format, Object... args) {if (canPrint()) getLogger().info(type.format(format), args);}
    default void info(String format, Object... args) {info(LogType.ACTION, format, args);}
    default void warn(LogType type, String format, Object... args) {if (canPrint()) getLogger().warn(type.format(format), args);}
    default void warn(String format, Object... args) {warn(LogType.WARN, format, args);}
    default void error(LogType type, String format, Object... args) {
        if (canPrint()) {
            getLogger().error(type.format(format), args);
            getLogger().trace(type.format(format), args);
        }
    }
    default void error(String format, Object... args) {error(LogType.ERROR, format, args);}

    default void errorWithStackTrace(Exception exception, String prefixMessage, Object... args) {
        String message = exception.getMessage() != null ? exception.getMessage() : exception.toString();
        getLogger().error(LogType.ERROR.format((prefixMessage != null ? prefixMessage + "; " : "") + message), args);
        getLogger().trace("Stacktrace:", exception);
    }
    default void errorWithStackTrace(Exception exception) {
        errorWithStackTrace(exception, "");
    }

    default void warnWithStackTrace(Exception exception, String prefixMessage, Object... args) {
        String message = exception.getMessage() != null ? exception.getMessage() : exception.toString();
        getLogger().warn(LogType.WARN.format((prefixMessage != null ? prefixMessage + "; " : "") + message), args);
        getLogger().trace("Stacktrace:", exception);
    }
    default void warnWithStackTrace(Exception exception) {
        warnWithStackTrace(exception, "");
    }

    enum LogType {
        ACTION("→"),
        SUB("↳"),
        DETAIL("  ↳"),
        DONE("✔"),
        ERROR("✖"),
        WARN("⚠");

        private final String symbol;
        LogType(String symbol) { this.symbol = symbol; }
        public String format(String formatTo) { return symbol + "|  " + formatTo; }
    }
}

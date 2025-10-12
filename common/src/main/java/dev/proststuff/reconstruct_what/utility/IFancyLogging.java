package dev.proststuff.reconstruct_what.utility;

import org.slf4j.Logger;

/**
 * Why add this???
 * <p>
 * Why not.
 */
public interface IFancyLogging {
    Logger getLogger();
    boolean canPrint();

    default void info(LogType type, String format, Object... args) {if (canPrint()) getLogger().info(type.format(format), args);}
    default void warn(LogType type, String format, Object... args) {if (canPrint()) getLogger().warn(type.format(format), args);}
    default void error(LogType type, String format, Object... args) {getLogger().error(type.format(format), args);}

    enum LogType {
        ACTION("→"),
        SUB("↳"),
        DETAIL("  ↳"),
        DONE("✔"),
        ERROR("✖"),
        WARN("⚠");

        private final String symbol;
        LogType(String symbol) { this.symbol = symbol; }
        public String format(String formatTo) { return symbol + " " + formatTo; }
    }
}

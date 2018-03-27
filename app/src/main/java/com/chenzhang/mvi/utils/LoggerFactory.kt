package com.chenzhang.mvi.utils

import de.mindpipe.android.logging.log4j.LogCatAppender
import de.mindpipe.android.logging.log4j.LogConfigurator
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.PatternLayout

class LoggerFactory {
    companion object {
        fun <T> getLogger(clazz: Class<T>): Logger {
            val logConfigurator = LogConfigurator()
            logConfigurator.rootLevel = Level.DEBUG
            logConfigurator.isUseFileAppender = false
            // The default pattern is "%m%n" which appends a newline to the message. This newline causes Android Studio
            // to fail to print the throwable stack trace if it's present, so we're overridding the pattern to drop this
            // newline in order to fix this.
            logConfigurator.logCatPattern = "%m"
            logConfigurator.configure()

            /* Forgive this hack. LogConfigurator doesn't provide a direct hook to set the logcat tag */
            val logger = Logger.getRootLogger()
            val appenders = logger.allAppenders
            while (appenders.hasMoreElements()) {
                val appender = appenders.nextElement()
                if (appender is LogCatAppender) {
                    appender.tagLayout = PatternLayout("%c{1}") // Simple class name (not fully qualified)
                }
            }

            return Logger.getLogger(clazz)
        }
    }
}

package com.chenzhang.mvi.application

import de.mindpipe.android.logging.log4j.LogCatAppender
import de.mindpipe.android.logging.log4j.LogConfigurator
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.PatternLayout

class Log4JConfigurator {
    companion object {
        fun configureLogging() {
            LogConfigurator().apply {
                rootLevel = Level.DEBUG
                isUseFileAppender = false
                // The default pattern is "%m%n" which appends a newline to the message. This newline causes Android Studio
                // to fail to print the throwable stack trace if it's present, so we're overridding the pattern to drop this
                // newline in order to fix this.
                logCatPattern = "%m"
                configure()
            }

            /* Forgive this hack. LogConfigurator doesn't provide a direct hook to set the logcat tag */
            val logger = Logger.getRootLogger()

            val appenders = logger.allAppenders
            while (appenders.hasMoreElements()) {
                val appender = appenders.nextElement()
                if (appender is LogCatAppender) {
                    appender.tagLayout = PatternLayout("%c{1}") // Simple class name (not fully qualified)
                }
            }
        }
    }
}

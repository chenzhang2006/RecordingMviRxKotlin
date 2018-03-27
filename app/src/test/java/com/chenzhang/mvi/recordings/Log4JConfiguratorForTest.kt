package com.chenzhang.mvi.recordings

import org.apache.log4j.ConsoleAppender
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.PatternLayout

class Log4JConfiguratorForTest {
    companion object {
        fun configure() {
            val rootLogger = Logger.getRootLogger()
            val console = ConsoleAppender(PatternLayout(
                    "%d{ABSOLUTE} %5p %c{1}:%L - %m%n"), ConsoleAppender.SYSTEM_OUT)
            console.name = "console"

            rootLogger.removeAllAppenders()
            rootLogger.addAppender(console)
            rootLogger.level = Level.INFO
        }
    }
}

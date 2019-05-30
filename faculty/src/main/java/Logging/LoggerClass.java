package Logging;

import org.apache.log4j.Logger;
public class LoggerClass {
        private final static Logger logger = Logger.getLogger(LoggerClass.class);

        public static void main(String[] args) {
            logger.trace("Trace log message");
            logger.debug("Debug log message");
            logger.info("Info log message");
            logger.error("Error log message");
            logger.fatal("Fatal log message");
        }
}

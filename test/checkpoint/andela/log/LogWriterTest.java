package checkpoint.andela.log;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferSingletons;
import checkpoint.andela.db.DbRecord;
import checkpoint.andela.models.Reaction;
import checkpoint.andela.parser.FileParser;

import org.junit.Test;

import java.util.Date;

public class LogWriterTest {

    @Test
    public void testRun() throws Exception {
        String logFileName = "logs/logFile-"
                + (new Date()).toString()
                + ".txt";

        Buffer<Reaction> reactionBuffer = BufferSingletons.getReactionBuffer();
        Buffer<String> logBuffer = BufferSingletons.getStringLogBuffer();

        FileParser fileParser
                = new FileParser("data/react.dat", reactionBuffer, logBuffer);
        LogWriter logWriter = new LogWriter(logBuffer, logFileName);

        Thread fileParserThread = new Thread(fileParser);
        Thread logWriterThread = new Thread(logWriter);

        fileParserThread.run();
        logWriterThread.run();
    }
}
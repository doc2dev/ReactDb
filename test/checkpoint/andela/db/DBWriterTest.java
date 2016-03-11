package checkpoint.andela.db;

import checkpoint.andela.buffer.Buffer;
import checkpoint.andela.buffer.BufferFactory;
import checkpoint.andela.config.Constants;
import checkpoint.andela.parser.FileParser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DBWriterTest {

    private FileParser fileParser;

    private DBWriter writer;
    @Before
    public void beforeTestRun() {
        String filePath = "data/react.dat";

        Buffer<DbRecord> recordBuffer = BufferFactory.getDbRecordBuffer();
        Buffer<String> logBuffer = BufferFactory.getStringLogBuffer();

        MyDbWriter myDbWriter = new MyDbWriter(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.MYSQL_TABLE_NAME);

        writer = new DBWriter(recordBuffer, myDbWriter, logBuffer);

        fileParser = new FileParser(filePath, recordBuffer, logBuffer);
    }

    @Test
    public void testRun() throws Exception {
        Thread fileParserThread = new Thread(fileParser);
        Thread dbWriterThread = new Thread(writer);

        Thread.currentThread().setName("Test thread");

        fileParserThread.run();
        dbWriterThread.run();
    }
}
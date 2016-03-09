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

    @Before
    public void beforeTestRun() {
        String filePath = "data/reactions.dat";
        fileParser = new FileParser(filePath,
                BufferFactory.getDbRecordBuffer());
    }

    @Test
    public void testRun() throws Exception {
        MyDbWriter myDbWriter = new MyDbWriter(Constants.MYSQL_DRIVER_NAME,
                Constants.MYSQL_URL,
                Constants.MYSQL_USERNAME,
                Constants.MYSQL_PASSWORD,
                Constants.MYSQL_TABLE_NAME);

        Buffer<DbRecord> dbRecordBuffer = BufferFactory.getDbRecordBuffer();
        DBWriter writer = new DBWriter(dbRecordBuffer, myDbWriter);

        Thread fileParserThread = new Thread(fileParser);
        Thread dbWriterThread = new Thread(writer);
        fileParserThread.run();
        dbWriterThread.run();
    }
}
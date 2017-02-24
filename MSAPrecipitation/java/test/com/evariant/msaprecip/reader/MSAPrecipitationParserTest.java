package test.com.evariant.msaprecip.reader;

import java.io.IOException;
import java.util.Map;

import org.junit.*;
import static org.junit.Assert.*;

import com.evariant.msaprecip.reader.MSAPrecipitationParser;

public class MSAPrecipitationParserTest {
	MSAPrecipitationParser precipParser;
	String file;
	char delimiter;
	
	@Before
	public void setUp() {
		//Skip 1 header line for precipitation file
		precipParser = new MSAPrecipitationParser(0, 40, 1, 2, 1, ',');
		file = "java/test/resource/hourly.txt";  //Test file
		//file = "java/resource/hourly.csv";  //production file
		delimiter = ',';
		System.out.println("In setup");
	}

	@Test
	public void testParse() {
		try{
			Map<Long,Float> pMap = precipParser.parse(file);
			for (Map.Entry<Long,Float> entry: pMap.entrySet()) {
				Long key = entry.getKey();
				float precipitation = entry.getValue();
				assertEquals(key, (Long)102L);
				assertEquals(0.5, precipitation, 0.00001f);
				System.out.println(entry);
			}
		}catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	@After
	public void tearDown(){
		System.out.println("Running: tearDown");
	}

}

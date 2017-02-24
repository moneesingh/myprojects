package test.com.evariant.msaprecip.reader;

import java.io.IOException;
import java.util.Map;

import org.junit.*;
import static org.junit.Assert.*;

import com.evariant.msaprecip.MSA;
import com.evariant.msaprecip.reader.WBANMSAParser;

public class WBANMSAParserTest {
	WBANMSAParser wbanParser;
	String file;
	char delimiter;
	
	@Before
	public void setUp() {
		//Columns
		wbanParser = new WBANMSAParser(0, 6, 7, 1, '|');
		file = "java/test/resource/station.txt";
		delimiter = '|';
		System.out.println("In setup");
	}


	@Test
	public void testParse() {
		try {
			Map<Long,MSA> wMap = wbanParser.parse(file);
			for (Map.Entry<Long,MSA> entry: wMap.entrySet()) {
				Long key = entry.getKey();
				MSA msa = entry.getValue();
				assertEquals(key, (Long)100L);
				assertTrue(msa.equals(new MSA("ARKADELPHIA","AR")));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown(){
		System.out.println("Running: tearDown");
	}


}

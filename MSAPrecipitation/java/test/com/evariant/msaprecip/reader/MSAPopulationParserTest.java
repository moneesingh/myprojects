package test.com.evariant.msaprecip.reader;

import java.io.IOException;
import java.util.Map;

import org.junit.*;
import static org.junit.Assert.*;

import com.evariant.msaprecip.MSA;
import com.evariant.msaprecip.reader.MSAPolulationParser;

public class MSAPopulationParserTest {
	MSAPolulationParser populationParser;
	String file;
	char delimiter;
	
	@Before
	public void setUp() {
		//Skip 8 header lines
		populationParser = new MSAPolulationParser(0, 2, 4, 8, ',');
		file = "java/test/resource/population.csv";
		delimiter = ',';
		System.out.println("In setup");
	}

	@Test
	public void testParse() {
		
		try {
			Map<MSA,Long> pMap = populationParser.parse(file);
			for (Map.Entry<MSA,Long> entry: pMap.entrySet()) {
				Long value = entry.getValue();
				MSA msa = entry.getKey();
				assertEquals(value, (Long)167813L);
				assertTrue(msa.equals(new MSA("Abilene", "TX")));
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

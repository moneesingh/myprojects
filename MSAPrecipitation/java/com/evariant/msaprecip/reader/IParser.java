package com.evariant.msaprecip.reader;

import java.io.IOException;
import java.util.Map;


public interface IParser <M,N> {

	Map<M,N> parse (String file) throws IOException;
}

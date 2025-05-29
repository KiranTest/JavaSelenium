package com.hrsoft.utils.zerocell.converters;

import com.creditdatamw.zerocell.converter.Converter;

public class StringToIntConverter implements Converter<Integer>{

	@Override
	public Integer convert(String Value, String arg1, int arg2) {
		
		return  Integer.parseInt(Value);
	}
	

}

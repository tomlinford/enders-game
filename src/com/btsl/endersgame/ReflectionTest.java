package com.btsl.endersgame;

import android.test.ActivityInstrumentationTestCase2;

public class ReflectionTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public ReflectionTest() {  
	       super(MainActivity.class);  
	   }
	
	public void test() {
		Object o = Integer.valueOf(1);
		System.out.print(o instanceof Integer);
		fail("Not yet implemented");
	}

}

package com.polytech.planning;

import org.junit.Assert;
import org.junit.Test;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

import com.polytech.planning.controller.ToolBox;

public class ToolBoxTest {

	@Test
	public void capitalize() {
		String str = "pommes";
		String output = "Pommes";
		str = ToolBox.capitalize(str);

		Assert.assertEquals(output, str);
	}

	@Test
	public void getColLetter() {
		try {
			Assert.assertEquals("F", ToolBox.getColLetter(5));
			Assert.assertEquals("Z", ToolBox.getColLetter(25));
			Assert.assertEquals("A", ToolBox.getColLetter(0));
			Assert.assertEquals("X", ToolBox.getColLetter(23));
			Assert.assertEquals("ZZ", ToolBox.getColLetter(675));
			Assert.assertEquals("BG", ToolBox.getColLetter(32));
		} catch (InvalidValue e) {
			e.printStackTrace();
		}
	}
}

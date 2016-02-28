package uk.co.zoneofavoidance.my.tasks.controllers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link EnumConverter}.
 */
public class EnumConverterTest {

   private static enum MyEnum {
      FOO, BAR, NONE;
   }

   private EnumConverter<MyEnum> converter;

   @Before
   public void setUp() throws Exception {
      converter = new EnumConverter<>(MyEnum.class, MyEnum.NONE);
   }

   @Test
   public void setAsTextSupportsLowerCaseNames() {
      converter.setAsText("foo");
      assertEquals(MyEnum.FOO, converter.getValue());
      converter.setAsText("bar");
      assertEquals(MyEnum.BAR, converter.getValue());
   }

   @Test
   public void setAsTextSupportsUpperCaseNames() {
      converter.setAsText("FOO");
      assertEquals(MyEnum.FOO, converter.getValue());
      converter.setAsText("BAR");
      assertEquals(MyEnum.BAR, converter.getValue());
   }

   @Test
   public void setAsTextSupportsMixedCaseNames() {
      converter.setAsText("fOo");
      assertEquals(MyEnum.FOO, converter.getValue());
      converter.setAsText("Bar");
      assertEquals(MyEnum.BAR, converter.getValue());
   }

   @Test
   public void setAsTextReturnsOnErrorValueForUnrecognisedName() {
      converter.setAsText("fooo");
      assertEquals(MyEnum.NONE, converter.getValue());
   }

   @Test
   public void setAsTextReturnsOnErrorValueForEmptyName() {
      converter.setAsText("");
      assertEquals(MyEnum.NONE, converter.getValue());
   }

   @Test(expected = IllegalArgumentException.class)
   public void setAsTextThrowsForUnrecognisedNameIfOnErrorValueNotAvailable() {
      converter = new EnumConverter<EnumConverterTest.MyEnum>(MyEnum.class);
      converter.setAsText("fooo");
   }

   @Test(expected = NullPointerException.class)
   public void setAsTextThrowsForNullName() {
      converter.setAsText(null);
      assertEquals(MyEnum.NONE, converter.getValue());
   }

   @Test
   public void getAsTextReturnsLowerCaseName() {
      converter.setValue(MyEnum.FOO);
      assertEquals("foo", converter.getAsText());
      converter.setValue(MyEnum.BAR);
      assertEquals("bar", converter.getAsText());
   }

   @Test(expected = IllegalArgumentException.class)
   public void setValueThrowsForNonMatchingType() {
      converter.setValue("foo");
   }

}

package com.ylfin.spider.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * StringUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 19, 2017</pre>
 */
public class StringUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: replaceSpaceWithPlus(String keyword)
     */
    @Test
    public void testReplaceSpaceWithPlus() throws Exception {
        String keyword = "ps4 gt sport 二手";
        String result = StringUtils.replaceSpaceWithPlus(keyword);
        Assert.isTrue(result.contains("ps4+gt+sport+二手"), result);
        Assert.isTrue(!result.contains("ps4 gt+sport+二手"), result);
    }

  @Test
  public void testSplit(){
      String str= "abvc&123&";
      System.out.println(str.substring(0,str.length()-1));
  }
  @Test
  public void SystemOutTest(){
      System.out.println("171.42.201.118:5374");
      System.out.println("171.42.201.118:5374");
  }
}

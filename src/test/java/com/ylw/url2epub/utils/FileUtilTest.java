package com.ylw.url2epub.utils;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeleteDir() {
		FileUtil.deleteDir("C:\\Users\\ylw\\Desktop\\epubReader");
		assertFalse("文件未被删除！", FileUtil.isExistFile("C:\\Users\\ylw\\Desktop\\epubReader"));
	}

}

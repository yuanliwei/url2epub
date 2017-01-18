package com.ylw.url2epub.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZipUtilTest {

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

	@SuppressWarnings("unused")
	@Test
	public void test() throws IOException, ArchiveException {
		String name = "C:\\Users\\ylw\\Desktop\\new epub\\test.zip";
		@SuppressWarnings("resource")
		OutputStream file = new FileOutputStream(name);

		File addFile = new File("C:\\Users\\ylw\\Desktop\\new epub\\mimetype");

		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(name)));
		ZipArchiveOutputStream ozip = (ZipArchiveOutputStream) new ArchiveStreamFactory("utf-8")
				.createArchiveOutputStream("zip", out);
		ArchiveEntry archiveEntry = new ZipArchiveEntry(addFile.getName());
		ozip.putArchiveEntry(archiveEntry);
		InputStream input = new FileInputStream(addFile);
		org.apache.commons.compress.utils.IOUtils.copy(input, ozip, 2048);
		input.close();
		ozip.closeArchiveEntry();
		ozip.close();
	}

	@Test
	public void testZipFiles() throws ArchiveException, IOException {
		String filesPath = "C:\\Users\\ylw\\Desktop\\new epub\\aa\\";
		String filesPath1 = "C:\\Users\\ylw\\Desktop\\new epub\\";
		String outFile = filesPath1 + File.separator + "EBOOK.epub.zip";

		FileUtil.delete(outFile);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outFile)));
		ZipArchiveOutputStream ozip = (ZipArchiveOutputStream) new ArchiveStreamFactory("utf-8")
				.createArchiveOutputStream("zip", out);
		Path path = Paths.get(filesPath);
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path fileName = file.subpath(path.getNameCount(), file.getNameCount());
				ArchiveEntry archiveEntry = new ZipArchiveEntry(fileName.toString());
				InputStream input = new FileInputStream(file.toFile());
				ozip.putArchiveEntry(archiveEntry);
				org.apache.commons.compress.utils.IOUtils.copy(input, ozip, 2048);
				input.close();
				ozip.closeArchiveEntry();
				return FileVisitResult.CONTINUE;
			}
		});
		ozip.close();
	}

	@Test
	public void testTempFile() {
		String rootDir = System.getProperty("java.io.tmpdir");
		System.out.println(rootDir);
	}
}

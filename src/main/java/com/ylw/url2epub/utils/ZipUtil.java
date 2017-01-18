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
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.util.IOUtils;

public class ZipUtil {

	private static Log log = LogFactory.getLog(ZipUtil.class);

	/**
	 * zip压缩文件
	 * 
	 * @param dir
	 * @param zippath
	 */
	public static void zip(String dir, String zippath) {
		try {
			FileUtil.delete(zippath);
			File zipFile = new File(zippath);
			Files.createDirectories(Paths.get(zipFile.getParent()));
			OutputStream out;
			out = new BufferedOutputStream(new FileOutputStream(new File(zippath)));
			ZipArchiveOutputStream ozip = (ZipArchiveOutputStream) new ArchiveStreamFactory("utf-8")
					.createArchiveOutputStream("zip", out);
			Path path = Paths.get(dir);
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
		} catch (IOException | ArchiveException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *            zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir
	 *            解压后的文件存放路径, 如"D:/test/" ()
	 */
	public static void unzip(String zipFilePath, String saveFileDir) {
		// Path path = Paths.get(saveFileDir);
		File dir = new File(saveFileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(zipFilePath);

		if (file.exists()) {
			InputStream is = null;
			ZipArchiveInputStream zais = null;
			try {
				is = new FileInputStream(file);
				zais = new ZipArchiveInputStream(is);
				ArchiveEntry archiveEntry = null;
				while ((archiveEntry = zais.getNextEntry()) != null) {
					// 获取文件名
					String entryFileName = archiveEntry.getName();
					// 构造解压出来的文件存放路径
					String entryFilePath = saveFileDir + entryFileName;
					OutputStream os = null;
					try {
						// 把解压出来的文件写到指定路径
						File entryFile = new File(entryFilePath);
						if (entryFileName.endsWith("/")) {
							entryFile.mkdirs();
						} else {
							os = new BufferedOutputStream(new FileOutputStream(entryFile));
							byte[] buffer = new byte[1024];
							int len = -1;
							while ((len = zais.read(buffer)) != -1) {
								os.write(buffer, 0, len);
							}
						}
					} catch (IOException e) {
						throw new IOException(e);
					} finally {
						if (os != null) {
							os.flush();
							os.close();
						}
					}

				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.close(zais);
				IOUtils.close(is);
			}
		}
	}
}
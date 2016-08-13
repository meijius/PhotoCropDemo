package com.mmeijiu.photo.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

	/** 格式化文件大小 */
	public static String formatSize(long size) {
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };

		int i = 0;
		for (; i < units.length; i++) {
			if (size < Math.pow(1024, i + 1))
				break;
		}
		return ((long) (size / Math.pow(1024, i) * 100) / 100.0) + units[i];
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		String extension = "";
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}

	/** 对象序列化 */
	public static void ObjectSerialization(File file, Object object)
			throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				fileOutputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		objectOutputStream.close();
		objectOutputStream = null;
	}

	/** 对象反序列化 */
	public static Object ObjectDeserialization(File file)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream objectInpuStream = new ObjectInputStream(
				fileInputStream);
		Object object = objectInpuStream.readObject();
		objectInpuStream.close();
		objectInpuStream = null;
		return object;
	}

	/** 复制文件 */
	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			File pf = destFile.getParentFile();
			if (!pf.exists()) {
				pf.mkdirs();
			}
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	public static void copyFdToFile(FileDescriptor src, File dst)
			throws IOException {
		if (!dst.exists()) {
			File pf = dst.getParentFile();
			if (!pf.exists()) {
				pf.mkdirs();
			}
			dst.createNewFile();
		}
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	public static boolean copyAssetsToSdCard(Context context, String name,
			String path) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = context.getAssets().open(name);

			File file = new File(path);
			File pf = file.getParentFile();
			if (!pf.exists()) {
				pf.mkdirs();
			}
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/** 删除文件夹 */
	public static void deleteAllFilesOfDir(File path, boolean isDeleteSelf) {
		if (path == null || !path.exists()) {
			return;
		}
		// 如果是文件就直接删除了
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFilesOfDir(files[i], isDeleteSelf);
		}
		if (isDeleteSelf) {
			path.delete();
		}
	}

	/** 创建 .nomeadia */
	public static void createNoMediaFile(File parentFile) {
		try {
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			File file = new File(parentFile, ".nomedia");
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压 zip
	 * 
	 * @param file
	 *            需要解压的文件
	 */
	public static boolean unpackZip(File file) {
		InputStream is = null;
		;
		ZipInputStream zis = null;
		try {
			is = new FileInputStream(file);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[1024];

			File parFile = file.getParentFile();
			String filename;
			while ((ze = zis.getNextEntry()) != null) {
				// zapis do souboru
				filename = ze.getName();
				// filename = filename.substring(filename.lastIndexOf("/") + 1);
				System.out.println(filename);

				// Need to create directories if not exists, or
				// it will generate an Exception...
				File fmd = new File(parFile, filename);
				if (ze.isDirectory()) {
					fmd.mkdirs();
					continue;
				}

				FileOutputStream fout = new FileOutputStream(fmd);

				// cteni zipu a zapis
				int count;
				while ((count = zis.read(buffer)) != -1) {
					fout.write(buffer, 0, count);
				}
				zis.closeEntry();
				fout.close();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (zis != null) {
					zis.close();
					zis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
}

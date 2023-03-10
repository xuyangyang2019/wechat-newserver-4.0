package com.jubotech.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jubotech.framework.common.Constants;

public class FileUtil {
 
	public static String getMd5ByFile(MultipartFile upload) throws Exception {
		byte[] uploadBytes = upload.getBytes();
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digest = md5.digest(uploadBytes);
		String hashString = new BigInteger(1, digest).toString(16);
		// 修改统一方法，在MD5前加上时间戳
		return hashString.toUpperCase();
	}

	public static void copy(MultipartFile fileSrc, File fileDest) throws Exception {
		File fileDestParent = fileDest.getParentFile();
		if (fileDestParent != null && (!fileDestParent.exists())) {
			fileDestParent.mkdirs();
		}
		fileSrc.transferTo(fileDest);
	}

	/**
	 * 计算文件的MD5
	 */
	public static String getMD5(File file) throws Exception {
		return DigestUtils.md5Hex(new FileInputStream(file));
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 *
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.<br>
	 *         If a deletion fails, the method stops attempting to delete and
	 *         returns "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			if (null != children) {
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 根据传入的url和路径，并保存新文件，最后返回下载链接
	 *
	 * @param file
	 *            文件
	 * @return 最终路径
	 * 
	 */
	public static Map<String, Object> saveFile(MultipartFile file, String isMp3ToAmr,String isAmrToMp3,String serverip,Integer serverPort,String filePath) throws Exception {

		// 获取文件MD5
		String md5 = FileUtil.getMd5ByFile(file);
		// 这里频繁使用md5做文件目录不好，每次上传都是一个新的md5字符串，在没有批量上传的情况下，一个文件夹就只有一张图片
		// 改成：使用日期做文件目录，一天一个，方便管理
		String datePath = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_5);
		String suffix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

		final String newPath = filePath.replace("file:", "") + Constants.FILE_PATH + File.separator + datePath + File.separator + md5 + "." + suffix;

		File fileTemp = new File(newPath);

		// 将文件流保存至指定路径
		FileUtils.copyInputStreamToFile(file.getInputStream(), fileTemp);

		//下面这段代码要依赖silk-v3-decoder-master
		////////////////start/////////////
		if (!StringUtils.isEmpty(isMp3ToAmr) && isMp3ToAmr.equals("true")) {
			// 如果是mp3
			if (suffix.equalsIgnoreCase("mp3")) {
				try {
					new Thread(new Runnable() {
						@Override
						public void run() {
							ConverterAmrMp3.mp3ToAmr(null, newPath);
						}
					}).start();
					suffix = "amr";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			if (!StringUtils.isEmpty(isAmrToMp3) && isAmrToMp3.equals("true")) {
				// 如果是amr
				if (suffix.equalsIgnoreCase("amr")) {
					try {
						new Thread(new Runnable() {
							@Override
							public void run() {
								ConverterAmrMp3.amrToMp3(null, newPath);
							}
						}).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
					suffix = "mp3";
				}
			}
		}
        ////////////////end/////////////////
		
		String url = "http://" + serverip + ":" + serverPort + "/"+ Constants.FILE_PATH + "/" + datePath + "/" + md5 + "." + suffix;

		Long fileSize = fileTemp.length();

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("url", url);

		data.put("fileSize", fileSize);

		return data;
	}

}

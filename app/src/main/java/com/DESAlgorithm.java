package com.fuchun.reader.utils;



/**
 * <p>
 * Title: DESAlgorithm
 * </p>
 * <p>
 * Description: DESAlgorithm but this is not standard DES algorithm.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: ND
 * </p>
 * 
 * @author: Jason.Lee
 * @version: 1.0
 */
public class DESAlgorithm {
	// permuted choice table (key)
	private static DESAlgorithm instance = null;
	private final static byte[] PC1_Table = { 57, 49, 41, 33, 25, 17, 9, 1, 58,
			50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52,
			44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14,
			6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

	private static final char[] HEXCODE = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final byte[] K = new byte[64];
	private static final byte[] ROTATEL_TMP = new byte[256];
	private static final byte[] TRANSFORM_TMP = new byte[256];
	private static byte[] F_FUNC_MR = new byte[48];
	private static final byte[] DES_RUN_TMP = new byte[32];
	private static final byte[] DES_RUN_M = new byte[64];
	private static final byte ENCRYPT = 0;
	private static final byte DECRYPT = 1;
	private byte[][] subKey = new byte[16][48];//

	private static final byte[] LOOP_TABLE = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2,
			2, 2, 2, 2, 1 };

	private final static byte[] PC2_TABLE = { 14, 17, 11, 24, 1, 5, 3, 28, 15,
			6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31,
			37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42,
			50, 36, 29, 32 };

	private final static byte[] IP_TABLE = { 58, 50, 42, 34, 26, 18, 10, 2, 60,
			52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56,
			48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43,
			35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39,
			31, 23, 15, 7 };

	private static final byte[] E_TABLE = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8,
			9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19,
			20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30,
			31, 32, 1 };

	// 32-bit permutation function P used on the output of the S-boxes
	private static final byte[] P_TABLE = { 16, 7, 20, 21, 29, 12, 28, 17, 1,
			15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30,
			6, 22, 11, 4, 25 };

	private final static byte[] S_BOXSINGLE = {
			// S1
			14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4,
			14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11,
			15, 12, 9,
			7,
			3,
			10,
			5,
			0,
			15,
			12,
			8,
			2,
			4,
			9,
			1,
			7,
			5,
			11,
			3,
			14,
			10,
			0,
			6,
			13,
			// S2
			15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7,
			15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13,
			1, 5, 8, 12, 6, 9, 3,
			2,
			15,
			13,
			8,
			10,
			1,
			3,
			15,
			4,
			2,
			11,
			6,
			7,
			12,
			0,
			5,
			14,
			9,
			// S3
			10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9,
			3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0,
			11, 1, 2, 12, 5, 10, 14, 7, 1,
			10,
			13,
			0,
			6,
			9,
			8,
			7,
			4,
			15,
			14,
			3,
			11,
			5,
			2,
			12,
			// S4
			7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5,
			6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13,
			15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6,
			10,
			1,
			13,
			8,
			9,
			4,
			5,
			11,
			12,
			7,
			2,
			14,
			// S5
			2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2,
			12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7,
			8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2,
			13,
			6,
			15,
			0,
			9,
			10,
			4,
			5,
			3,
			// S6
			12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2,
			7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3,
			7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14,
			1,
			7,
			6,
			0,
			8,
			13,
			// S7
			4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7,
			4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14,
			10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15,
			14, 2,
			3,
			12,
			// S8
			13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8,
			10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2,
			0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0,
			3, 5, 6, 11 };

	private static final byte[] IPR_TABLE = { 40, 8, 48, 16, 56, 24, 64, 32,
			39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37,
			5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3,
			43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41,
			9, 49, 17, 57, 25 };

	private DESAlgorithm() {

	}

	public static DESAlgorithm getInstance() {
		if (null == instance) {
			instance = new DESAlgorithm();
		}
		return instance;
	}

	/**
	 * encrypt
	 * 
	 * @param key
	 * @param dataIn
	 * @return
	 */
	public String encode(String key, String dataIn) {
		generateKey(key.getBytes());
		int pos = 0;
		int len = (dataIn.length() - 1) * 2;
		if (dataIn.length() == 1) {
			len = 2;
		}
		len = ((len + 7) / 8) * 8;
		len *= 2;
		byte[] dataTmp = dataIn.getBytes();
		byte[] dataTmp2 = new byte[len];
		for (int i = 0; i < dataTmp2.length && i / 2 < dataTmp.length; i += 2) {
			dataTmp2[i] = dataTmp[i / 2];
			dataTmp2[i + 1] = 0;
		}
		while (pos < len) {
			desRun(dataTmp2, pos, dataTmp2, pos, ENCRYPT);
			pos += 8;
		}
		char out[] = transCode(dataTmp2);
		return new String(out);
	}

	/**
	 * dencrypt
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public String decode(String key, String data) {
		generateKey(key.getBytes());
		byte[] bData = data.getBytes();
		byte[] deData = deTransCode(bData);
		int pos = 0;
		int len = deData.length;
		len = ((len + 7) / 8) * 8;
		while (pos < len) {
			desRun(deData, pos, deData, pos, DECRYPT);
			pos += 8;
		}
		byte[] outData = new byte[deData.length / 2];
		for (int i = 0; i < deData.length - 1; i += 2) {
			outData[i / 2] = deData[i];
		}
		String result = new String(outData);
		result = replaceAll(result, String.valueOf((char) 0), "");
		return result;
	}

	private String replaceAll(String src, String regex, String replacement) {
		int i = 0;
		int j = 0;
		StringBuffer buffer = new StringBuffer();
		while ((j = src.indexOf(regex, i)) >= 0) {
			buffer.append(src.substring(i, j));
			buffer.append(replacement);
			i = j + regex.length();
		}
		buffer.append(src.substring(i));
		return buffer.toString();
	}

	private void generateKey(byte[] Key) {
		byte[] newKey = new byte[8];
		System
				.arraycopy(Key, 0, newKey, 0, Math.min(Key.length,
						newKey.length));
		byte2Bit(K, 0, newKey, 0, 64);
		transform(K, 0, K, 0, PC1_Table, 56);
		for (int i = 0; i < 16; i++) {
			rotateL(K, 0, 28, LOOP_TABLE[i]);
			rotateL(K, 28, 28, LOOP_TABLE[i]);
			transform(subKey[i], 0, K, 0, PC2_TABLE, 48);
		}
	}

	private void xor(byte[] inA, int inAIndex, byte[] inB, int inBIndex, int len) {
		for (int i = 0; i < len; i++)
			inA[inAIndex + i] ^= inB[inBIndex + i];
	}

	private void sBox(byte[] out, int outIndex, byte[] in) {
		int OutIndex = 0;
		int InIndex = 0;
		for (int i = 0, j, k; i < 8; i++) {
			j = (in[InIndex + 0] << 1) + in[InIndex + 5];
			k = (in[InIndex + 1] << 3) + (in[InIndex + 2] << 2)
					+ (in[InIndex + 3] << 1) + in[InIndex + 4];
			byte2Bit(out, outIndex + OutIndex, S_BOXSINGLE, i * 4 * 16 + j * 16
					+ k, 4);
			OutIndex += 4;
			InIndex += 6;
		}
	}

	private void function(byte[] in, int inIndex, byte[] ki) {
		transform(F_FUNC_MR, 0, in, inIndex, E_TABLE, 48);
		xor(F_FUNC_MR, 0, ki, 0, 48);
		sBox(in, inIndex, F_FUNC_MR);
		transform(in, inIndex, in, inIndex, P_TABLE, 32);
	}

	private void byte2Bit(byte[] out, int outIndex, byte[] in, int inIndex,
			int bits) {
		for (int i = 0; i < bits; i++) {
			out[outIndex + i] = (byte) ((in[inIndex + i / 8] >> (i % 8)) & 1);
		}
	}

	private void bit2Byte(byte[] out, int outIndex, byte[] in, int inIndex,
			int bits) {
		for (int i = 0; i < (bits + 7) / 8; i++) {
			out[outIndex + i] = 0;
		}
		for (int i = 0; i < bits; i++)
			out[outIndex + i / 8] |= in[inIndex + i] << (i % 8);
	}

	private void transform(byte[] out, int outIndex, byte[] in, int inIndex,
			byte[] Table, int len) {
		for (int i = 0; i < len; i++)
			TRANSFORM_TMP[i] = in[inIndex + Table[i] - 1];
		System.arraycopy(TRANSFORM_TMP, 0, out, outIndex, len);
	}

	private void rotateL(byte[] in, int startIndex, int len, int loop) {
		System.arraycopy(in, startIndex, ROTATEL_TMP, 0, loop);
		for (int i = 0; i < len - loop; i++) {
			in[startIndex + i] = in[startIndex + i + loop];
		}
		System.arraycopy(ROTATEL_TMP, 0, in, startIndex + len - loop, loop);
	}

	private void desRun(byte[] out, int outIndex, byte[] in, int inIndex,
			byte type) {
		byte2Bit(DES_RUN_M, 0, in, inIndex, 64);
		transform(DES_RUN_M, 0, DES_RUN_M, 0, IP_TABLE, 64);
		if (type == ENCRYPT) {
			for (int i = 0; i < 16; i++) {
				System.arraycopy(DES_RUN_M, 32, DES_RUN_TMP, 0, 32);
				function(DES_RUN_M, 32, subKey[i]);
				xor(DES_RUN_M, 32, DES_RUN_M, 0, 32);
				System.arraycopy(DES_RUN_TMP, 0, DES_RUN_M, 0, 32);
			}
		} else {
			for (int i = 15; i >= 0; i--) {
				System.arraycopy(DES_RUN_M, 0, DES_RUN_TMP, 0, 32);
				function(DES_RUN_M, 0, subKey[i]);
				xor(DES_RUN_M, 0, DES_RUN_M, 32, 32);
				System.arraycopy(DES_RUN_TMP, 0, DES_RUN_M, 32, 32);
			}
		}
		transform(DES_RUN_M, 0, DES_RUN_M, 0, IPR_TABLE, 64);
		bit2Byte(out, outIndex, DES_RUN_M, 0, 64);
	}

	private char[] transCode(byte[] data) {
		int len = data.length;
		char[] buffer = new char[len * 3];
		int index = 0;
		for (int i = 0, k = 0; i < len; i++) {
			int tempChar = (data[i] & 0xFF);
			if ((tempChar >= 'a' && tempChar <= 'z')
					|| (tempChar >= 'A' && tempChar <= 'Z')
					|| (tempChar >= '0' && tempChar <= '9')) {// 
				buffer[k++] = (char) tempChar; //
				index++;
			} else {
				buffer[k++] = '%';
				index++;
				buffer[k++] = (char) HEXCODE[(tempChar >>> 4) & 15]; // >>>
				index++;
				buffer[k++] = (char) HEXCODE[tempChar & 15];
				index++;
			}
		}
		char[] result = new char[index];
		System.arraycopy(buffer, 0, result, 0, index);
		return result;
	}

	private byte[] deTransCode(byte[] data) {
		int len = data.length;
		byte[] buffer = new byte[len];
		int k = 0;
		for (int m = 0; m < len; m++) {

			char tempChar = (char) data[m];
			if ((tempChar >= 'a' && tempChar <= 'z')
					|| (tempChar >= 'A' && tempChar <= 'Z')
					|| (tempChar >= '0' && tempChar <= '9')) {
				buffer[k++] = (byte) tempChar;
			} else {
				int num = 0;
				num += 16 * convert((char) data[++m]);
				num += convert((char) data[++m]);
				buffer[k++] = (byte) num;
			}
		}
		byte[] result = new byte[k];
		System.arraycopy(buffer, 0, result, 0, k);
		return result;
	}

	private int convert(char c) {
		int result = 0;
		if ((c >= 'a') && (c <= 'z')) {
			result = c - 'a' + 10;
		} else if (c >= 'A' && c <= 'Z') {
			result = c - 'A' + 10;
		} else if (c >= '0' && c <= '9') {
			result = c - '0';
		} else {
			result = 0;
		}
		return result;
	}
}
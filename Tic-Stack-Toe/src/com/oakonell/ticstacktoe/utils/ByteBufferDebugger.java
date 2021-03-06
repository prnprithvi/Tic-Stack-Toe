package com.oakonell.ticstacktoe.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A wrapper around a ByteBuffer that can conditionally log the components being read/written to the ByteBuffer 
 */
public class ByteBufferDebugger {
	private ByteBuffer buffer;

	public ByteBufferDebugger(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	private void log(String string) {
		// System.out.println(string);
	}

	public byte get(String string) {
		byte result = buffer.get();
		log("Reading (byte)" + string + ": " + result);
		return result;
	}

	public short getShort(String string) {
		short result = buffer.getShort();
		log("Reading (short)" + string + ": " + result);
		return result;
	}

	public int getInt(String string) {
		int result = buffer.getInt();
		log("Reading (int)" + string + ": " + result);
		return result;
	}

	public long getLong(String string) {
		long result = buffer.getLong();
		log("Reading (long)" + string + ": " + result);
		return result;
	}

	public void put(String comment, byte num) {
		log("Writing (byte)" + comment + ": " + num);
		buffer.put(num);
	}

	public void putShort(String comment, short i) {
		log("Writing (short)" + comment + ": " + i);
		buffer.putShort(i);
	}

	public void putInt(String comment, int i) {
		log("Writing (int)" + comment + ": " + i);
		buffer.putInt(i);
	}

	public void putLong(String comment, long i) {
		log("Writing (long)" + comment + ": " + i);
		buffer.putLong(i);
	}

	public void put(String comment, byte[] bytes) {
		log("Writing (byte[])" + comment + ": " + Arrays.toString(bytes));
		buffer.put(bytes);
	}

	public void get(String string, byte[] blackplayerIdBytes) {
		buffer.get(blackplayerIdBytes);
		log("Reading (byte[])" + string + ": "
				+ Arrays.toString(blackplayerIdBytes));
		return;
	}
}
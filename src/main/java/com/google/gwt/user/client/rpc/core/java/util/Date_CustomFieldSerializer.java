package com.google.gwt.user.client.rpc.core.java.util;

import java.util.Date;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for {@link java.util.Date}.
 */
public class Date_CustomFieldSerializer {
  public static void deserialize(SerializationStreamReader streamReader,
      Date instance) {
    // No fields
  }

  @SuppressWarnings("deprecation")
  public static Date instantiate(SerializationStreamReader streamReader)
      throws SerializationException {
    // return new Date(streamReader.readLong());
    return new Date(streamReader.readInt(), streamReader.readInt(),
        streamReader.readInt(), streamReader.readInt(), streamReader.readInt(),
        streamReader.readInt());
  }

  @SuppressWarnings("deprecation")
  public static void serialize(SerializationStreamWriter streamWriter,
      Date instance) throws SerializationException {
    // streamWriter.writeLong(instance.getTime());
    // System.out.println("serialize date...");
    streamWriter.writeInt(instance.getYear());
    streamWriter.writeInt(instance.getMonth());
    streamWriter.writeInt(instance.getDate());
    streamWriter.writeInt(instance.getHours());
    streamWriter.writeInt(instance.getMinutes());
    streamWriter.writeInt(instance.getSeconds());
  }
}

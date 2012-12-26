package com.google.gwt.user.client.rpc.core.java.sql;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import java.sql.Timestamp;

/**
 * Custom field serializer for {@link java.sql.Date}. Similar to Time, we use
 * the three-arg constructor to account for variances in implementations of
 * Date.
 */
public final class Timestamp_CustomFieldSerializer {

  public static void deserialize(SerializationStreamReader streamReader,
      Timestamp instance) {
    // No fields
  }

  @SuppressWarnings("deprecation")
  public static Timestamp instantiate(SerializationStreamReader streamReader)
      throws SerializationException {
    return new Timestamp(streamReader.readInt(), streamReader.readInt(),
        streamReader.readInt(), streamReader.readInt(), streamReader.readInt(),
        streamReader.readInt(), 0);
  }

  @SuppressWarnings("deprecation")
  public static void serialize(SerializationStreamWriter streamWriter,
      Timestamp instance) throws SerializationException {

    streamWriter.writeInt(instance.getYear());
    streamWriter.writeInt(instance.getMonth());
    streamWriter.writeInt(instance.getDate());
    streamWriter.writeInt(instance.getHours());
    streamWriter.writeInt(instance.getMinutes());
    streamWriter.writeInt(instance.getSeconds());
  }
}
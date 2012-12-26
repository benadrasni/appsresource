package com.google.gwt.user.client.rpc.core.java.sql;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import java.sql.Date;

/**
 * Custom field serializer for {@link java.sql.Date}. Similar to Time, we use
 * the three-arg constructor to account for variances in implementations of
 * Date.
 */
public final class Date_CustomFieldSerializer {

  public static void deserialize(SerializationStreamReader streamReader,
      Date instance) {
    // No fields
  }

  @SuppressWarnings("deprecation")
  public static Date instantiate(SerializationStreamReader streamReader)
      throws SerializationException {
    return new Date(streamReader.readInt(), streamReader.readInt(),
        streamReader.readInt());
  }

  @SuppressWarnings("deprecation")
  public static void serialize(SerializationStreamWriter streamWriter,
      Date instance) throws SerializationException {

    streamWriter.writeInt(instance.getYear());
    streamWriter.writeInt(instance.getMonth());
    streamWriter.writeInt(instance.getDate());
  }
}
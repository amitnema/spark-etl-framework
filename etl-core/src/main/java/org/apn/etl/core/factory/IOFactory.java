/*
* Copyright 2025 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License
*/
package org.apn.etl.core.factory;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apn.etl.core.io.DataReader;
import org.apn.etl.core.io.DataWriter;
import org.apn.etl.core.io.DatabaseDataReader;
import org.apn.etl.core.io.DatabaseDataWriter;
import org.apn.etl.core.io.FileDataReader;
import org.apn.etl.core.io.FileDataWriter;

/**
 * Factory for creating data readers and writers. This class provides static methods to instantiate
 * appropriate data reader and writer implementations based on the specified type.
 *
 * @author Amit Prakash Nema
 */
@UtilityClass
@Slf4j
public class IOFactory {

  /**
   * Creates a {@link DataReader} instance based on the specified type.
   *
   * @param type The type of the reader to create (e.g., "file", "database").
   * @return A {@link DataReader} instance.
   * @throws IllegalArgumentException if the reader type is not supported.
   */
  public DataReader createReader(final String type) {
    log.info("Creating reader for type: {}", type);

    switch (type.toLowerCase()) {
      case "file":
      case "filesystem":
        return new FileDataReader();
      case "database":
      case "jdbc":
        return new DatabaseDataReader();
      default:
        throw new IllegalArgumentException("Unsupported reader type: " + type);
    }
  }

  /**
   * Creates a {@link DataWriter} instance based on the specified type.
   *
   * @param type The type of the writer to create (e.g., "file", "database").
   * @return A {@link DataWriter} instance.
   * @throws IllegalArgumentException if the writer type is not supported.
   */
  public DataWriter createWriter(final String type) {
    log.info("Creating writer for type: {}", type);

    switch (type.toLowerCase()) {
      case "file":
      case "filesystem":
        return new FileDataWriter();
      case "database":
      case "jdbc":
        return new DatabaseDataWriter();
      default:
        throw new IllegalArgumentException("Unsupported writer type: " + type);
    }
  }
}

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
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
* See the License for the specific language governing permissions and
* limitations under the License
*/
package org.apn.etl.core.model;

import java.util.List;

/**
 * Schema configuration model for ETL jobs.
 *
 * <p>Defines the schema for input/output data, including field definitions and strict mode.
 *
 * @author Amit Prakash Nema
 */
public class SchemaConfig {
  private List<FieldConfig> fields;
  private boolean strictMode;

  public SchemaConfig() {}

  /**
   * Gets the list of field configurations.
   *
   * @return list of field configs
   */
  public List<FieldConfig> getFields() {
    return fields;
  }

  /**
   * Sets the list of field configurations.
   *
   * @param fields list of field configs
   */
  public void setFields(List<FieldConfig> fields) {
    this.fields = fields;
  }

  /**
   * Checks if strict mode is enabled.
   *
   * @return true if strict mode, false otherwise
   */
  public boolean isStrictMode() {
    return strictMode;
  }

  /**
   * Sets strict mode.
   *
   * @param strictMode true to enable strict mode
   */
  public void setStrictMode(boolean strictMode) {
    this.strictMode = strictMode;
  }

  /**
   * Field configuration for schema definition.
   *
   * @author Amit Prakash Nema
   */
  public static class FieldConfig {
    private String name;
    private String type;
    private boolean nullable;
    private Object defaultValue;

    public FieldConfig() {}

    public FieldConfig(String name, String type, boolean nullable) {
      this.name = name;
      this.type = type;
      this.nullable = nullable;
    }

    /**
     * Gets the field name.
     *
     * @return field name
     */
    public String getName() {
      return name;
    }

    /**
     * Sets the field name.
     *
     * @param name field name
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * Gets the field type.
     *
     * @return field type
     */
    public String getType() {
      return type;
    }

    /**
     * Sets the field type.
     *
     * @param type field type
     */
    public void setType(String type) {
      this.type = type;
    }

    /**
     * Checks if the field is nullable.
     *
     * @return true if nullable, false otherwise
     */
    public boolean isNullable() {
      return nullable;
    }

    /**
     * Sets the nullable property for the field.
     *
     * @param nullable true if nullable
     */
    public void setNullable(boolean nullable) {
      this.nullable = nullable;
    }

    /**
     * Gets the default value for the field.
     *
     * @return default value
     */
    public Object getDefaultValue() {
      return defaultValue;
    }

    /**
     * Sets the default value for the field.
     *
     * @param defaultValue default value
     */
    public void setDefaultValue(Object defaultValue) {
      this.defaultValue = defaultValue;
    }
  }
}

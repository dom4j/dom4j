package org.dom4j.persistence;

import java.net.URL;

/**
 * Überschrift:   HL7 API
 * Beschreibung:
 * Copyright:     Copyright (c) 2001
 * Organisation:  ceyoniq healthcare
 * @author
 * @version 1.0
 */

public class MarshallingContext {

  protected Class driver;
  protected URL databaseLocation;
  protected String strategy;
  protected boolean isAutoCommiting;

  public MarshallingContext(boolean isAutoCommting) {
    this.isAutoCommiting = isAutoCommiting;
  }

  public void setDatabaseDriver(Class driver) {
    this.driver = driver;
  }

  public Class getDatabaseDriver() {
    return this.driver;
  }

  public boolean isAutoCommiting() {
    return this.isAutoCommiting;
  }

  public void setDatabaseLocation(URL location) {
    this.databaseLocation = location;
  }

  public URL getDatabaseLocation() {
    return this.databaseLocation;
  }

  public void setMarshallingStrategy(String strategy) {
    this.strategy = strategy;
  }

  public String getMarshallingStrategy() {
    return this.strategy;
  }
}

package me.phoenixra.libs.atum.json.jsonpath.internal;

import me.phoenixra.libs.atum.json.jsonpath.Configuration;
import me.phoenixra.libs.atum.json.jsonpath.Option;
import me.phoenixra.libs.atum.json.jsonpath.spi.json.GsonJsonProvider;
import me.phoenixra.libs.atum.json.jsonpath.spi.json.JsonProvider;
import me.phoenixra.libs.atum.json.jsonpath.spi.mapper.GsonMappingProvider;
import me.phoenixra.libs.atum.json.jsonpath.spi.mapper.MappingProvider;

import java.util.EnumSet;
import java.util.Set;

public final class DefaultsImpl implements Configuration.Defaults {

  public static final DefaultsImpl INSTANCE = new DefaultsImpl();

  private final MappingProvider mappingProvider = new GsonMappingProvider();

  @Override
  public JsonProvider jsonProvider() {
    return new GsonJsonProvider();
  }

  @Override
  public Set<Option> options() {
    return EnumSet.noneOf(Option.class);
  }

  @Override
  public MappingProvider mappingProvider() {
    return mappingProvider;
  }

  private DefaultsImpl() {
  }

}

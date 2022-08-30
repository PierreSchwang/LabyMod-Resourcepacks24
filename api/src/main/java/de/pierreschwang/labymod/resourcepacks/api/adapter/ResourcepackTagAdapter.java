package de.pierreschwang.labymod.resourcepacks.api.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.pierreschwang.labymod.resourcepacks.api.dao.result.Resourcepack;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * TypeAdapter for parsing tags of a {@link Resourcepack} into a list of tags. This is required due
 * to the fact, that the api returns the tags as a serialised json array, inside the actual json
 * response...
 */
public class ResourcepackTagAdapter extends TypeAdapter<List<String>> {

  @Override
  public void write(JsonWriter out, List<String> value) throws IOException {
    out.beginArray();
    for (String s : value) {
      out.value(s);
    }
    out.endArray();
  }

  @Override
  public List<String> read(JsonReader in) throws IOException {
    String value = in.nextString();
    JsonElement element = JsonParser.parseString(value);
    if (!element.isJsonArray()) {
      throw new IOException(
          "Expected tags to be a JSON-Array, but instead is: " + element.getClass()
              .getSimpleName());
    }
    JsonArray array = element.getAsJsonArray();
    return StreamSupport.stream(array.spliterator(), true).map(JsonElement::getAsString)
        .collect(Collectors.toList());
  }

}
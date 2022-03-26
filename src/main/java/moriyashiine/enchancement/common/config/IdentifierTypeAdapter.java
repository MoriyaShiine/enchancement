package moriyashiine.enchancement.common.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class IdentifierTypeAdapter extends TypeAdapter<Identifier> {
	@Override
	public void write(JsonWriter out, Identifier value) throws IOException {
		out.value(value.toString());
	}

	@Override
	public Identifier read(JsonReader in) throws IOException {
		return Identifier.tryParse(in.nextString());
	}
}

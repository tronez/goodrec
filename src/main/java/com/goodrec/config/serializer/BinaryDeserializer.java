package com.goodrec.config.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.types.Binary;

import java.io.IOException;

public class BinaryDeserializer extends JsonDeserializer<Binary> {

    @Override
    public Binary deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = p.getCodec().readTree(p);
        final JsonNode dataNode = node.get("data");

        if (dataNode.isBinary() || dataNode.isTextual())
            return new Binary(dataNode.binaryValue());

        return new Binary(new byte[]{});
    }
}

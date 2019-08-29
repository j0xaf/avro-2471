package de.j0xaf.test.avro.jira2471;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS;
import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

import de.j0xaf.test.avro.jira2471.avdl.AvdlUnion1Record;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import org.apache.avro.Conversion;
import org.apache.avro.data.TimeConversions.TimestampMicrosConversion;
import org.junit.Test;

public class TestAvroJira2471 {

  /**
   * this is failing due to AVRO-2471
   */
  @Test
  public void testAvdlUnion() {
    testAvdlBug();
  }

  /**
   * A workaround is to add manually the {@link TimestampMicrosConversion}.
   */
  @Test
  public void testAvdlUnionWorkaround() {
    testAvdlBug(new TimestampMicrosConversion());
  }

  private void testAvdlBug(Conversion<?>... additionalConversions) {
    final var union1Record = AvdlUnion1Record.newBuilder()
        .setNullableInstant(Instant.now())
        .build();
    Arrays.stream(additionalConversions).forEach(c -> union1Record.getSpecificData().addLogicalTypeConversion(c));
    try (KafkaAvroSerializer s = new KafkaAvroSerializer(new MockSchemaRegistryClient(),
        Map.of(
            AUTO_REGISTER_SCHEMAS, true,
            SCHEMA_REGISTRY_URL_CONFIG, "http://localhost")
    )) {
      s.serialize("someTopic", union1Record);
    }
  }

}

package de.j0xaf.test.avro.jira2471;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS;
import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

import de.j0xaf.test.avro.jira2471.avdl.AvdlUnion1Record;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.time.Instant;
import java.util.Map;
import org.junit.Test;

public class TestAvroJira2471 {

  // this is failing due to AVRO-2471
  @Test
  public void testAvdlUnion() {
    final var union1Record = AvdlUnion1Record.newBuilder()
        .setNullableInstant(Instant.now())
        .build();
    try (KafkaAvroSerializer s = new KafkaAvroSerializer(new MockSchemaRegistryClient(),
        Map.of(
            AUTO_REGISTER_SCHEMAS, true,
            SCHEMA_REGISTRY_URL_CONFIG, "http://localhost")
    )) {
      s.serialize("someTopic", union1Record);
    }
  }

}

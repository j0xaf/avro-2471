# avro-2471
Reproducing the bug of AVRO-2471

This repository contains a JUnit test to reproduce https://issues.apache.org/jira/browse/AVRO-2471.

## Workaround

The workaround for the bug is to manually register a the conversion:

```java
AvdlUnion1Record record = ...;
record.getSpecificData().addLogicalTypeConversion(TimestampMicrosConversion());
```
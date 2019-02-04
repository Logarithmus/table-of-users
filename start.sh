#!/bin/bash
java -server -Xmx64m -Xms32m -Djava.awt.headless=true -XX:+UseSerialGC -XX:ReservedCodeCacheSize=16m -jar ./target/table-of-users-0.1.0.jar

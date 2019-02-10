#!/bin/bash
java -server -Xmx64m -Xms32m -Djava.awt.headless=true -XX:+UseSerialGC -XX:ReservedCodeCacheSize=16m -jar ./target/social-crud-0.1.1.jar

#!/bin/bash
cd "$(dirname "$0")"
mvn compile exec:java -Dexec.mainClass="Dashboard" -q
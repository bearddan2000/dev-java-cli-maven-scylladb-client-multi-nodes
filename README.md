# dev-java-cli-maven-scylladb-client-multi-nodes

## Description
A POC for connecting scylladb cluster with cql api to java.
Creates keyspace, table, and populates table with data.

## Tech stack
- java
- maven
  - scylla connector

## Docker stack
- scylladb/scylla
- maven:3-openjdk-17

## To run
`sudo ./install.sh -u`

## To stop
`sudo ./install.sh -d`

## For help
`sudo ./install.sh -h`

## Credit
- [Scylladb docker](https://opensource.docs.scylladb.com/stable/operating-scylla/procedures/tips/best-practices-scylla-on-docker.html)
- [Scylladb docs application.conf example](https://java-driver.docs.scylladb.com/stable/manual/core/)
- [Scylladb springboot](https://github.com/eugenp/tutorials/tree/master/persistence-modules/scylladb)
- [Scylladb cluster doc](https://opensource.docs.scylladb.com/stable/operating-scylla/procedures/tips/best-practices-scylla-on-docker.html)
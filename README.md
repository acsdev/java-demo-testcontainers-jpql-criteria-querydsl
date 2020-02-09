# java-demo-testcontainers-jpql-criteria-querydsl

This demo has 2 gols.

First, comparing JPQL, Criteria and QueryDSL showing by code their differences.

Second, show how to use testcontainers library in a pratical way.

## This repository has two projects, they are organized in this way

- project-main
  - Uses JPQL, Criteria and QueryDSL
  - Build a web application (**project-main.war**)
  - The web application exposes **http resources** where each one of them calls a method that uses JPQL Criteria or QueryDSL to retrive data from oracle database.
- project-test-container
  - Uses jax-rs as a http client to call the resources expose by **project-main**
  - Uses **apache-ibatis** to run script that creates database and load their tables
  - Runs has two containers:
    1. Oracle container that will be use as source of date
    2. Payara container that will run a web app which expose the http resouces that all retrive data from oracle container.
 
  
## Recomendation
  
First time that oracle container runs takes to long because it actually install the database. To avoid that this process runs everytime, run the first time manually, following the steps bellow.


#### create a dir to keep oracle database files
mkdir -p <ADDRESS_IN_HOST_MACHINE_TO_KEEP_ORACLEP_FILES>

#### create full permission of that dir
chmod -R 777 <ADDRESS_IN_HOST_MACHINE_TO_KEEP_ORACLEP_FILES>

#### Run a oracle container using <ADDRESS_IN_HOST_MACHINE_TO_KEEP_ORACLEP_FILES> as a shared volume
docker run --name oracle-18c-xe \
  -p 8080:8080 \
  -p 1521:1521 \
  -p 5500:5500 \
  -e ORACLE_BASE=/opt/oracle \
  -e ORACLE_SID=XE \
  -e ORACLE_PDB=oracle \
  -e ORACLE_PWD=oracle \
  -v <ADDRESS_IN_HOST_MACHINE_TO_KEEP_ORACLEP_FILES>:/opt/oracle/oradata \
  --detach \
  --privileged \
  acsdev00/oracle-18-doc4-0-xe

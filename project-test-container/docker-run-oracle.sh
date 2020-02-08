# mkdir -p ~/Dev/Docker/Volume/oracle-18c-xe
#chmod -R 777 ~/Dev/Docker/Volume/oracle-18c-xe

docker run --name oracle-18c-xe \
  -p 8080:8080 \
  -p 1521:1521 \
  -p 5500:5500 \
  -e ORACLE_BASE=/opt/oracle \
  -e ORACLE_SID=XE \
  -e ORACLE_PDB=oracle \
  -e ORACLE_PWD=oracle \
  -v ~/Dev/Docker/Volume/oracle-18c-xe:/opt/oracle/oradata \
  --detach \
  --privileged \
  acsdev00/oracle-18-doc4-0-xe
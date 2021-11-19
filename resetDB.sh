#!/bin/bash

sqlFolder="src/test/resources/sql/dev/"
mysqlShell="/usr/local/mysql/bin/mysql"
dbHost="127.0.0.1"
dbName="seedling_dev"
dbUsername="root"
dbPassword="root"

while IFS= read -r -d '' sqlFile; do
  echo "$sqlFile"" -> "$dbName
  $mysqlShell -h$dbHost -u$dbUsername -p$dbPassword $dbName <"$sqlFile"
done < <(find $sqlFolder -name '*.sql' -print0)
echo "import $dbName done"

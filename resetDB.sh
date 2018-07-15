#!/bin/bash

for i in $(find src/test/resources/sql/dev/*.sql) ; do
  mysql -uroot -proot seedling_dev < ${i};
done

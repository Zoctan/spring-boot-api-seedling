@echo off & color 0A

set sqlFolder="src/test/resources/sql/dev/"
set mysqlExe="C:\phpstudy\Extensions\MySQL8.0.12\bin\mysql.exe"
set dbHost="127.0.0.1"
set dbName="seedling_dev"
set dbUsername="root"
set dbPassword="root"

for /R %sqlFolder% %%s in (*.sql) do (
  echo %%s
  %mysqlExe% -u%dbUsername% -p%dbPassword% %dbName% < %%s
)
echo "import %dbName% done"
pause
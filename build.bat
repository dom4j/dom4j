@echo off
if "%JAVA_HOME%" == "" goto error

REM 
set JAVA=%JAVA_HOME%\bin\java
REM set cp=
for %%i in (lib\*.jar) do call cp.bat %%i
set CP=build\classes;%JAVA_HOME%\lib\tools.jar;%CP%
REM set CP=lib\ant.jar;lib\xalan.jar;lib\crimson.jar;lib\junit.jar;lib\fop.jar;lib\batik.jar;%JAVA_HOME%\lib\tools.jar;%CP%
%JAVA% -classpath %CP% -Dant.home=lib org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

goto end

:error

echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

:end

set CP=


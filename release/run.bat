@echo off

cd %~dp0

setlocal EnableDelayedExpansion
Set cp=.\lib\classes
for %%p in (.\lib\*.*) do (
	set cp=!cp!;%%p
)

echo %cp%

java -classpath %cp% com.mu.stock.ui.DataAnalyserApp 


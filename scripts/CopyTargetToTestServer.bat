@echo off
echo Copying target to test server...
COPY .\target\Verilock-1.0-SNAPSHOT.jar .\testserver\plugins\Verilock.jar
echo Copy operation finished.
@echo on
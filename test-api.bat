@echo off
REM Test script for Soar Maps API

echo Testing Soar Maps API...
echo.

REM Test with the coordinates from the requirements
echo Test 1: Point at Lat: 25.25306481 / Lng: 22.27009466
echo Expected: Should return "North Africa Region" and "Sahara Desert Section"
curl -s "http://localhost:8080/map-layers/search?latitude=25.25306481&longitude=22.27009466"
echo.
echo.

REM Test with a point outside all layers
echo Test 2: Point at Lat: 10.0 / Lng: 10.0
echo Expected: Should return empty array
curl -s "http://localhost:8080/map-layers/search?latitude=10.0&longitude=10.0"
echo.
echo.

REM Test with a point in the Mediterranean Coast layer
echo Test 3: Point at Lat: 30.0 / Lng: 20.0
echo Expected: Should return "Mediterranean Coast" layer
curl -s "http://localhost:8080/map-layers/search?latitude=30.0&longitude=20.0"
echo.
echo.

echo Tests completed!



docker build . -t jaokim_div_crashes
docker run -ti -v %cd%/:/work --rm jaokim_div_crashes:latest

pause
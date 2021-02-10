
docker build . -t jaokim_div_crashes
docker run -ti -v $(pwd)/:/work --rm jaokim_div_crashes:latest

pause
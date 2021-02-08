
docker build . -t dumpster
docker run -ti -v %cd%/:/work --rm dumpster:latest

pause
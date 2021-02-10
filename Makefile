
all : setup java_src native_lib

setup:
	mkdir build

java_src: 
	javac -d build/ -cp build/ ./src/jaokim/dumpster/*.java
	javac -h src -cp build/ ./src/jaokim/dumpster/Divider.java

jaokim_dumpster_Divider.o:
	g++ -c -D__int64="long long" -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux src/jaokim_dumpster_Divider.cpp -o build/jaokim_dumpster_Divider.o

native_lib : divider.so libdump.so 

divider.so: FORCE
	jaotc --output build/divider.so -J-cp -J./build/ --class-name jaokim.dumpster.Divider 
	
#Linux version
libdump.so : jaokim_dumpster_Divider.o
	g++ -shared -fPIC -o build/$@ build/jaokim_dumpster_Divider.o -lc

#Windows version:
dump.dll: 
	g++ -c -I%JAVA_HOME%\include -I%JAVA_HOME%\include\win32 jaokim_dumpster_Divider.cpp -o build/$@ 

#MacOS version;
dump.dylib:
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin jaokim_dumpster_Divider.cpp -o build/$@ 

clean: 
	rm -r build/*


test_loops : FORCE
	java -cp "build/" -Djava.library.path="/work/build" jaokim.dumpster.Dumpster 10


test_many_loops: FORCE
	java -cp "build/" -Djava.library.path="/work/build" jaokim.dumpster.Dumpster 1000

test_aot : FORCE
	java -XX:+UnlockExperimentalVMOptions -XX:AOTLibrary="./build/divider.so" -cp "build/" -Djava.library.path="/work/build" jaokim.dumpster.Dumpster 1000


	
FORCE:

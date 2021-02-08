
all : java_src native_lib

java_src: 
	javac -h src src/se/oracle/jaokim/dumpster/Dumpster.java
	javac src/se/oracle/jaokim/dumpster/Dumpster.java

se_oracle_jaokim_dumpster_Dumpster.o:
	g++ -c -D__int64="long long" -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux src/se_oracle_jaokim_dumpster_Dumpster.cpp -o build/se_oracle_jaokim_dumpster_Dumpster.o

native_lib : libdump.so 

#Linux version
libdump.so : se_oracle_jaokim_dumpster_Dumpster.o
	g++ -shared -fPIC -o build/$@ build/se_oracle_jaokim_dumpster_Dumpster.o -lc

#Windows version:
dump.dll: 
	g++ -c -I%JAVA_HOME%\include -I%JAVA_HOME%\include\win32 java_inside_jaokim_dumpster_Dumpster.cpp -o build/$@ 

#MacOS version;
dump.dylib:
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin java_inside_jaokim_dumpster_Dumpster.cpp -o build/$@ 

clean: 
	rm build/*
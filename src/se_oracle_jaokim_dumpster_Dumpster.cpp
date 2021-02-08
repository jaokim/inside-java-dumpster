
#include <inttypes.h>
#include <cstdio>
#include <cstdlib>

#include "se_oracle_jaokim_dumpster_Dumpster.h"

JNIEXPORT void JNICALL Java_se_oracle_jaokim_dumpster_Dumpster_div_1operator
  (JNIEnv *env, jobject jobj, jint j) {
	  int i = 9;
	  j = j - 9;
	  j = i / j;
	  
	  return;
}
  
JNIEXPORT void JNICALL Java_se_oracle_jaokim_dumpster_Dumpster_div_1call
  (JNIEnv *, jobject, jint j) {
	div_t h;
	int i = 9;
	j = j - 9;
	h = div(i, j);
	
	return;
}

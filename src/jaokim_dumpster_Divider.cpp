
#include <inttypes.h>
#include <cstdio>
#include <cstdlib>

#include "jaokim_dumpster_Divider.h"

void div_libc(int num, int denom) {
	div_t h;
	h = div(num, denom);

	return;
}
  
JNIEXPORT void JNICALL Java_jaokim_dumpster_Divider_native_1div_1call
  (JNIEnv *, jobject, jint denom) {
	int num = 9;
	div_libc(num, denom);
	return;
}

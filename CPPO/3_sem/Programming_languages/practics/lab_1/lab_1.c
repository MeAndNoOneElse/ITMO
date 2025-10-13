#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

typedef struct {
	int x;
	int y;
} Cords;
typedef struct {
	int soldiers;
	char* Soutout;
	int number;
	Cords cords;
} Arm;
Arm* createArm(int soldiers, char* Soutout, int number, Cords cords) {
	Arm* arm = (Arm*)malloc(sizeof(Arm));
	if (!arm) {
		fprintf(stderr, "Memory allocation failed\n");
		return NULL;
	}
	arm->cords = cords;
	arm->soldiers = soldiers;
	arm->Soutout = Soutout;
	arm->number = number;
	return arm;


}
int main() {
	return 0;
}

import math


def fibonacci(n):
	if(n > 2):
		return fibonacci(n - 1) + fibonacci(n - 2)
	elif(n > 0):
		return 1
	else:
		return 0

n = input()
	
print(fibonacci(int(n)))	
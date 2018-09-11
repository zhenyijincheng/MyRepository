import math

i1 = (1 + math.sqrt(5)) / 2

i2 = (1 - math.sqrt(5)) / 2

i3 = math.sqrt(5) / 5

def fibonacci(n):
	print(str(i3) + " " + str(pow(i1,n)) + " " + str(pow(i2,n)))	
	return i3 * (pow(i1,n) - pow(i2,n))

n = input()
	
print(fibonacci(int(n)))	
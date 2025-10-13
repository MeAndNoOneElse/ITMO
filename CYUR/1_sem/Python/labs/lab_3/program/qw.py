import time

def f(n):
    if n == 1: return 1
    if n == 2: return 2
    if n >= 3: return f(n-1)*n
def data(iterations, filename="timing_log.txt"):
    with open(filename, 'w') as file:
        for i in range(iterations):
            start_time = time.time()
            f(10)
            end_time = time.time()
            execution_time = (end_time - start_time)*10000000
            file.write(f"{execution_time:2.0f}\n")
data(10**4, "performance_log.csv")
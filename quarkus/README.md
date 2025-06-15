Spring-Boot App

Use GraalVM CE 23.0.2+7.1

Compile application in native
```console
./gradlew spring-boot:nativeCompile 
./gradlew spring-boot:build
```
Start in normal mode
```console
java -jar ./spring-boot/build/libs/spring-boot-1.0.1.jar
```
Start native image
```console
./spring-boot/build/native/nativeCompile/spring-boot
```
Performance test
```console
wrk -t10  -c100  -d60s -s wrk/messages.lua http://localhost:8080/messages
wrk -t100 -c400  -d60s -s wrk/messages.lua http://localhost:8080/messages
wrk -t300 -c1000 -d60s -s wrk/messages.lua http://localhost:8080/messages
```
Normal Mode
```
10 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.14ms    6.85ms 155.90ms   90.48%
    Req/Sec   400.73     35.52   505.00     77.82%
  239578 requests in 1.00m, 29.97MB read
Requests/sec:   3987.86
Transfer/sec:    510.89KB

100 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    98.19ms   21.31ms 352.31ms   82.01%
    Req/Sec    40.79      8.01    80.00     60.08%
  244415 requests in 1.00m, 30.58MB read
  Socket errors: connect 0, read 533, write 0, timeout 0
Requests/sec:   4066.96
Transfer/sec:    521.02KB

300 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   224.11ms   35.71ms 699.51ms   81.72%
    Req/Sec    13.71      5.05    40.00     63.37%
  240311 requests in 1.00m, 30.05MB read
  Socket errors: connect 0, read 4299, write 0, timeout 0
Requests/sec:   3998.22
Transfer/sec:    512.05KB
```
Native image
```
10 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    25.62ms    7.11ms 108.39ms   90.31%
    Req/Sec   392.77     31.02   490.00     73.07%
  234869 requests in 1.00m, 29.38MB read
Requests/sec:   3909.52
Transfer/sec:    500.85KB

100 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   126.06ms   29.96ms 495.78ms   80.82%
    Req/Sec    31.75      9.54    80.00     72.25%
  190309 requests in 1.00m, 23.80MB read
  Socket errors: connect 0, read 1166, write 0, timeout 0
Requests/sec:   3166.70
Transfer/sec:    405.61KB

300 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   281.92ms   39.83ms 598.01ms   81.18%
    Req/Sec    11.28      4.76    40.00     67.52%
  190776 requests in 1.00m, 23.87MB read
  Socket errors: connect 0, read 6095, write 0, timeout 0
Requests/sec:   3174.85
Transfer/sec:    406.71KB
```
Normal Mode with virtual threads
```
10 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    27.18ms    6.82ms 131.26ms   90.50%
    Req/Sec   370.79     42.08   464.00     76.57%
  221749 requests in 1.00m, 27.74MB read
Requests/sec:   3690.80
Transfer/sec:    472.84KB

100 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   100.75ms   21.61ms 464.52ms   92.95%
    Req/Sec    39.76      6.44    90.00     73.41%
  238322 requests in 1.00m, 29.81MB read
  Socket errors: connect 0, read 491, write 0, timeout 0
Requests/sec:   3965.25
Transfer/sec:    507.91KB

300 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   222.55ms   44.77ms 879.59ms   95.76%
    Req/Sec    13.70      4.92    50.00     66.74%
  241719 requests in 1.00m, 30.23MB read
  Socket errors: connect 0, read 5704, write 0, timeout 0
Requests/sec:   4021.69
Transfer/sec:    515.05KB
```
Native image with virtual threads
```
10 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    26.68ms    7.14ms 103.67ms   92.09%
    Req/Sec   377.64     34.62   454.00     79.93%
  225860 requests in 1.00m, 28.26MB read
Requests/sec:   3759.26
Transfer/sec:    481.60KB

100 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   127.23ms   35.65ms 515.58ms   89.16%
    Req/Sec    31.83     10.23    70.00     70.60%
  189102 requests in 1.00m, 23.65MB read
  Socket errors: connect 0, read 274, write 0, timeout 0
Requests/sec:   3146.26
Transfer/sec:    402.99KB

300 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   300.86ms   78.35ms   1.01s    93.44%
    Req/Sec    10.79      5.14    50.00     65.52%
  178992 requests in 1.00m, 22.39MB read
  Socket errors: connect 0, read 6469, write 0, timeout 0
Requests/sec:   2978.14
Transfer/sec:    381.39KB
```
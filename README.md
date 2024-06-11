Demonstrates using ScopedValue for tracing library.

generate output similar to:

```
request 913, total duration 125ms
request 245, total duration 86ms
request 50, total duration 90ms
request 544, event Event[start=1718119287317, end=1718119287373, description=method handleRequest], duration 56ms
request 967, event Event[start=1718119287323, end=1718119287422, description=method callServer], duration 99ms
request 989, total duration 125ms
request 570, total duration 129ms
request 919, event Event[start=1718119287323, end=1718119287373, description=method handleRequest], duration 50ms
request 980, event Event[start=1718119287323, end=1718119287423, description=method handleRequest], duration 100ms
request 956, event Event[start=1718119287323, end=1718119287377, description=method handleRequest], duration 54ms
request 948, total duration 131ms
request 831, total duration 80ms
request 802, total duration 134ms
```

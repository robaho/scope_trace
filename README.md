Demonstrates using ScopedValue for tracing library.

generate output similar to:

```
request 841, event Event[start=1718117820928, end=1718117821023, description=method callServer], duration 95ms
request 841, event Event[start=1718117820928, end=1718117821023, description=method handleRequest], duration 95ms
request 993, event Event[start=1718117820930, end=1718117821024, description=method callServer], duration 94ms
request 909, event Event[start=1718117820929, end=1718117821024, description=method callServer], duration 95ms
request 909, event Event[start=1718117820929, end=1718117821024, description=method handleRequest], duration 95ms
request 769, event Event[start=1718117820927, end=1718117821024, description=method callServer], duration 97ms
request 769, event Event[start=1718117820927, end=1718117821024, description=method handleRequest], duration 97ms
request 993, event Event[start=1718117820930, end=1718117821024, description=method handleRequest], duration 94ms
request 944, event Event[start=1718117820929, end=1718117821024, description=method callServer], duration 95ms
request 944, event Event[start=1718117820929, end=1718117821024, description=method handleRequest], duration 95ms
request 857, event Event[start=1718117820928, end=1718117821025, description=method callServer], duration 97ms
request 857, event Event[start=1718117820928, end=1718117821025, description=method handleRequest], duration 97ms
request 844, event Event[start=1718117820928, end=1718117821026, description=method callServer], duration 98ms
request 844, event Event[start=1718117820928, end=1718117821026, description=method handleRequest], duration 98ms
request 915, event Event[start=1718117820929, end=1718117821026, description=method callServer], duration 97ms
request 915, event Event[start=1718117820929, end=1718117821026, description=method handleRequest], duration 97ms
request 781, event Event[start=1718117820927, end=1718117821026, description=method callServer], duration 99ms
request 781, event Event[start=1718117820927, end=1718117821026, description=method handleRequest], duration 99ms
```

Demonstrates using ScopedValue for tracing library.

generate output similar to:

```
event Event[start=1718643580403, end=1718643580403, description=close span making jdbc call, span=7336, tags [jdbc], parent 782], duration 0ms
event Event[start=1718643580193, end=1718643580403, description=close span handleRequest, span=782, tags [request 382], parent -1], duration 210ms
event Event[start=1718643580407, end=1718643580407, description=call jdbc, span=7380, tags [jdbc], parent 888], duration 0ms
event Event[start=1718643580416, end=1718643580416, description=close span making jdbc call, span=7505, tags [jdbc], parent 889], duration 0ms
event Event[start=1718643580186, end=1718643580418, description=close span handleRequest, span=226, tags [request 113], parent -1], duration 232ms
event Event[start=1718643580197, end=1718643580431, description=close span handleRequest, span=1031, tags [request 501], parent -1], duration 234ms
event Event[start=1718643580431, end=1718643580431, description=call jdbc, span=7687, tags [jdbc], parent 2093], duration 0ms
event Event[start=1718643580431, end=1718643580431, description=close span making jdbc call, span=7687, tags [jdbc], parent 2093], duration 0ms
event Event[start=1718643580212, end=1718643580431, description=close span handleRequest, span=2093, tags [request 993], parent -1], duration 219ms
event Event[start=1718643580433, end=1718643580433, description=call jdbc, span=7702, tags [jdbc], parent 524], duration 0ms
event Event[start=1718643580433, end=1718643580433, description=close span making jdbc call, span=7702, tags [jdbc], parent 524], duration 0ms
event Event[start=1718643580191, end=1718643580433, description=close span handleRequest, span=524, tags [request 257], parent -1], duration 242ms
event Event[start=1718643580198, end=1718643580435, description=close span handleRequest, span=1092, tags [request 528], parent -1], duration 237ms
event Event[start=1718643580407, end=1718643580407, description=close span making jdbc call, span=7380, tags [jdbc], parent 888], duration 0ms
event Event[start=1718643580195, end=1718643580407, description=close span handleRequest, span=888, tags [request 433], parent -1], duration 212ms
event Event[start=1718643580195, end=1718643580416, description=close span handleRequest, span=889, tags [request 434], parent -1], duration 221ms
```

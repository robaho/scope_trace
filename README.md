Demonstrates using ScopedValue for tracing library.

generate output similar to:

```
Event[@1718673016553 for 0ms, span 1, tags [], parent -1, "open request 0"
Event[@1718673016554 for 0ms, span 1, tags [], parent -1, "request starting"
Event[@1718673016561 for 0ms, span 2, tags [jdbc, table1], parent 1, "open jdbc call"
Event[@1718673016561 for 0ms, span 2, tags [jdbc, table1], parent 1, "call jdbc"
Event[@1718673016562 for 0ms, span 3, tags [], parent 1, "open request 0.subtask"
Event[@1718673016562 for 0ms, span 4, tags [jdbc, table2], parent 3, "open jdbc call"
Event[@1718673016562 for 0ms, span 4, tags [jdbc, table2], parent 3, "call jdbc"
Event[@1718673016562 for 0ms, span 4, tags [jdbc, table2], parent 3, "close jdbc call"
Event[@1718673016563 for 0ms, span 5, tags [], parent 3, "open call service"
Event[@1718673016561 for 10ms, span 2, tags [jdbc, table1], parent 1, "close jdbc call"
Event[@1718673016563 for 68ms, span 5, tags [], parent 3, "close call service"
Event[@1718673016562 for 69ms, span 3, tags [], parent 1, "close request 0.subtask"
Event[@1718673016633 for 0ms, span 1, tags [], parent -1, "request cleanup"
Event[@1718673016553 for 80ms, span 1, tags [], parent -1, "close request 0"
```

# Concurrent Boolean Circuit Solver

## Overview
This project implements a **concurrent solver** for tree-structured Boolean expressions. It evaluates Boolean circuits using parallel computation, supporting:

- Short-circuit evaluation for AND/OR operations
- Conditional IF expressions
- Threshold expressions (GTx/LTx)
- Safe interruption of ongoing computations via `stop()`
- Multiple simultaneous `solve()` requests

## Features
- Fully concurrent evaluation of sub-expressions
- Handles multiple circuits independently
- Implemented in Java using threads and concurrency utilities


# Disclaimer
All projects that start with `dev`
are under active development.

This project is in development meaning
it does not produce expected results.

# Problem
Error creating scylladb cluster. 
- Need a wait and seed command on node1.
- There's also an error for async io: `(Could not setup Async I/O: Resource temporarily unavailable. The most common cause is not enough request capacity in /proc/sys/fs/aio-max-nr. Try increasing that number or reducing the amount of logical CPUs available for your application)
` seems to be all nodes.

# Possible solutions
  - Need to know the exact command to start database.
  - Adjust async io for servers
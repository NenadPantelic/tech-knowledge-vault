# Containers from scratch

- Namespaces
- Chroot
- Cgroups (Control groups)


## Namespaces

- limit what the container can see
- created with syscalls

    - Unix Timesharing System
    - Process IDS
    - Mounts
    - Networks
    - User IDS
    - InterProcess Comms


- `ps` gets the list of process from the `/proc` directory
- whenever you do `ls`, you start a new process

- `proc` is a pseudo filesystem; a mechanism for the kernel and user namespace to share information

## CGroups (control groups)

- what the container can use
- filesystem interface
    - memory
    - CPU
    - I/O
    - process numbers
    - ...

- `/sys/fs/cgroup/` - here the OS creates all resource paramaters/limits for all resources controlled by cgroups
- `docker run --rm -it -memory=10M ubuntu /bin/bash` - kernel limits this particular container to 10M

- forkbomb - `:() { : | : &}; :` - we define a function named `:` without any parameters; in the function itself, it make a recursive call of `:` and we pipe it; finally we invoke the function `:` -> it will create a process for every invoked `:` 
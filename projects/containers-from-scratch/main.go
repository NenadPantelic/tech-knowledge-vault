package main

// docker 		  run image <cmd> <params>
// go run main.go run 		<cmd> <params>
import (
	"fmt"
	"os"
	"os/exec"
	"syscall"
)

func main() {
	switch os.Args[1] {
	case "run":
		run()

	case "child":
		child()

	default:
		panic("bad command")
	}
}


func run() {
	fmt.Printf("Running %v as %d\n", os.Args[2:], os.Getpid())

	cmd := exec.Command("/proc/self/exe", append([]string{"child"}, os.Args[2:]...)...) // the program will run itself in a new namespace

	cmd.Stdin = os.Stdin // wire up the stdin, stdout, stderr
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	cmd.SysProcAttr = &syscall.SysProcAttr{
		Cloneflags: syscall.CLONE_NEWUTS | syscall.CLONE_NEWPID | syscall.CLONE_NEWNS,
		Unshareflags: syscall.CLONE_NEWNS 
		// CLONE_NEWUTS
		// Unix Timesharing System and the
		//  we have the hostname in this namespace; the container will be able to see only its namespace, 
		// but not what happens on the host OS

		// Host process
		// └── run()
		// 		└── New namespaces created
		// 			└── child() — PID 1 inside the new PID namespace
		// 					└── /bin/sh

		// syscall.CLONE_NEWNS -> the fist NS invented and added to the kernel
		
		
		// Unshareflags: syscall.CLONE_NEWNS 
		// there are recurisevely shared properites (with the root NS from the OS); turn that off; 
		// so that won't be shared with the host
		// if we do now: mount | grep proc on the host OS, we won't see that proc NS; but we can see them from the host OS if we run a process in the container
		// and then do on the host OS -> cat /proc/<PID>/mounts -> it will give us the mounted NS

	}


	if err := cmd.Run(); err != nil {
		fmt.Fprintf(os.Stderr, "command failed: %v\n", err)
	} // the command is not executed until we hit this Run command
}


func child() { // when we execute this, it will be already in the new namespace
	fmt.Printf("Running %v as %d\n", os.Args[2:], os.Getpid())
	cg()

	syscall.Sethostname([]byte("container")) //  it will be in new namespace, so the host should say "container" in terminal when (nenad@container...)
	// this function has been executed
	// so once the operation is executed now, we will see that the root location is /vagrant/ubuntu-fs; you cannot see anything above in the 
	// file system
	// if we do (from the host OS) - ls -l /proc/<PID> we should see that its root is `/vagrant/ubuntu-fs`
	// but e.g. if we run the sleep command (e.g. sleep 100) in the container, the host OS can still see that process
	
	syscall.Chroot("/vagrant/ubuntu-fs") // it changes the root
	syscalll.Chdir("/") 

	// `proc` is a pseudo filesystem; a mechanism for the kernel and user namespace to share information
	// so, if we run it in container, we need to mount it, so the kernel knows to populate the processes information
	syscall.Mount("proc", "proc", "proc", 0, "") // source, target, fstype, flags, data

	// if we do `mount` in the container ns -> proc on /proc type proc
	// if we do `mount | grep proc` in the host OS -> proc on /vagrant/ubuntu-fs/proc type proc
	
	cmd := exec.Command(os.Args[2], os.Args[3:]...)
	cmd.Stdin = os.Stdin // wire up the stdin, stdout, stderr
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	if err := cmd.Run(); err != nil {
		fmt.Fprintf(os.Stderr, "command failed: %v\n", err)
	}

	syscall.Unmount("/proc", 0) // unmount once we are done
}


func cg() {
	cgroups := "/sys/fs/cgroup"
	pids := filepath.Join(cgroups, "pids")
	err := os.Mkdir(filepath.Join(pids, "np"), 0755)
	if err != nil && !os.IsExist(err) {
		panic(err)
	}

	must(ioutil.WriteFile(filepath.Join(pids, "np/pids.max"), []byte("20"), 0700)) // max 20 processes in this control group
	// Removes the new cgroup in place after the container exits
	must(ioutil.WriteFile(filepath.Join(pids, "np/notify_on_release"), []byte("1"), 0700))
	must(ioutil.WriteFile(filepath.Join(pids, "np/cgroup.procs"), []byte(strconv.Itoa(os.Getpid())), 0700)) // add process to "np" control group (this file)
}


func must(err error) {
	if err != nil {
		panic(err)
	}
}
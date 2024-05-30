# Network Research Project

This Java program is designed to automate the process of installing necessary network tools, anonymizing network traffic, and conducting remote scans on specified IP addresses or domains. It leverages tools like Tor, Nipe, GeoIP, and others to ensure anonymity and perform comprehensive network research.

## Features

- **Package Installation**: Automatically checks for and installs required network packages (`tor`, `torify`, `git`, `geoip-bin`, `sshpass`).
- **Nipe Management**: Clones and installs the Nipe tool to anonymize network traffic through the Tor network.
- **Anonymity Check**: Verifies if the host's IP is anonymized by comparing the original and spoofed IP addresses and their respective countries.
- **Remote Server Interaction**: Uses `sshpass` to execute commands on a remote server, including fetching server uptime and current IP address, performing WHOIS lookups, and conducting Nmap scans.

## How to Run This Project

### Prerequisites

- **Java Development Kit (JDK)**: Ensure you have the JDK installed on your system.
  - On Ubuntu/Debian:
    ```bash
    sudo apt update
    sudo apt install openjdk-11-jdk
    ```
  - On Windows or MacOS: Download and install the JDK from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

### Steps to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/YOUR_GITHUB_USERNAME/network-research-project.git
   cd network-research-project
2. 
Sure! You can include detailed instructions on how to run the project in your GitHub repository's README file. Here's an example README file with the added section on how to run the project:

markdown
Copy code
# Network Research Project

This Java program is designed to automate the process of installing necessary network tools, anonymizing network traffic, and conducting remote scans on specified IP addresses or domains. It leverages tools like Tor, Nipe, GeoIP, and others to ensure anonymity and perform comprehensive network research.

## Features

- **Package Installation**: Automatically checks for and installs required network packages (`tor`, `torify`, `git`, `geoip-bin`, `sshpass`).
- **Nipe Management**: Clones and installs the Nipe tool to anonymize network traffic through the Tor network.
- **Anonymity Check**: Verifies if the host's IP is anonymized by comparing the original and spoofed IP addresses and their respective countries.
- **Remote Server Interaction**: Uses `sshpass` to execute commands on a remote server, including fetching server uptime and current IP address, performing WHOIS lookups, and conducting Nmap scans.

## How to Run This Project

### Prerequisites

- **Java Development Kit (JDK)**: Ensure you have the JDK installed on your system.
  - On Ubuntu/Debian:
    ```bash
    sudo apt update
    sudo apt install openjdk-11-jdk
    ```
  - On Windows or MacOS: Download and install the JDK from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

### Steps to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/YOUR_GITHUB_USERNAME/network-research-project.git
   cd network-research-project
   
**2. Compile the Java Program:**
```bash
   javac NetworkResearchProject.java
   ```
**3.Run the Java Program:**
```bash
  java NetworkResearchProject
```

**Notes**
_Permissions_: Ensure you have the necessary permissions to execute the required system commands (e.g., sudo access for installing packages).
_Dependencies_: The program relies on external packages and services (e.g., sshpass, git, geoiplookup). Ensure these are installed on your system.
_File_ _Paths_: Adjust file paths if needed, especially for logging and saving outputs.

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class NetworkResearchProject {
    private static String executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    private static void checkPackageStatus(String packageName) throws IOException, InterruptedException {
        String checkCommand = String.format("dpkg -s %s | grep -i status | awk '{print $4}'", packageName);
        String status = executeCommand(checkCommand).trim();
        if ("installed".equals(status)) {
            System.out.println("[+] " + packageName + " is installed");
        } else {
            System.out.println("[++] Not installed, initiating " + packageName + " installation......");
            executeCommand("sudo apt-get install -y " + packageName);
            System.out.println("[+] " + packageName + " installed completely!");
        }
    }

    private static void checkPackageStatusAlt(String packageName) throws IOException, InterruptedException {
        String checkCommand = String.format("command -v %s", packageName);
        String status = executeCommand(checkCommand).trim();
        if (!status.isEmpty()) {
            System.out.println("[+] " + packageName + " is installed");
        } else {
            System.out.println("[++] Not installed, initiating " + packageName + " installation......");
            executeCommand("sudo apt-get install -y " + packageName);
            System.out.println("[+] " + packageName + " installed completely!");
        }
    }

    private static boolean isNipeInstalled() throws IOException, InterruptedException {
        String nipeCheckCommand = "ls -d nipe && command -v tor && command -v iptables";
        String lwpCheckCommand = "perl -MLWP::UserAgent -e 'print \"LWP::UserAgent module is installed\n\"'";
        String configCheckCommand = "perl -MConfig::Simple -e 'print \"Config::Simple module is installed\n\"'";

        String nipeCheck = executeCommand(nipeCheckCommand);
        String lwpCheck = executeCommand(lwpCheckCommand);
        String configCheck = executeCommand(configCheckCommand);

        return !nipeCheck.isEmpty() && !lwpCheck.isEmpty() && !configCheck.isEmpty();
    }

    private static void installNipe() throws IOException, InterruptedException {
        System.out.println("[+] Nipe not installed");
        Thread.sleep(3000);

        executeCommand("rm -R nipe");
        System.out.println("[+++] Cloning nipe from GitHub");
        executeCommand("git clone https://github.com/htrgouvea/nipe.git");
        System.out.println("[+] Nipe cloned completely.......");

        executeCommand(
                "cd nipe && sudo cpan install -y Switch JSON LWP::UserAgent && sudo cpan install Config::Simple");
        executeCommand("cd nipe && sudo perl nipe.pl install");
        System.out.println("Installation complete!");
        Thread.sleep(2000);
    }

    public static void main(String[] args) {
        try {
            System.out.println("            Network Research Project");
            System.out.println();
            System.out.println("Author: Raunak Rajendra Mankapure");
            System.out.println("Date developed: May 2024");

            // Save current path
            String path = System.getProperty("user.dir");

            // Check and install required packages
            checkPackageStatus("tor");
            checkPackageStatusAlt("torify");
            checkPackageStatus("git");
            checkPackageStatus("geoip-bin");
            checkPackageStatusAlt("sshpass");

            // Check and install Nipe if necessary
            if (!isNipeInstalled()) {
                installNipe();
            } else {
                System.out.println("[+] Nipe is installed");
            }

            // Start Nipe service
            executeCommand("cd nipe && sudo perl nipe.pl start");

            // Save original public IP of host
            String hostCurrentIP = executeCommand("curl -s ifconfig.me").trim();
            String hostSpoofedIP = executeCommand("cd nipe && sudo perl nipe.pl status | grep -i ip | awk '{print $3}'")
                    .trim();
            String currentCountry = executeCommand("geoiplookup " + hostCurrentIP + " | awk -F \",\" '{print $2}'")
                    .trim();
            String spoofedCountry = executeCommand("geoiplookup " + hostSpoofedIP + " | awk -F \",\" '{print $2}'")
                    .trim();

            // Detecting Anonymity
            if (currentCountry.equals(spoofedCountry)) {
                System.out.println("[#] You are not anonymous, you are exposed!");
                Thread.sleep(2000);
                System.out.println("Stopping Nipe!!!");
                executeCommand("cd nipe && sudo perl nipe.pl stop");
                System.out.println("Nipe stopped!!!");
                System.out.println("[#] Exiting .................");
                return;
            } else {
                System.out.println("[*] You are anonymous..... Connecting to the remote server!");
                System.out.println();
            }

            // Current details
            System.out.println("[**] Your spoofed IP address is: " + hostSpoofedIP + ", Spoofed country: "
                    + spoofedCountry + "!!");
            Scanner scanner = new Scanner(System.in);
            System.out.print("[??] Specify a Domain/IP address to scan: ");
            String domainIP = scanner.nextLine();
            System.out.println();

            // Remote server details
            String remoteUsername = "root";
            String remotePassword = "root";
            String remoteHostname = "192.168.101.134";

            // Remote execution
            String remoteCommand = String.format(
                    "sshpass -p \"%s\" ssh -T %s@%s 'echo -n \"Remote server uptime: \" && uptime && echo -n \"Remote current IP: \" && curl -s ifconfig.me'",
                    remotePassword, remoteUsername, remoteHostname);
            System.out.println(executeCommand(remoteCommand));

            // Store remote IP
            String remoteIP = executeCommand(String.format("sshpass -p \"%s\" ssh -T %s@%s 'curl -s ifconfig.me'",
                    remotePassword, remoteUsername, remoteHostname)).trim();
            try (FileWriter writer = new FileWriter("serverIP.txt")) {
                writer.write(remoteIP);
            }

            // Display remote country
            String remoteCountry = executeCommand("geoiplookup $(cat serverIP.txt) | awk -F \",\" '{print $2}'").trim();
            System.out.println("Remote country: " + remoteCountry);

            // Remove stored IP file
            new File("serverIP.txt").delete();

            // Whois victim's address
            System.out.println("[*] Whois victim's address: ");
            System.out.println("[@] Whois data was saved into " + path + "/nipe/whois_" + domainIP + ".txt");

            String whoisCommand = String.format("sshpass -p \"%s\" ssh -T %s@%s 'whois %s'", remotePassword,
                    remoteUsername, remoteHostname, domainIP);
            String whoisData = executeCommand(whoisCommand);

            try (FileWriter writer = new FileWriter(path + "/nipe/whois_" + domainIP + ".txt")) {
                writer.write(whoisData);
            }

            // Log whois data
            try (FileWriter writer = new FileWriter("/var/log/netresearch.log", true)) {
                writer.write(String.format("%s - [*] Whois data collected for: %s%n", java.time.LocalDateTime.now(),
                        domainIP));
            }

            // Scan victim's address
            System.out.println("[*] Scanning victim's address: ");
            System.out.println("[@] Nmap scan data was saved into " + path + "/nipe/Nmap_" + domainIP + ".txt");

            String nmapCommand = String.format("sshpass -p \"%s\" ssh -T %s@%s 'nmap -sS -sV -F %s'", remotePassword,
                    remoteUsername, remoteHostname, domainIP);
            String nmapData = executeCommand(nmapCommand);

            try (FileWriter writer = new FileWriter(path + "/nipe/Nmap_" + domainIP + ".txt")) {
                writer.write(nmapData);
            }

            // Log Nmap data
            try (FileWriter writer = new FileWriter("/var/log/netresearch.log", true)) {
                writer.write(String.format("%s - [*] Nmap data collected for: %s%n", java.time.LocalDateTime.now(),
                        domainIP));
            }

            // Stop Nipe
            executeCommand("cd nipe && sudo perl nipe.pl stop");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
